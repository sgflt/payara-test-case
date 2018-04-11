package cz.i.cache;

import java.util.Map;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodebookCacheInitializer {

  private static final Logger LOG = LoggerFactory.getLogger(CodebookCacheInitializer.class);
  static final String CACHE_FINAL_NAME = "codebooks";


  /**
   * Clears all codebook caches or creates a new cache. Method is synchronized.
   * Runtime exceptions are only logged.
   */
  public static synchronized void reinitialize() {
    LOG.debug("reinitialize()");
    try {
      final CacheManager manager = getCacheManagerInternal();
      if (isInitialized()) {
        final Cache<String, Map<?, ?>> cache = manager.getCache(CodebookCacheInitializer.CACHE_FINAL_NAME);
        cache.clear();
        LOG.info("All codebook caches reset.");
      } else {
        createCache(manager);
      }
      LOG.info("Codebook caches initialized.");
    } catch (final RuntimeException e) {
      LOG.error("Cannot initialize CodebookCache!", e);
    }
  }


  /**
   * @return true if cache is accessible via the JNDI.
   */
  public static boolean isInitialized() {
    try {
      final CacheManager manager = getCacheManagerInternal();
      return manager != null && manager.getCache(CACHE_FINAL_NAME) != null;
    } catch (final IllegalStateException e) {
      return false;
    }
  }


  private static CacheManager getCacheManagerInternal() {
    LOG.debug("getCacheManagerInternal()");
    try {
      final InitialContext ctx = new InitialContext();
      final CacheManager cm = (CacheManager) ctx.lookup("payara/CacheManager");
      if (cm == null) {
        throw new IllegalStateException("No 'payara/CacheManager' found in initial context, ctx returned null!.");
      }
      return cm;
    } catch (final NamingException e) {
      throw new IllegalStateException("Could not find 'payara/CacheManager' via the JNDI", e);
    }
  }


  private static Cache<String, Map<?, ?>> createCache(final CacheManager cacheManager) {
    LOG.debug("createCache(cacheManager={})", cacheManager);
    final MutableConfiguration<String, Map<?, ?>> jcacheConfig = new MutableConfiguration<>();
    // jcacheConfig.setTypes(String.class, Map.class);
    jcacheConfig.setStatisticsEnabled(true).setManagementEnabled(true);
    jcacheConfig.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.TEN_MINUTES));
    jcacheConfig.setStoreByValue(false);
    final Cache<String, Map<?, ?>> cache = cacheManager.createCache(CACHE_FINAL_NAME, jcacheConfig);
    return cache;
  }
}
