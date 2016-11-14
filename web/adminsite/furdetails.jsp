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

<script src="../scripts/jquery-3.1.1.min.js" type="text/javascript"></script>
<script>

    function handleclick(theLine) {
        var tr = $("tr[data-eyeid='" + theLine + "']");
        tr.css("background-color","red");
        var eyename = tr.find("input[name='eyecolorname']");
        var rarity =  tr.find("input[name='eyecolorrarity']");

        $.ajax({
            type: "PUT",
            url: "../api/v1/admin/update?type=eyecolor&name=" + eyename.val() + "&id=" + theLine + "&rarity=" + rarity.val(),
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
        var tr = $("tr[data-eyeid='" + theLine + "']");
        $.ajax({
            type: "DELETE",
            url: "../api/v1/admin/update?type=eyecolor&id=" + theLine + "&furcolorid=<%=furIdStr%>",
            dataType: "json",
            timeout: 3000,
            success: function (result, didIt, status) {
                tr.remove();
            },
            error: function (theErr) {
            }
        });

    }

    function handleneweyecolor() {
        var newColor = window.prompt("Name of new eye color");
        if (newColor != null) {
            $.ajax({
                type: "POST",
                url: "../api/v1/admin/update?type=eyecolor&name=" + newColor + "&furcolorid=<%=furIdStr%>",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                timeout: 3000,
                success: function (result, didIt, status) {
                    var curColor = result;
                    let newHTML = '<tr data-eyeid="' + curColor.id + '">';
                    newHTML += '<td><a href="eyedetails.jsp?eyecolorid=' + curColor.id + '">' + curColor.id + '</a></td>';
                    newHTML += '<td><input type="text" name="eyecolorname" value="' + curColor.ColorName + '"></td>';
                    newHTML += '<td><input type="text" name="eyecolorrarity" value="' + curColor.rarity + '"></td>';
                    newHTML += '<td><img width="64" height="64" src="../images/profiles/minilop_<%=theFur.ColorName.toLowerCase()%>_' + curColor.ColorName.toLowerCase() + '.png"></td>';
                    newHTML += '<td><span>0</span></td>';
                    newHTML += '<td><button onclick="handleclick(' + curColor.id + ')">Submit</button></td>';
                    newHTML += '<td><button onclick="handledelete(' + curColor.id + ')">Delete</button></td>';
                    newHTML += '<td><button onclick="handleaddtostore(' + curColor.id + ')">add</button></td>';
                    newHTML += '</tr>';

                    $("tbody").append(newHTML);
                },
                error: function (theErr) {
                }
            });
        }
    }

    function handleaddtostore(theLine) {
        $.ajax({
            type: "POST",
            url: "../api/v1/admin/update?type=store&eyecolorid=" + theLine + "&furcolorid=<%=furIdStr%>",
            dataType: "json",
            timeout: 3000,
            success: function (result, didIt, status) {
                tr.remove();
            },
            error: function (theErr) {
            }
        });

    }
</script>

<h1>OK here is where you can admin the <%= theFur.ColorName %> color.  </h1>


<h2>Here you can create change the randomness, add new eye colors, etc..</h2>
<div>Eye Colors for fur color <%=theFur.ColorName%></div>
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
            <td>add to store</td>
        </tr>
        </thead>
        <tbody>

        <%
            if (theFur.possibleEyeColors != null) {
                for (BunnyEyeColorObj curColor : theFur.possibleEyeColors) {

        %>
        <tr data-eyeid="<%=curColor.id%>">
            <td><a href="eyedetails.jsp?eyecolorid=<%=curColor.id.toString()%>"><%= curColor.id.toString() %></a></td>
            <td><input type="text" name="eyecolorname" value="<%= curColor.ColorName%>"></td>
            <td><input type="text" name="eyecolorrarity" value="<%= curColor.rarity%>"></td>
            <%
                if (theFur.ColorName != null && curColor.ColorName != null) {
            %>
            <td><img width="64" height="64" src="../images/profiles/minilop_<%=theFur.ColorName.toLowerCase()%>_<%=curColor.ColorName.toLowerCase()%>.png"></td>
            <% } else  { %>
            <td><span>none</span></td>
            <% } %>
            <td><%=ofy().load().type(BunnyObj.class).filter("EyeColorID =", curColor.id).count()%></td>
            <td><button onclick="handleclick(<%=curColor.id%>)">Submit</button></td>
            <td><button onclick="handledelete(<%=curColor.id%>)">Delete</button></td>
            <td><button onclick="handleaddtostore(<%=curColor.id%>)">Add</button></td>
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
    <div>
        <button onclick="handleneweyecolor()">Add New Eye Color</button>
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