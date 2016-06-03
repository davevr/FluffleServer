package com.eweware.fluffle.rest;

import com.eweware.fluffle.api.LeaderboardAPI;
import com.eweware.fluffle.obj.BunnyObj;
import com.eweware.fluffle.obj.PlayerObj;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by davidvronay on 6/2/16.
 */
public class LeaderBoardsREST extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String countStr = request.getParameter("bycount");
        String sizeStr = request.getParameter("bysize");
        String shareStr = request.getParameter("byshares");
        String spreadStr = request.getParameter("byspread");

        if (countStr != null) {
            List<PlayerObj> players = LeaderboardAPI.PlayersWithMostBunnies();
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(players, out);
            out.flush();
            out.close();
        } else if (sizeStr != null) {
            List<BunnyObj> bunnies = LeaderboardAPI.BunniesWithLargestSize();
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(bunnies, out);
            out.flush();
            out.close();

        } else if (shareStr != null) {
            List<PlayerObj> players = LeaderboardAPI.PlayersWithMostShares();
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(players, out);
            out.flush();
            out.close();
        } else if (spreadStr != null) {
            List<BunnyObj> bunnies = LeaderboardAPI.BunniesWithMostShares();
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(bunnies, out);
            out.flush();
            out.close();
        }
    }
}
