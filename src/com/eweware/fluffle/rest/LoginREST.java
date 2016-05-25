package com.eweware.fluffle.rest;

import com.eweware.fluffle.api.Authenticator;
import com.eweware.fluffle.api.BunnyAPI;
import com.eweware.fluffle.obj.PlayerObj;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by davidvronay on 5/10/16.
 */
public class LoginREST extends HttpServlet {
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
