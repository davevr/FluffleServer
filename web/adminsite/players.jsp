<%@ page import="com.eweware.fluffle.api.Authenticator" %>
<%@ page import="com.eweware.fluffle.obj.PlayerObj" %>
<%@ page import="com.eweware.fluffle.api.PlayerAPI" %>
<%@ page import="com.googlecode.objectify.cmd.Query" %>
<%@ page import="java.util.List" %>
<%@ page import="static com.googlecode.objectify.ObjectifyService.ofy" %>

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
    <title>Fluffle Admin - Players</title>
</head>
<body>
<h1>OK here is where you can admin fluffle.  </h1>

<%
    Boolean isAdmin = false;

    PlayerObj thePlayer = PlayerAPI.FetchById(Authenticator.CurrentUserId(session));
    if (thePlayer != null) {
        isAdmin = thePlayer.isAdmin == null ? false : thePlayer.isAdmin;
    }

    if (isAdmin) {
%>
<h2>Here you can give players carrots or make them admins, etc.</h2>
<div>
    <table id="BunnyTable">
        <thead>
        <tr>
            <td>Id</td>
            <td>Nickname</td>
            <td>Username</td>
            <td>carrotCount</td>
            <td>lifetime Bunnies</td>
            <td>created</td>
            <td>Last Active</td>
            <td>admin</td>
            <td>breeder</td>
        </tr>
        </thead>
        <tbody>

        <%
            Query<PlayerObj> query = ofy().load().type(PlayerObj.class).limit(100);

            //query = query.startAt(Cursor.fromWebSafeString(cursorString));
            List<PlayerObj> playerList = query.list();

            for(PlayerObj curPlayer : playerList) {

        %>
        <tr>
            <td><%= curPlayer.id.toString() %></td>
            <td><%= curPlayer.nickname%></td>
            <td><%= curPlayer.username %></td>
            <td><%= curPlayer.carrotCount %></td>
            <td><%= curPlayer.totalBunnies %></td>
            <td><%= curPlayer.creationDate != null? curPlayer.creationDate.toString() : "--" %></td>
            <td><%= curPlayer.lastActiveDate!= null? curPlayer.lastActiveDate.toString() : "--" %></td>
            <td><%= curPlayer.isAdmin == null? false : curPlayer.isAdmin %></td>
            <td><%= curPlayer.isBreeder == null? false : curPlayer.isBreeder %></td>
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
