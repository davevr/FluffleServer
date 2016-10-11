package com.eweware.fluffle.rest;

import com.eweware.fluffle.api.Authenticator;
import com.eweware.fluffle.api.BunnyAPI;
import com.eweware.fluffle.api.PlayerAPI;
import com.eweware.fluffle.api.StoreAPI;
import com.eweware.fluffle.obj.BunnyObj;
import com.eweware.fluffle.obj.PlayerObj;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by davidvronay on 5/10/16.
 */
public class StoreREST extends HttpServlet {
    private static final Logger log = Logger.getLogger(StoreREST.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long curUserId = Authenticator.CurrentUserId(request.getSession());
        String bunnyIdStr = request.getParameter("bunnyid");

        if (bunnyIdStr != null) {
            // trying to sell a bunny
            if (curUserId == 0) {
                log.log(Level.SEVERE, "signed out out user trying to sell a bunny");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                Long bunnyId = Long.parseLong(bunnyIdStr);

                int salePrice = PlayerAPI.SellBunny(curUserId, bunnyId);

                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                RestUtils.get_gson().toJson(salePrice, out);
                out.flush();
                out.close();
            }
        } else {
            // trying to buy carrots
            if (curUserId == 0) {
                log.log(Level.SEVERE, "signed out out user trying to make a purchase");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                int newCarrots = 0;

                try {
                    String theReceipt = request.getParameter("receipt-data");
                    String productId = request.getParameter("product");
                    String storeId = request.getParameter("store");

                    if (productId == null || theReceipt == null || storeId == null) {
                        log.log(Level.SEVERE, "no product, receipt, or store specified for buy");
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                    } else {
                        if (StoreAPI.ValidateReceipt(curUserId, storeId, theReceipt)) {
                            newCarrots = StoreAPI.AddCredit(curUserId, productId);
                            response.setStatus(HttpServletResponse.SC_OK);
                        }

                    }
                } catch (Exception exp) {
                    log.severe(exp.getMessage());
                }

                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                RestUtils.get_gson().toJson(newCarrots, out);
                out.flush();
                out.close();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bunnyIdStr = request.getParameter("bunnyid");

        if (bunnyIdStr != null) {
            long bunnyId = Long.parseLong(bunnyIdStr);
            int price = BunnyAPI.GetSellPrice(bunnyId);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(price, out);
            out.flush();
            out.close();

        } else {
            // no detail - send them the store
            List<BunnyObj> storeList = StoreAPI.FetchAvailableBunnies();
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(storeList, out);
            out.flush();
            out.close();
        }
    }
}
