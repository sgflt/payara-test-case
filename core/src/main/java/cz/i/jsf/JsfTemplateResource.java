package cz.i.jsf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used for serving shared xhtml templates for JSF pages.
 *
 * @author Lukáš Kvídera
 */
public class JsfTemplateResource extends Resource {

  private static final Logger LOG = LoggerFactory.getLogger(JsfTemplateResource.class);


  public JsfTemplateResource(final String resourceName) {
    setLibraryName("templates");
    setResourceName(resourceName);
  }


  @Override
  public InputStream getInputStream() throws IOException {
    LOG.trace("getInputStream()");
    throw new UnsupportedOperationException("xhtml page is not supposed to be sent as stream");
  }


  @Override
  public Map<String, String> getResponseHeaders() {
    LOG.trace("getResponseHeaders()");
    throw new UnsupportedOperationException("xhtml template does not care about headers");
  }


  @Override
  public String getRequestPath() {
    LOG.trace("getRequestPath()");
    final URL url =
        Thread.currentThread().getContextClassLoader()
            .getResource("META-INF/resources/" + getLibraryName() + "/" + getResourceName());
    return url.toString();
  }


  @Override
  public URL getURL() {
    LOG.trace("getURL()");
    throw new UnsupportedOperationException("xhtml templates does not need an URL");
  }


  @Override
  public boolean userAgentNeedsUpdate(final FacesContext context) {
    LOG.trace("userAgentNeedsUpdate(context)");
    return false;
  }

}
