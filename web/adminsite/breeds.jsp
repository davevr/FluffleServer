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
<script src="../scripts/jquery-3.1.1.min.js" type="text/javascript"></script>
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
        if (newBreed != null) {
            $.ajax({
                type: "POST",
                url: "../api/v1/admin/update?type=breed&name=" + newBreed,
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                timeout: 3000,
                success: function (result, didIt, status) {
                    var curBreed = result;
                    var newHTML = '<tr data-breedid="' + curBreed.id + '">';
                    newHTML += '<td><a href="breeddetails.jsp?breedid=' + curBreed.id + '">' + curBreed.id + '></a></td>';
                    newHTML += '<td><input type="text" name="breedname" value="' + curBreed.BreedName + '"></td>';
                    newHTML += '<td><input type="text" name="breedrarity" value="' + curBreed.rarity + '"></td>';
                    newHTML += '<td><span>none</span></td>';
                    newHTML += '<td><span>0</span></td>';
                    newHTML += '<td><button onclick="handleclick(' + curBreed.id + ')">Submit</button></td>';
                    newHTML += '<td><button onclick="handledelete(' + curBreed.id + ')">Delete</button></td>';
                    newHTML += '</tr>';
                    $("tbody").append(newHTML);
                },
                error: function (theErr) {
                }
            });
        }
    }

    function handlerecompute() {
        $.ajax({
            type: "POST",
            url: "../api/v1/admin/update?type=remap",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            timeout: 3000,
            success: function (result, didIt, status) {
                console.log("remapped!");
            },
            error: function (theErr) {
                console.log("remap failed");
            }
        });
    }

    function handlerepair() {
        $.ajax({
            type: "POST",
            url: "../api/v1/admin/update?type=repair",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            timeout: 3000,
            success: function (result, didIt, status) {
                console.log("repaired!!");
            },
            error: function (theErr) {
                console.log("repair failed");
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
            <td>count</td>
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
                if (curBreed.possibleFurColors != null && curBreed.possibleFurColors.size() != 0)
                    curFur = curBreed.possibleFurColors.get(0);
                if (curFur != null && curFur.possibleEyeColors != null && curFur.possibleEyeColors.size() != 0)
                    curEye = curFur.possibleEyeColors.get(0);

              if (curFur != null && curEye != null) {
            %>
            <td><img width="64" height="64" src="../images/profiles/minilop_<%=curFur.ColorName.toLowerCase()%>_<%=curEye.ColorName.toLowerCase()%>.png"></td>
            <% } else  { %>
            <td><span>none</span></td>
            <% } %>
            <td><a href="bunnies.jsp?breedid=<%=curBreed.id.toString()%>"><%=ofy().load().type(BunnyObj.class).filter("BreedID =", curBreed.id).count()%></a></td>
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
        <button onclick="handlerecompute()">Recompute Color Model</button>
        <button onclick="handlerepair()">Repair Color Model</button>
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
