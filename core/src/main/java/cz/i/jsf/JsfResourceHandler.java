package cz.i.jsf;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class serves shared resources as CSS, JS or xhtml templates from core.
 *
 * Usualy you don't need to use this class directly, JSF framework does it instead of you.
 *
 * You may find some configuration in faces-config.xml
 *
 * @author Lukáš Kvídera
 */
public class JsfResourceHandler extends ResourceHandlerWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(JsfResourceHandler.class);
  private final ResourceHandler wrapped;


  public JsfResourceHandler(final ResourceHandler wrapped) {
    LOG.trace("JsfResourceReslover(wrapped={})", wrapped);
    this.wrapped = wrapped;
  }


  @Override
  public ResourceHandler getWrapped() {
    LOG.trace("getWrapped()");
    return this.wrapped;
  }


  @Override
  public Resource createResource(final String resourceName, final String libraryName) {
    LOG.trace("createResource(resourceName={}, libraryName={})", resourceName, libraryName);
    if ("templates".equals(libraryName)) {
      return new JsfTemplateResource(resourceName);
    }

    return super.createResource(resourceName, libraryName);
  }


  @Override
  public boolean libraryExists(final String libraryName) {
    LOG.trace("libraryExists(libraryName={})", libraryName);
    if ("templates".equals(libraryName)) {
      return true;
    }

    return super.libraryExists(libraryName);
  }

}
