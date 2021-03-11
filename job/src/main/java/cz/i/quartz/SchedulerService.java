package cz.i.quartz;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Startup
@Singleton(name = "SchedulerSingleton")
@TransactionManagement(TransactionManagementType.BEAN)
public class SchedulerService {

  private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);

  private final QuartzThreadHazelcastContextIssue quartz = new QuartzThreadHazelcastContextIssue();


  @PostConstruct
  public void start() {
    LOG.info("Starting scheduler");
    this.quartz.start();
    LOG.info("scheduler started");
  }


  @PreDestroy
  public void destroy() {
    LOG.info("Stopping the scheduler.");
    this.quartz.stop();
    LOG.info("scheduler stopped");
  }
}
