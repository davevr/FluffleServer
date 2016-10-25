<%@ page import="com.eweware.fluffle.api.Authenticator" %>
<%@ page import="com.eweware.fluffle.obj.PlayerObj" %>
<%@ page import="com.eweware.fluffle.api.PlayerAPI" %>
<%@ page import="com.eweware.fluffle.obj.BunnyBreedObj" %>
<%@ page import="com.eweware.fluffle.obj.BunnyObj" %>
<%@ page import="static com.googlecode.objectify.ObjectifyService.ofy" %>
<%@ page import="com.googlecode.objectify.cmd.Query" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: Dave
  Date: 6/1/2016
  Time: 2:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Fluffle Admin - Breeds</title>
    <link rel="stylesheet" href="../css/admin.css" type="text/css" media="screen">
</head>
<body>
<h1>OK here is where you can admin the breeds of rabbits.  </h1>

<%
    Boolean isAdmin = false;

    PlayerObj thePlayer = PlayerAPI.FetchById(Authenticator.CurrentUserId(session));
    if (thePlayer != null) {
        isAdmin = thePlayer.isAdmin == null ? false : thePlayer.isAdmin;
    }

    if (isAdmin) {
%>
<h2>Here you can create new breeds, add fur color and eye colors, change randombess, etc.</h2>
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
            Query<BunnyBreedObj> query = ofy().load().type(BunnyBreedObj.class).limit(100);

            //query = query.startAt(Cursor.fromWebSafeString(cursorString));
            List<BunnyBreedObj> breedList = query.list();

            for(BunnyBreedObj curBreed : breedList) {

        %>
        <tr>
            <td><a href="breeddetails.jsp?breedid=<%=curBreed.id.toString()%>"><%= curBreed.id.toString() %></a></td>
            <td><%= curBreed.BreedName%></td>
            <td><%= curBreed.rarity %></td>

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
<h2 style="color:red;">Sorry, you are not an admin</h2>
<%
    }
%>



</body>
</html>
