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
public class PlayerREST extends HttpServlet {
    private static final Logger log = Logger.getLogger(PlayerREST.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long currentUserId = Authenticator.CurrentUserId(request.getSession());

        if (currentUserId == 0) {
            log.log(Level.SEVERE, "logged out user attempting to get player info");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String countStr = request.getParameter("carrotcount");
        String messageStr = request.getParameter("message");

        if (countStr != null) {
            PlayerObj curPlayer = PlayerAPI.FetchById(currentUserId);
            int count = curPlayer.carrotCount;
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(count, out);
            out.flush();
            out.close();
        } else if (messageStr != null) {
            String message = PlayerAPI.GetDailyMessage(currentUserId);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(message, out);
            out.flush();
            out.close();
        }
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
        String nickStr = request.getParameter("nickname");
        String petStr = request.getParameter("pet");
        String imageStr = request.getParameter("image");

        if (petStr != null) {
            long bunnyId = Long.parseLong(petStr);
            PlayerAPI.PlayerPetBunny(currentUserId, bunnyId);
        }

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

        if (nickStr != null) {
            PlayerAPI.ChangeNickname(currentUserId, nickStr);
        }

        if (imageStr != null) {
            PlayerAPI.ChangeUserImage(currentUserId, imageStr);
        }

        response.setStatus(HttpServletResponse.SC_OK);


    }
}
