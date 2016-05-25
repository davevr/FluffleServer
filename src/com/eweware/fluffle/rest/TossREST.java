package com.eweware.fluffle.rest;

import com.eweware.fluffle.api.Authenticator;
import com.eweware.fluffle.api.TossAPI;
import com.eweware.fluffle.obj.TossObj;

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
public class TossREST extends HttpServlet {
    private static final Logger log = Logger.getLogger(TossREST.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long currentUserId = Authenticator.CurrentUserId(request.getSession());

        if (currentUserId == 0) {
            log.log(Level.SEVERE, "logged out user attempting to toss");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String bunnyStr = request.getParameter("bunny");
        String priceStr = request.getParameter("price");

        if (bunnyStr != null) {
            long bunnyId = Long.parseLong(bunnyStr);

            TossObj newToss = TossAPI.StartToss(currentUserId, bunnyId);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(newToss, out);
            out.flush();
            out.close();

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
