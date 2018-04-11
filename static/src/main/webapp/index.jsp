<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8" session="false" %>
<%@ page import="cz.i.core.http.SessionManager" %>
<%=SessionManager.getApplicationStatus().getVersion()%>
<a href="/bug/index.jsp">Click here</a>
