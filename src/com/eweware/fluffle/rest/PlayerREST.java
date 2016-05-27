package com.eweware.fluffle.rest;

import com.eweware.fluffle.api.Authenticator;
import com.eweware.fluffle.api.BunnyAPI;
import com.eweware.fluffle.api.PlayerAPI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by davidvronay on 5/10/16.
 */
public class PlayerREST extends HttpServlet {
    private static final Logger log = Logger.getLogger(PlayerREST.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long currentUserId = Authenticator.CurrentUserId(request.getSession());

        if (currentUserId == 0) {
            log.log(Level.SEVERE, "logged out user attempting to update player");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String feedStr = request.getParameter("feed");
        String giveCarrotsStr = request.getParameter("givecarrots");
        String buyStr = request.getParameter("buybunny");
        String sellStr = request.getParameter("sellbunny");

        if (feedStr != null) {
            long bunnyId = Long.parseLong(feedStr);
            PlayerAPI.PlayerFedBunny(currentUserId, bunnyId);
        }

        if (giveCarrotsStr != null) {
            int numCarrots = Integer.parseInt(giveCarrotsStr);
            PlayerAPI.PlayerGotCarrots(currentUserId, numCarrots);
        }

        if (buyStr != null) {
            long bunnyId = Long.parseLong(buyStr);
            PlayerAPI.PlayerBoughtBunny(currentUserId, bunnyId);
        }

        if (sellStr != null) {
            long bunnyId = Long.parseLong(sellStr);
            PlayerAPI.PlayerSoldBunny(currentUserId, bunnyId);
        }


    }
}
