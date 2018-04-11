package cz.i.cache;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class CodebookCache<K extends Comparable<K>, V> {

  private static final Logger LOG = LoggerFactory.getLogger(CodebookCache.class);

  private final CacheManager cacheManager;
  private final Class<V> valueClass;


  private static CacheManager getCacheManager() {
    LOG.debug("getCacheManager()");
    try {
      final InitialContext ctx = new InitialContext();
      final CacheManager cm = (CacheManager) ctx.lookup("payara/CacheManager");
      if (cm == null) {
        throw new NamingException("No 'payara/CacheManager' found in initial context, lookup returned null!.");
      }
      return cm;
    } catch (final NamingException | RuntimeException e) {
      LOG.error("Could not find 'payara/CacheManager' via the JNDI, caching is disabled and codebook"
          + " will be reloaded on each access until JNDI would be fixed.", e);
      return null;
    }
  }


  /**
   * Initializes the cache manager.
   *
   * @param valueClass
   */
  protected CodebookCache(final Class<V> valueClass) {
    this.cacheManager = getCacheManager();
    this.valueClass = valueClass;
  }


  /**
   * @return the type of cached values.
   */
  protected final Class<V> getValueClass() {
    return this.valueClass;
  }


  /**
   * @return a function that returns a key for the cached value.
   */
  protected abstract Function<V, K> getKeyFunction();

  /**
   * Returns cached entity by it's identifier.
   *
   * @param key
   * @return cached instance or null (if key is null or entity not found).
   */
  public V get(final K key) {
    LOG.debug("get(key={})", key);
    if (key == null) {
      return null;
    }
    final Map<K, V> cache = getCachedCodebook();
    return cache.get(key);
  }

  /**
   * Convert the cachedType to the codebook id.
   *
   * @param cachedType
   * @return the codebook Id
   */
  protected String toCodebookId(final Class<V> cachedType) {
    return cachedType.getCanonicalName();
  }


  private Map<K, V> getCachedCodebook() {
    LOG.trace("getCachedCodebook()");
    final Cache<String, Map<?, ?>> jcache = loadJCache();
    if (jcache == null) {
      LOG.warn("Cannot access JCache, but we can still continue ...");
    } else {
      final Map<K, V> codebook = loadCodebookFromJCache(jcache);
      if (codebook != null) {
        return codebook;
      }
    }

    final List<V> list = loadCodebookFromDatabase();
    final Map<K, V> map = list.stream().collect(Collectors.toMap(getKeyFunction(), Function.identity()));
    saveCodebookToJCache(jcache, map);
    return map;
  }


  private Cache<String, Map<?, ?>> loadJCache() {
    LOG.trace("loadJCache()");
    if (this.cacheManager == null) {
      return null;
    }
    try {
      return this.cacheManager.getCache(CodebookCacheInitializer.CACHE_FINAL_NAME);
    } catch (final RuntimeException e) {
      LOG.error(
          "Cannot use the cache '" + CodebookCacheInitializer.CACHE_FINAL_NAME + "'! But we can still continue.", e);
      return null;
    }
  }


  private Map<K, V> loadCodebookFromJCache(final Cache<String, Map<?, ?>> jcache) {
    final String codebookId = toCodebookId(getValueClass());
    LOG.debug("loadCodebookFromJCache(jcache={}); codebookId={}", jcache, codebookId);
    try {
      // we know what we put to the cache.
      final Map<K, V> codebook = (Map<K, V>) jcache.get(codebookId);
      if (codebook != null) {
        LOG.debug("Found cached codebook: '{}'", codebookId);
        return codebook;
      }
    } catch (final RuntimeException e) {
      LOG.error(
          "Cannot use the jcache '" + CodebookCacheInitializer.CACHE_FINAL_NAME + "' to access codebook '" + codebookId
              + "'! But we can still continue.", e);
    }
    return null;
  }


  private void saveCodebookToJCache(final Cache<String, Map<?, ?>> jcache, final Map<K, V> map) {
    final String codebookId = toCodebookId(getValueClass());
    LOG.debug("saveCodebookToJCache(jcache={}, map); codebookId={}", jcache, codebookId);
    if (jcache == null) {
      LOG.warn("JCache is null, cannot cache codebookId='{}'", codebookId);
      return;
    }
    try {
      jcache.put(codebookId, map);
    } catch (final RuntimeException e) {
      LOG.warn("Cannot put codebook '" + codebookId + "' to the JCache! But we can still continue.", e);
    }
  }


  /**
   * Loads all entities from the database.
   *
   * @return list of entities to cache.
   */
  protected List<V> loadCodebookFromDatabase() {
    LOG.debug("loadCodebookFromDatabase(); valueClass={}", getValueClass());
    return Collections.emptyList();
  }
}
