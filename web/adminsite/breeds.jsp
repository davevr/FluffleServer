<%@ page import="com.eweware.fluffle.api.Authenticator" %>
<%@ page import="com.eweware.fluffle.api.PlayerAPI" %>
<%@ page import="static com.googlecode.objectify.ObjectifyService.ofy" %>
<%@ page import="com.googlecode.objectify.cmd.Query" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eweware.fluffle.obj.*" %>
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
<!--
<script src="https://code.jquery.com/jquery-3.1.1.min.js" integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8=" crossorigin="anonymous"></script>
-->
<script src="../scripts/jquery-2.1.0.min.js" type="text/javascript"></script>
<script>

    function handleclick(theLine) {
        var tr = $("tr[data-breedid='" + theLine + "']");
        tr.css("background-color","red");
        var breedname = tr.find("input[name='breedname']");
        var rarity =  tr.find("input[name='breedrarity']");

        $.ajax({
            type: "PUT",
            url: "../api/v1/admin/update?type=breed&name=" + breedname.val() + "&id=" + theLine + "&rarity=" + rarity.val(),
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
        var tr = $("tr[data-breedid='" + theLine + "']");
        $.ajax({
            type: "DELETE",
            url: "../api/v1/admin/update?type=breed&id=" + theLine,
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

    function handlenewbreed() {
        var newBreed = window.prompt("Name of new breed");
        $.ajax({
            type: "POST",
            url: "../api/v1/admin/update?type=breed&name=" + newBreed,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            timeout: 3000,
            success: function (result, didIt, status) {
                console.log(result.toString());

            },
            error: function (theErr) {
            }
        });
    }
</script>

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
            <td>image</td>
            <td>save</td>
            <td>delete</td>
        </tr>
        </thead>
        <tbody>

        <%
            Query<BunnyBreedObj> query = ofy().load().type(BunnyBreedObj.class).limit(100);

            List<BunnyBreedObj> breedList = query.list();

            for(BunnyBreedObj curBreed : breedList) {

        %>
        <tr data-breedid="<%=curBreed.id%>">
            <td><a href="breeddetails.jsp?breedid=<%=curBreed.id.toString()%>"><%= curBreed.id.toString() %></a></td>
            <td><input type="text" name="breedname" value="<%= curBreed.BreedName%>"></td>
            <td><input type="text" name="breedrarity" value="<%= curBreed.rarity%>"></td>
            <%
                BunnyFurColorObj curFur = null;
                BunnyEyeColorObj curEye = null;
                if (curBreed.possibleFurColors != null)
                    curFur = curBreed.possibleFurColors.get(0);
                if (curFur != null && curFur.possibleEyeColors != null)
                    curEye = curFur.possibleEyeColors.get(0);

              if (curFur != null && curEye != null) {
            %>
            <td><img width="64" height="64" src="../images/profiles/minilop_<%=curFur.ColorName.toLowerCase()%>_<%=curEye.ColorName.toLowerCase()%>.png"></td>
            <% } else  { %>
            <td><span>none</span></td>
            <% } %>
            <td><button onclick="handleclick(<%=curBreed.id%>)">Submit</button></td>
            <td><button onclick="handledelete(<%=curBreed.id%>)">Delete</button></td>


        </tr>
        <%
            }
        %>
        </tbody>
    </table>
    <div>
        <button onclick="handlenewbreed()">Add New Breed</button>
    </div>
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
