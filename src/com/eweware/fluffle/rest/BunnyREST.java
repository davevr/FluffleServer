package com.eweware.fluffle.rest;

import com.eweware.fluffle.api.Authenticator;
import com.eweware.fluffle.api.BunnyAPI;
import com.eweware.fluffle.obj.BunnyObj;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by davidvronay on 5/10/16.
 */
public class BunnyREST extends HttpServlet {
    private static final Logger log = Logger.getLogger(BunnyREST.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long currentUserId = Authenticator.CurrentUserId(request.getSession());

        if (currentUserId == 0) {
            log.log(Level.SEVERE, "logged out user attempting to breed bunny");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String momIdStr = request.getParameter("mom");
        String dadIdStr = request.getParameter("dad");

        if (momIdStr != null && dadIdStr != null) {
            long momId = Long.parseLong(momIdStr);
            long dadId = Long.parseLong(dadIdStr);

            BunnyObj newBuns = BunnyAPI.BreedBunnies(momId, dadId);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(newBuns, out);
            out.flush();
            out.close();


        } else {
            log.log(Level.SEVERE, "invalid mother and father bunnies");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<BunnyObj> bunnies = null;
        BunnyObj curBuns = null;
        final long curID = Authenticator.CurrentUserId(request.getSession());
        if (curID != 0) {
            String bunnyidstr = request.getParameter("bunnyid");

            if (bunnyidstr == null ) {
                bunnies = BunnyAPI.FetchBunniesByOwner(curID);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                RestUtils.get_gson().toJson(bunnies, out);
                out.flush();
                out.close();
            }
            else {
                long curBunId = Long.parseLong(bunnyidstr);
                curBuns = BunnyAPI.FetchById(curBunId);
                if (curBuns == null)
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                else {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    PrintWriter out = response.getWriter();
                    RestUtils.get_gson().toJson(curBuns, out);
                    out.flush();
                    out.close();
                }
            }
        } else
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long currentUserId = Authenticator.CurrentUserId(request.getSession());

        if (currentUserId == 0) {
            log.log(Level.SEVERE, "logged out user attempting to update bunny");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String renameStr = request.getParameter("renamebunny");
        String moveStr = request.getParameter("move");


        if (renameStr != null) {
            long bunnyId = Long.parseLong(renameStr);
            String newName = request.getParameter("name");
            BunnyAPI.RenameBunny(currentUserId, bunnyId, newName);
        }

        if (moveStr != null) {
            long bunnyId = Long.parseLong(moveStr);
            String xLocStr = request.getParameter("xloc");
            String yLocStr = request.getParameter("yloc");
            int xLoc = Integer.parseInt(xLocStr);
            int yLoc = Integer.parseInt(yLocStr);
            BunnyAPI.UpdateBunnyLoc(bunnyId, xLoc, yLoc);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");

    }
}
