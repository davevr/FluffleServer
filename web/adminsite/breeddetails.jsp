<%@ page import="com.eweware.fluffle.api.Authenticator" %>
<%@ page import="com.eweware.fluffle.obj.PlayerObj" %>
<%@ page import="com.eweware.fluffle.api.PlayerAPI" %>
<%@ page import="com.eweware.fluffle.obj.BunnyBreedObj" %>
<%@ page import="com.eweware.fluffle.obj.BunnyObj" %>
<%@ page import="static com.googlecode.objectify.ObjectifyService.ofy" %>
<%@ page import="com.googlecode.objectify.cmd.Query" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eweware.fluffle.api.BunnyBreedAPI" %>
<%@ page import="com.eweware.fluffle.obj.BunnyFurColorObj" %>
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
    String breedIdStr = request.getParameter("breedid");
    long breedId = -1;
    if (breedIdStr != null)
        breedId = Long.parseLong(breedIdStr);

    PlayerObj thePlayer = PlayerAPI.FetchById(Authenticator.CurrentUserId(session));
    if (thePlayer != null) {
        isAdmin = thePlayer.isAdmin == null ? false : thePlayer.isAdmin;
    }

    if (isAdmin && breedId != -1) {
        BunnyBreedObj theBreed = BunnyBreedAPI.FetchById(breedId);

%>
<html>
<head>
    <title>Fluffle Admin - <%= theBreed.BreedName%></title>
    <link rel="stylesheet" href="../css/admin.css" type="text/css" media="screen">
</head>

<body>
<script src="https://code.jquery.com/jquery-3.1.1.min.js" integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8=" crossorigin="anonymous"></script>
<script>

    function handleclick(theLine) {
        var tr = $("tr[data-furid='" + theLine + "']");
        tr.css("background-color","red");
        var furname = tr.find("input[name='furcolorname']");
        var rarity =  tr.find("input[name='furcolorrarity']");

        $.ajax({
            type: "PUT",
            url: "../api/v1/admin/update?type=furcolor&name=" + furname.val() + "&id=" + theLine + "&rarity=" + rarity.val(),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            timeout: 3000,
            success: function (result, didIt, status) {
                tr.css("background-color","white");
            },
            error: function (theErr) {
            }
        });
    }

    function handledelete(theLine) {
        var tr = $("tr[data-furid='" + theLine + "']");
        $.ajax({
            type: "DELETE",
            url: "../api/v1/admin/update?type=furcolor&id=" + theLine,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            timeout: 3000,
            success: function (result, didIt, status) {
                tr.remove();
            },
            error: function (theErr) {
            }
        });
    }

    function handlenewfurcolor(breedId) {

    }
</script>
<h1>OK here is where you can admin the <%= theBreed.BreedName %> breed.  </h1>


<h2>Here you can create change the randomness, add new furcolors, etc..</h2>
<div>Fur Colors for <%=theBreed.BreedName%></div>
<div>
    <table id="BunnyTable">
        <thead>
        <tr>
            <td>Id</td>
            <td>Name</td>
            <td>rarity</td>
            <td>image</td>
            <td>save</td>
            <td>delete</td>
        </tr>
        </thead>
        <tbody>

        <%
            for (BunnyFurColorObj curColor : theBreed.possibleFurColors) {

        %>
        <tr data-furid="<%=curColor.id%>">
            <td><a href="furdetails.jsp?furid=<%=curColor.id.toString()%>"><%= curColor.id.toString() %></a></td>
            <td><input type="text" name="furcolorname" value="<%= curColor.ColorName%>"></td>
            <td><input type="text" name="furcolorrarity" value="<%= curColor.rarity%>"></td>
            <td><img width="64" height="64" src="../images/profiles/minilop_<%=curColor.ColorName.toLowerCase()%>_<%=curColor.possibleEyeColors.get(0).ColorName.toLowerCase()%>.png"></td>
            <td><button onclick="handleclick(<%=curColor.id%>)">Submit</button></td>
            <td><button onclick="handledelete(<%=curColor.id%>)">Delete</button></td>
        </tr>
        <%
            }
        %>

        </tbody>
    </table>
    <div>
        <button onclick="handlenewfurcolor(<%=theBreed.id%>)">Add New Fur Color</button>
    </div>
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