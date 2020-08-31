/*
 * (c) ICZ a.s.
 * 31. 08. 2020
 */
package cz.i.test;

import org.glassfish.embeddable.BootstrapProperties;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.junit.Test;

/**
 * @author Lukáš Kvídera
 */
public class EmbeddedPayaraITest {

  @Test
  public void startEmbeddedPayara_doesStartWithTempInstanceRoot() throws GlassFishException {
    final BootstrapProperties bootProps = new BootstrapProperties();
    final GlassFishProperties props = new GlassFishProperties();
    props.setPort("http-listener", 8080);

    final GlassFishRuntime bootstrap = GlassFishRuntime.bootstrap(bootProps);
    final GlassFish glassFish = bootstrap.newGlassFish(props);

    glassFish.start();
    glassFish.stop();
    bootstrap.shutdown();
  }

  @Test
  public void startEmbeddedPayara_NPECausedByWrongDefaultPathToDomainXML() throws GlassFishException {
    final BootstrapProperties bootProps = new BootstrapProperties();
    bootProps.setInstallRoot("/tmp/custom-install-root");

    final GlassFishProperties props = new GlassFishProperties();
    props.setPort("http-listener", 8080);
    props.setInstanceRoot("/tmp/custom-instance-root");

    final GlassFishRuntime bootstrap = GlassFishRuntime.bootstrap(bootProps);
    final GlassFish glassFish = bootstrap.newGlassFish(props);

    glassFish.start();
    glassFish.stop();
    bootstrap.shutdown();
  }
}
