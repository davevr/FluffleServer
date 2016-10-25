package com.eweware.fluffle.admin;

import com.eweware.fluffle.api.BunnyFurColorAPI;
import com.eweware.fluffle.obj.BunnyFurColorObj;
import com.eweware.fluffle.rest.RestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by Dave on 10/24/2016.
 */
public class AdminUpdate extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String typeStr = request.getParameter("type");
        String idStr = request.getParameter("id");
        String nameStr = request.getParameter("name");
        String rarityStr = request.getParameter("rarity");
        Boolean didIt = true;

        if (typeStr.equals("furcolor")) {
            long furId = Long.parseLong(idStr);
            BunnyFurColorObj theFur = BunnyFurColorAPI.FetchById(furId);
            if (theFur != null) {
                boolean dirty = false;

                if (!theFur.ColorName.equals(nameStr) ) {
                    theFur.ColorName = nameStr;
                    dirty = true;
                }
                int newRarity = Integer.parseInt(rarityStr);
                if (theFur.rarity != newRarity) {
                    theFur.rarity = newRarity;
                    dirty = true;
                }

                if (dirty) {
                    ofy().save().entity(theFur).now();
                }

                didIt = true;

            }

        } else if (typeStr.equals("eyecolor")) {

        } else if (typeStr.equals("breed")) {

        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        RestUtils.get_gson().toJson(didIt, out);
        out.flush();
        out.close();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
