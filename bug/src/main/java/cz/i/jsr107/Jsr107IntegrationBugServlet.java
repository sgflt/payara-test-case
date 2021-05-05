package cz.i.jsr107;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Jsr107IntegrationBugServlet extends HttpServlet {


  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
    var oldProxy = new OldProxy();
    resp.getWriter().println(oldProxy.getService().getCachedValue());
  }
}
