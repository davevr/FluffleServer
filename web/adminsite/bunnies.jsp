<%@ page import="com.eweware.fluffle.api.Authenticator" %>
<%@ page import="com.eweware.fluffle.obj.PlayerObj" %>
<%@ page import="com.eweware.fluffle.api.PlayerAPI" %>
<%@ page import="com.eweware.fluffle.obj.BunnyObj" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eweware.fluffle.api.BunnyAPI" %>
<%@ page import="com.googlecode.objectify.cmd.Query" %>
<%@ page import="static com.googlecode.objectify.ObjectifyService.ofy" %>
<%@ page import="com.google.appengine.api.datastore.Cursor" %><%--
  Created by IntelliJ IDEA.
  User: Dave
  Date: 6/1/2016
  Time: 2:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Fluffle Admin - Bunnies</title>
    <link rel="stylesheet" href="../css/admin.css" type="text/css" media="screen">
</head>
<body>
<h1>OK here is where you can admin bunnies.  </h1>

<%
    Boolean isAdmin = false;

    PlayerObj thePlayer = PlayerAPI.FetchById(Authenticator.CurrentUserId(session));
    if (thePlayer != null) {
        isAdmin = thePlayer.isAdmin == null ? false : thePlayer.isAdmin;
    }

    if (isAdmin) {
%>
<h2>Here you can create bunnies, give them to players, see ownership and history, etc.</h2>
<div>
    <table id="BunnyTable">
        <thead>
            <tr>
                <td>Id</td>
                <td>Name</td>
                <td>Owner</td>
                <td>Size</td>
                <td>Progress</td>
                <td>Gender</td>
                <td>Breed</td>
                <td>Fur Color</td>
                <td>Eye Color</td>
            </tr>
        </thead>
        <tbody>

            <%
                Query<BunnyObj> query = ofy().load().type(BunnyObj.class).limit(100);

                //query = query.startAt(Cursor.fromWebSafeString(cursorString));
                List<BunnyObj> bunList = query.list();

                for(BunnyObj curBuns : bunList) {

            %>
            <tr>
            <td><%= curBuns.id.toString() %></td>
            <td><%= curBuns.BunnyName%></td>
            <td><%= curBuns.CurrentOwner.toString() %></td>
            <td><%= curBuns.BunnySize %></td>
            <td><%= curBuns.FeedState %></td>
            <td><%= curBuns.Female ? "female" : "male" %></td>
            <td><%= curBuns.BreedName %></td>
            <td><%= curBuns.FurColorName %></td>
            <td><%= curBuns.EyeColorName %></td>
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
