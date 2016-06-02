<%@ page import="com.eweware.fluffle.api.Authenticator" %>
<%@ page import="com.eweware.fluffle.obj.PlayerObj" %>
<%@ page import="com.eweware.fluffle.api.PlayerAPI" %><%--
  Created by IntelliJ IDEA.
  User: Dave
  Date: 6/1/2016
  Time: 2:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Fluffle Admin Page</title>
</head>
<body>
<h1>OK here is where you can admin fluffle.  </h1>

<%

    Boolean signedIn = Authenticator.UserIsLoggedIn(session);
    Boolean isAdmin = false;

    if (!signedIn) {
        String username = request.getParameter("username");
        String pwd = request.getParameter("pwd");

        if (username != null && pwd != null) {
            PlayerObj thePlayer = Authenticator.AuthenticateUser(session, username, pwd);

            if (thePlayer != null) {
                isAdmin = thePlayer.isAdmin;
            } else {
%>
                <div style="background-color:red; color:white">Incorrect Username or Password</div>
<%
            }
        }
    } else {
                PlayerObj thePlayer = PlayerAPI.FetchById(Authenticator.CurrentUserId(session));
                if (thePlayer != null) {
                    isAdmin = thePlayer.isAdmin == null ? false : thePlayer.isAdmin;
                }
    }

    if (isAdmin) {
%>
<h2>Here are your admin tools!</h2>
<div>
    <button onclick="window.location='/admin/game'">Edit Game Settings</button>
    <button onclick="window.location='/admin/players'">Edit Players</button>
    <button onclick="window.location='/admin/breeds'">Edit Bunny Breeds</button>
    <button onclick="window.location='/admin/bunnies'">Edit Bunnies</button>
</div>
<%
    } else if (signedIn) {
%>
<h2 style="color:red;">Sorry, you are not an admin</h2>
<%
    } else {
        %>
<h2>Sign in to start</h2>
<form action="/admin" >

    Username: <input type="text" title="username" name="username"><br/>
    Password: <input type="password" title="password" name="pwd"><br/>
    <input type="submit" value="Submit"><br/>
</form>
<%
    }

    %>



</body>
</html>
