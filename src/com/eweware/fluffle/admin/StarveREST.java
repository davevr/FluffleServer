package com.eweware.fluffle.admin;

import com.eweware.fluffle.api.BunnyAPI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Dave on 6/20/2016.
 */
public class StarveREST extends HttpServlet {
    private static final Logger log = Logger.getLogger(StarveREST.class.getName());


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BunnyAPI.StarveAllBunnies();
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
