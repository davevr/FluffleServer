package com.eweware.fluffle.rest;

import com.eweware.fluffle.api.Authenticator;
import com.eweware.fluffle.api.BunnyAPI;
import com.eweware.fluffle.api.PlayerAPI;
import com.eweware.fluffle.obj.PlayerObj;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by davidvronay on 5/10/16.
 */
public class LoginREST extends HttpServlet {
    private static final Logger log = Logger.getLogger(LoginREST.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userNameStr = request.getParameter("username");
        String passwordStr = request.getParameter("pwd");
        PlayerObj thePlayer = null;

        if (userNameStr == null) {
            thePlayer = Authenticator.CreateNewUserNoPassword(request.getSession());
        } else {
            thePlayer = Authenticator.AuthenticateUser(request.getSession(), userNameStr, passwordStr);
        }

        if (thePlayer != null) {
            thePlayer.Bunnies = BunnyAPI.FetchBunniesByOwner(thePlayer.id);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(thePlayer, out);
            out.flush();
            out.close();
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userNameStr = request.getParameter("username");
        String passwordStr = request.getParameter("pwd");

        long curUserId = Authenticator.CurrentUserId(request.getSession());

        if (curUserId == 0) {
            log.log(Level.SEVERE, "Signed out user trying to change username or password");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (userNameStr != null) {
            // change the user's name.  Typically this is only done once
            boolean didIt = Authenticator.ChangeUserName(curUserId, userNameStr);
            if (didIt)
                response.setStatus(HttpServletResponse.SC_OK);
            else
                // probably a duplicate
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(didIt, out);
            out.flush();
            out.close();
        }

        if (passwordStr != null) {
            // change the user's password
            boolean didIt = Authenticator.ChangePassword(curUserId, passwordStr);
            if (didIt)
                response.setStatus(HttpServletResponse.SC_OK);
            else
                // something went wrong
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(didIt, out);
            out.flush();
            out.close();
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long curUserId = Authenticator.CurrentUserId(request.getSession());
        boolean didIt = false;

        if (curUserId > 0) {
            didIt = true;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        RestUtils.get_gson().toJson(didIt, out);
        out.flush();
        out.close();
    }
}
