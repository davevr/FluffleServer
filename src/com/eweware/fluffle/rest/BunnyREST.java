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

/**
 * Created by davidvronay on 5/10/16.
 */
public class BunnyREST extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
}
