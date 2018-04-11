package cz.i.core.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cache.ParameterCache;

public class SessionManager {

  private static final Logger LOG = LoggerFactory.getLogger(SessionManager.class);

  private static volatile ApplicationStatus applicationStatus;


  public static ApplicationStatus getApplicationStatus() {
    if (isApplicationStatusOld()) {
      reloadApplicationStatus();
    }
    return applicationStatus;
  }


  private static boolean isApplicationStatusOld() {
    return true;
  }


  private static synchronized void reloadApplicationStatus() {
    LOG.debug("reloadApplicationStatus()");
    if (!isApplicationStatusOld()) {
      LOG.trace("Application status already reloaded by another thread, returning.");
      return;
    }
    final ParameterCache cache = new ParameterCache();
    cache.getStringValue("XXX");
    final ApplicationStatus status = new ApplicationStatus();
    applicationStatus = status;
  }
}
