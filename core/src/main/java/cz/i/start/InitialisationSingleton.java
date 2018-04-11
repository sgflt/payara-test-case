package cz.i.start;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cache.CodebookCacheInitializer;

@Startup
@Singleton(name = "InitializationSingleton")
@TransactionManagement(TransactionManagementType.BEAN)
public class InitialisationSingleton {

  private static final Logger LOG = LoggerFactory.getLogger(InitialisationSingleton.class);


  @PostConstruct
  public void init() {
    // FIXME comment this line for disabling hazelcast.
    CodebookCacheInitializer.reinitialize();
  }


  @PreDestroy
  public void destroy() {
    LOG.debug("destroy()");
  }


  @Schedule(second = "0", minute = "*", hour = "*", persistent = false)
  public void reconfigure() {
    LOG.debug("reconfigure()");
  }
}
