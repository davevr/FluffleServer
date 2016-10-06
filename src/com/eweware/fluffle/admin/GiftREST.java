package com.eweware.fluffle.admin;

import com.eweware.fluffle.api.PlayerAPI;
import com.eweware.fluffle.obj.PlayerObj;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by Dave on 10/5/2016.
 */
public class GiftREST extends HttpServlet {
    private static final Logger log = Logger.getLogger(GiftREST.class.getName());
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final List<PlayerObj> foundItems =  ofy().load().type(PlayerObj.class).list();
        String numCarrotsStr = request.getParameter("carrots");
        int numCarrots = 1;

        if (numCarrotsStr != null)
            numCarrots = Integer.parseInt(numCarrotsStr);

        for (PlayerObj curPlayer : foundItems) {
            PlayerAPI.GiveCarrots(curPlayer, numCarrots);
        }
        log.severe("gave " + numCarrots + " carrots to " + foundItems.size() + " players");

        response.setStatus(HttpServletResponse.SC_OK);


    }
}
