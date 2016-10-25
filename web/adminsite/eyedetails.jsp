<%@ page import="com.eweware.fluffle.api.Authenticator" %>
<%@ page import="com.eweware.fluffle.api.PlayerAPI" %>
<%@ page import="static com.googlecode.objectify.ObjectifyService.ofy" %>
<%@ page import="com.googlecode.objectify.cmd.Query" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eweware.fluffle.api.BunnyBreedAPI" %>
<%@ page import="com.eweware.fluffle.api.BunnyEyeColorAPI" %>
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
    String eyeColorIdStr = request.getParameter("eyecolorid");
    long eyeColorId = -1;
    if (eyeColorIdStr != null)
        eyeColorId = Long.parseLong(eyeColorIdStr);

    PlayerObj thePlayer = PlayerAPI.FetchById(Authenticator.CurrentUserId(session));
    if (thePlayer != null) {
        isAdmin = thePlayer.isAdmin == null ? false : thePlayer.isAdmin;
    }

    if (isAdmin && eyeColorId != -1) {
        BunnyEyeColorObj theEyeColor = BunnyEyeColorAPI.FetchById(eyeColorId);

%>
<html>
<head>
    <title>Fluffle Admin - <%= theEyeColor.ColorName%></title>
    <link rel="stylesheet" href="../css/admin.css" type="text/css" media="screen">
</head>
<body>
<h1>OK here is where you can admin the <%= theEyeColor.ColorName %> breed.  </h1>


<h2>Here you can create change the randomness, add new furcolors, etc..</h2>
<div>Fur Colors for eye colior <%=theEyeColor.ColorName%></div>
<div>
    we need to put something here I guess...
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