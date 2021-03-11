/*
 * (c) ICZ a.s.
 * 11. 03. 2021
 */
package cz.i.quartz;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lukáš Kvídera
 */
public class JobUsingCache implements Job {
  private static final Logger LOG = LoggerFactory.getLogger(JobUsingCache.class);


  private static CacheManager getCacheManager() {
    try {
      final InitialContext ctx = new InitialContext();
      final CacheManager cm = (CacheManager) ctx.lookup("payara/CacheManager");
      if (cm == null) {
        throw new IllegalStateException("No 'payara/CacheManager' found in initial context.");
      }
      return cm;
    } catch (final NamingException e) {
      throw new IllegalStateException("Could not find 'payara/CacheManager' via the JNDI", e);
    }
  }


  private static Cache<String, String> getCache(final String name) {
    final var cacheManager = getCacheManager();
    final var cache = cacheManager.getCache(name, String.class, String.class);
    if (cache != null) {
      return cache;
    }
    final var jcacheConfig = new MutableConfiguration<String, String>();
    jcacheConfig.setTypes(String.class, String.class);
    jcacheConfig.setStatisticsEnabled(true).setManagementEnabled(true);
    jcacheConfig.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));
    jcacheConfig.setStoreByValue(false);
    return cacheManager.createCache(name, jcacheConfig);
  }


  @Override
  public void execute(final JobExecutionContext context) {
    LOG.info("execute(context={})", context);

    getCache("CACHE-TO-GET");
  }
}
