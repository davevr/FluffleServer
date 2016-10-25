<%@ page import="com.eweware.fluffle.api.Authenticator" %>
<%@ page import="com.eweware.fluffle.api.PlayerAPI" %>
<%@ page import="static com.googlecode.objectify.ObjectifyService.ofy" %>
<%@ page import="com.googlecode.objectify.cmd.Query" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eweware.fluffle.api.BunnyBreedAPI" %>
<%@ page import="com.eweware.fluffle.api.BunnyFurColorAPI" %>
<%@ page import="com.eweware.fluffle.obj.*" %>
<%--
  Created by IntelliJ IDEA.
  User: Dave
  Date: 10/24/2016
  Time: 8:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Boolean isAdmin = false;
    String furIdStr = request.getParameter("furid");
    long furId = -1;
    if (furIdStr != null)
        furId = Long.parseLong(furIdStr);

    PlayerObj thePlayer = PlayerAPI.FetchById(Authenticator.CurrentUserId(session));
    if (thePlayer != null) {
        isAdmin = thePlayer.isAdmin == null ? false : thePlayer.isAdmin;
    }

    if (isAdmin && furId != -1) {
        BunnyFurColorObj theFur = BunnyFurColorAPI.FetchById(furId);

%>
<html>
<head>
    <title>Fluffle Admin - <%= theFur.ColorName%></title>
    <link rel="stylesheet" href="../css/admin.css" type="text/css" media="screen">
</head>
<body>
<h1>OK here is where you can admin the <%= theFur.ColorName %> breed.  </h1>


<h2>Here you can create change the randomness, add new eye colors, etc..</h2>
<div>Eye Colors for fur color <%=theFur.ColorName%></div>
<div>
    <table id="BunnyTable">
        <thead>
        <tr>
            <td>Id</td>
            <td>Name</td>
            <td>rarity</td>
        </tr>
        </thead>
        <tbody>

        <%
            for (BunnyEyeColorObj curColor : theFur.possibleEyeColors) {

        %>
        <tr>
            <td><a href="eyedetails.jsp?eyecolorid=<%=curColor.id.toString()%>"><%= curColor.id.toString() %></a></td>
            <td><%= curColor.ColorName%></td>
            <td><%= curColor.rarity %></td>

        </tr>
        <%
            }
        %>
        </tbody>
    </table>
</div>
<%
} else {
%>
<html>
<head>
    <title>Fluffle Admin - Breeds</title>
    <link rel="stylesheet" href="../css/admin.css" type="text/css" media="screen">
</head>
<body>
<h2 style="color:red;">Sorry, you are not an admin</h2>
</body>
    <%
    }
%>



</body>
</html>