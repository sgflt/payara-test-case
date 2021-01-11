package cz.i.start;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cache.CodebookCacheInitializer;

@Singleton
public class InitialisationSingleton {

  private static final Logger LOG = LoggerFactory.getLogger(InitialisationSingleton.class);


  @PostConstruct
  public void init(@Observes @Initialized(ApplicationScoped.class) final Object event) {
    // FIXME comment this line for disabling hazelcast.
    CodebookCacheInitializer.reinitialize();
  }


  @PreDestroy
  public void destroy() {
    LOG.debug("destroy()");
  }

}
