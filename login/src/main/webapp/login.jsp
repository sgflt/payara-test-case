<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html; charset=utf-8" session="false" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
  <form method="POST" action="j_security_check">
    <input class="input-ram uppercase" type="text" name="j_username" id="j_username"/>
    <input class="input-ram" type="password" name="j_password" id="j_password"/>
    <input value="Přihlásit" type="submit" />
  </form>
</body>
</html>
