<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import = "com.login.demo.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Ok</title>
</head>
<body>

<h1>Login eseguito</h1>

<% User user= (User) session.getAttribute("user");
	User user1=(User) request.getAttribute("user");
%>

<h3>
Ciao, tu sei l'utente <%=user.getUsername()%>.	<!-- o out.println -->
<br /> Il tuo numero telefonico è <%=user1.getTelefono() %>
</h3>

</body>
</html>