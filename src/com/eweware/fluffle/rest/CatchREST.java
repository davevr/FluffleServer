package com.eweware.fluffle.rest;

import com.eweware.fluffle.api.Authenticator;
import com.eweware.fluffle.api.TossAPI;
import com.eweware.fluffle.obj.BunnyObj;
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
public class CatchREST extends HttpServlet {
    private static final Logger log = Logger.getLogger(CatchREST.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long currentUserId = Authenticator.CurrentUserId(request.getSession());

        if (currentUserId == 0) {
            log.log(Level.SEVERE, "logged out user attempting to catch");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String tossStr = request.getParameter("toss");

        if (tossStr != null) {
            long tossId = Long.parseLong(tossStr);

            BunnyObj theBuns = TossAPI.CatchToss(currentUserId, tossId);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(theBuns, out);
            out.flush();
            out.close();

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
