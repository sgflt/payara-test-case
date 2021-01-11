/*
 * (c) ICZ a.s.
 * 11. 01. 2021
 */
package cz.i.cis.cache;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

/**
 * @author Lukáš Kvídera
 */
@RunWith(Arquillian.class)
abstract class ArquillianITest {

  @Deployment
  public static EnterpriseArchive getEar() {
    final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "application.ear");
    final JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class, "tested.jar");
    final JavaArchive tests = ShrinkWrap.create(JavaArchive.class, "tests.jar");

    return ear;
  }
}
