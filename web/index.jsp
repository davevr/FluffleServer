<%--
  Created by IntelliJ IDEA.
  User: davidvronay
  Date: 5/9/16
  Time: 9:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Fluffle:  A collection of Bunnies</title>
    <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-57x57.png">
    <link rel="apple-touch-icon" sizes="60x60" href="/apple-touch-icon-60x60.png">
    <link rel="apple-touch-icon" sizes="72x72" href="/apple-touch-icon-72x72.png">
    <link rel="apple-touch-icon" sizes="76x76" href="/apple-touch-icon-76x76.png">
    <link rel="apple-touch-icon" sizes="114x114" href="/apple-touch-icon-114x114.png">
    <link rel="apple-touch-icon" sizes="120x120" href="/apple-touch-icon-120x120.png">
    <link rel="apple-touch-icon" sizes="144x144" href="/apple-touch-icon-144x144.png">
    <link rel="apple-touch-icon" sizes="152x152" href="/apple-touch-icon-152x152.png">
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon-180x180.png">
    <link rel="icon" type="image/png" href="/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="/android-chrome-192x192.png" sizes="192x192">
    <link rel="icon" type="image/png" href="/favicon-96x96.png" sizes="96x96">
    <link rel="icon" type="image/png" href="/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="/manifest.json">
    <link rel="mask-icon" href="/safari-pinned-tab.svg" color="#5bbad5">
    <meta name="msapplication-TileColor" content="#da532c">
    <meta name="msapplication-TileImage" content="/mstile-144x144.png">
    <meta name="theme-color" content="#ffffff">
    <%@include file="scripts/facebookpixel.html"%>
  </head>
  <div id="fb-root"></div>
  <script>(function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.6&appId=1171087469573904";
    fjs.parentNode.insertBefore(js, fjs);
  }(document, 'script', 'facebook-jssdk'));</script>
  <body>
  <h1 style="text-align: center;">Fluffle!</h1>
  <p style="text-align: center;"><em>a collection of bunnies</em></p>
  <p><img style="display: block; margin-left: auto; margin-right: auto;" src="./images/profiles/minilop_white_blue.png" alt="cute white bunny" width="128" height="128" /></p>
  <p>Fluffle is the fun and easy game where you can buy, feed, grow, breed, and collect bunnies. You can also trade them with your friends!</p>
  <p>Fluffle is designed by Juliet, a 9 year old girl who loves bunnies.</p>
  <p>Fluffle is available now on iOS and coming soon to Android!</p>
<p></p>
  <a href="https://itunes.apple.com/us/app/fluffle/id1118628159?ls=1&mt=8">  <img src='images/Download_on_the_App_Store_Badge_US-UK_135x40.svg'></a>
  <a href='https://play.google.com/store/apps/details?id=com.eweware.fluffle&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img width="153" hright="46"  alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>

  <h2>Help spread the word by joining Fluffle on Social Media!</h2>
  <a target="_blank" title="follow me on facebook" href="https://www.facebook.com/fluffleit"><img alt="find me on facebook" src="https://c866088.ssl.cf3.rackcdn.com/assets/badgefacebook.png" border=0></a>


  <div class="fb-like" data-href="https://www.facebook.com/fluffleit/" data-layout="standard" data-action="like" data-show-faces="true" data-share="true"></div>

  </body>
</html>
