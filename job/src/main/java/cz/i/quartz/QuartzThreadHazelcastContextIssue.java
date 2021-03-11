/*
 * (c) ICZ a.s.
 * 11. 03. 2021
 */
package cz.i.quartz;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lukáš Kvídera
 */
class QuartzThreadHazelcastContextIssue {
  private static final Logger LOG = LoggerFactory.getLogger(QuartzThreadHazelcastContextIssue.class);
  private Scheduler scheduler;
  private volatile boolean started;


  synchronized void start() {
    if (this.started) {
      return;
    }

    try {
      final var schedulerFactory = new StdSchedulerFactory("quartz.properties");
      this.scheduler = schedulerFactory.getScheduler();
      this.scheduler.start();
      this.scheduler.scheduleJob(
          new JobDetail("job-with-cache", JobUsingCache.class),
          new SimpleTrigger("trigger", 10, 10000)
      );
      this.started = true;
    } catch (final SchedulerException e) {
      LOG.error("quartz initialization failed", e);
    }
    LOG.info("The Quartz scheduler started.");
  }


  synchronized void stop() {
    try {
      this.scheduler.shutdown();
    } catch (final SchedulerException e) {
      LOG.error("quartz shutdown failed", e);
    }
    LOG.info("The Quartz scheduler stopped.");
  }

}
