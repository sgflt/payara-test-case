package cz.i;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.core.http.CustomFilter;

/**
 * Servlet serving as an entry to login page.
 */
public class LoginServlet extends HttpServlet {

  private static final long serialVersionUID = -573558376396826296L;
  private static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);


  @Override
  protected void service(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException {
    LOG.debug("service(request, response)");

    if (request.getUserPrincipal() == null) {
      CustomFilter.redirect("/login.jsp", request, response);
      return;
    }

    // make sure the session is created right now!
    request.getSession();
    CustomFilter.redirect(getServletContext().getContext("/"), "/index.jsp", request, response);
  }
}
