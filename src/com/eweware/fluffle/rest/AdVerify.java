package com.eweware.fluffle.rest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Dave on 10/10/2016.
 */
public class AdVerify extends HttpServlet {
    private static final Logger log = Logger.getLogger(AdVerify.class.getName());
    static String vungleKey = "BVpyUqAOsg6cAPyGcrp1ATpv+bjVGktH7sh/KmaKObk=";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdStr = request.getParameter("uid");

        log.severe("user " + userIdStr + " watched an ad");
    }
}
