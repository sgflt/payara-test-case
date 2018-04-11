package cz.i.core.http;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cache.ParameterCache;

public class CustomFilter implements Filter {

  private static final Logger LOG = LoggerFactory.getLogger(CustomFilter.class);


  @Override
  public void init(final FilterConfig config) throws ServletException {
    LOG.debug("init(config={})", config);
  }


  @Override
  public void destroy() {
    LOG.warn("Destroying CustomFilter instance: {}", this);
  }


  @Override
  public void doFilter(final ServletRequest httpRequest, final ServletResponse response, final FilterChain chain)
      throws IOException, ServletException {
    LOG.debug("doFilter(request, response, chain)");
    final HttpServletResponse httpResponse = (HttpServletResponse) response;
    processRequest(((HttpServletRequest) httpRequest), httpResponse, chain);
  }


  protected void processRequest(final HttpServletRequest hr, final HttpServletResponse response,
      final FilterChain chain)
      throws IOException, ServletException {
    LOG.trace("processRequest(hr, response, chain)");

    final HttpSession session = hr.getSession(false);
    LOG.debug("session: {}", session);

    // use HTTP redirect to sync url context in browser and server.
    if (hr.getUserPrincipal() == null) {
      response.sendRedirect("/login/login");
      return;
    }

    // FIXME this line is possibel cause of ClassNotFoundError /config/index.jsp
    if (new ParameterCache().isEnabled("XXX")) {
      return;
    }

    chain.doFilter(hr, response);
  }


  public static void redirect(final String targetUri, final HttpServletRequest hr, final ServletResponse response)
      throws ServletException, IOException {
    LOG.info("redirect(targetUri='{}', hr, response)", targetUri);
    final RequestDispatcher dispatcher = hr.getRequestDispatcher(targetUri);
    if (dispatcher == null) {
      throw new ServletException("Cannot resolve dispatcher for the targetUri: " + targetUri);
    }
    dispatcher.forward(hr, response);
  }


  public static void redirect(final ServletContext targetContext, final String targetUri, final HttpServletRequest hr,
      final ServletResponse response)
      throws ServletException, IOException {
    LOG.info("redirect(targetContext, targetUri='{}', hr, response)", targetUri);
    Objects.requireNonNull(targetContext, "target context must not be null!");
    final RequestDispatcher dispatcher = targetContext.getRequestDispatcher(targetUri);
    if (dispatcher == null) {
      throw new ServletException("Cannot resolve dispatcher for the targetUri: " + targetUri);
    }
    dispatcher.forward(hr, response);
  }
}
