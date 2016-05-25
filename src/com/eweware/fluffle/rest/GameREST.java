package com.eweware.fluffle.rest;

import com.eweware.fluffle.api.GameAPI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by davidvronay on 5/10/16.
 */
public class GameREST extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String wantsGrowthStr = request.getParameter("growthchart");

        if (wantsGrowthStr != null) {
            List<Integer> growthChart = GameAPI.getGrowthStages();
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(growthChart, out);
            out.flush();
            out.close();
        }
    }
}
