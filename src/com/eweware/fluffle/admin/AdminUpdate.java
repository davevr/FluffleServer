package com.eweware.fluffle.admin;

import com.eweware.fluffle.api.*;
import com.eweware.fluffle.obj.BunnyBreedObj;
import com.eweware.fluffle.obj.BunnyEyeColorObj;
import com.eweware.fluffle.obj.BunnyFurColorObj;
import com.eweware.fluffle.obj.PlayerObj;
import com.eweware.fluffle.rest.RestUtils;
import com.googlecode.objectify.Key;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by Dave on 10/24/2016.
 */
public class AdminUpdate extends HttpServlet {
    private static final Logger log = Logger.getLogger(AdminUpdate.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PlayerObj curUser = Authenticator.CurrentUser(request.getSession());

        if (curUser != null && curUser.isAdmin) {
            String typeStr = request.getParameter("type");
            String nameStr = request.getParameter("name");
            Boolean didIt = true;

            if (typeStr.equals("furcolor")) {
                String breedIdStr = request.getParameter("breedid");
                long breedId = Long.parseLong(breedIdStr);
                BunnyBreedObj theBreed = BunnyBreedAPI.FetchById(breedId);

                if (theBreed != null) {
                    BunnyFurColorObj newFur = CreateNewFurColor(theBreed, nameStr);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    PrintWriter out = response.getWriter();
                    RestUtils.get_gson().toJson(newFur, out);
                    out.flush();
                    out.close();
                }

            } else if (typeStr.equals("eyecolor")) {
                String furColorIdStr = request.getParameter("furcolorid");
                long furColorId = Long.parseLong(furColorIdStr);
                BunnyFurColorObj theFur = BunnyFurColorAPI.FetchById(furColorId);

                if (theFur != null) {
                    BunnyEyeColorObj newEye = CreateNewEyeColor(theFur, nameStr);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    PrintWriter out = response.getWriter();
                    RestUtils.get_gson().toJson(newEye, out);
                    out.flush();
                    out.close();
                }
            } else if (typeStr.equals("breed")) {
                BunnyBreedObj newBreed = CreateNewBreed(nameStr);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                RestUtils.get_gson().toJson(newBreed, out);
                out.flush();
                out.close();
            } else {
                log.severe("Unknown create type - " + typeStr);
            }
        } else {
            log.severe("non-admin attempting to post to adminUpdate");
        }

    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            long eyeId = Long.parseLong(idStr);
            BunnyEyeColorObj theEye = BunnyEyeColorAPI.FetchById(eyeId);
            if (theEye != null) {
                boolean dirty = false;

                if (!theEye.ColorName.equals(nameStr) ) {
                    theEye.ColorName = nameStr;
                    dirty = true;
                }
                int newRarity = Integer.parseInt(rarityStr);
                if (theEye.rarity != newRarity) {
                    theEye.rarity = newRarity;
                    dirty = true;
                }

                if (dirty) {
                    ofy().save().entity(theEye).now();
                }

                didIt = true;
            }
        } else if (typeStr.equals("breed")) {
            long breedId = Long.parseLong(idStr);
            BunnyBreedObj theBreed = BunnyBreedAPI.FetchById(breedId);
            if (theBreed != null) {
                boolean dirty = false;

                if (!theBreed.BreedName.equals(nameStr) ) {
                    theBreed.BreedName = nameStr;
                    dirty = true;
                }
                int newRarity = Integer.parseInt(rarityStr);
                if (theBreed.rarity != newRarity) {
                    theBreed.rarity = newRarity;
                    dirty = true;
                }

                if (dirty) {
                    ofy().save().entity(theBreed).now();
                }

                didIt = true;
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        RestUtils.get_gson().toJson(didIt, out);
        out.flush();
        out.close();

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String typeStr = request.getParameter("type");
        String idStr = request.getParameter("id");

        Boolean didIt = true;

        if (typeStr.equals("furcolor")) {
            long furId = Long.parseLong(idStr);
            String breedIdStr = request.getParameter("breedid");
            long breedId = Long.parseLong(breedIdStr);
            didIt = DeleteFurColor(breedId, furId);

        } else if (typeStr.equals("eyecolor")) {
            long eyeColorId = Long.parseLong(idStr);
            String furColorIdStr = request.getParameter("furcolorid");
            long furColorId = Long.parseLong(furColorIdStr);
            didIt = DeleteEyeColor(furColorId, eyeColorId);
        } else if (typeStr.equals("breed")) {
            long breedId = Long.parseLong(idStr);
            didIt = DeleteBreed(breedId);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        RestUtils.get_gson().toJson(didIt, out);
        out.flush();
        out.close();

    }


    private BunnyBreedObj CreateNewBreed(String newName) {
        BunnyBreedObj newBreed = new BunnyBreedObj();
        newBreed.BreedName = newName;
        newBreed.rarity = 100;
        ofy().save().entity(newBreed).now();
        GameAPI.UpdateBreeds();
        return newBreed;
    }

    private BunnyFurColorObj CreateNewFurColor(BunnyBreedObj theBreed, String newName) {
        BunnyFurColorObj newFur = new BunnyFurColorObj();
        newFur.ColorName = newName;
        newFur.rarity = 100;
        ofy().save().entity(newFur).now();
        theBreed.possibleFurColors.add(newFur);
        ofy().save().entity(theBreed).now();
        GameAPI.UpdateBreeds();
        return newFur;
    }

    private BunnyEyeColorObj CreateNewEyeColor(BunnyFurColorObj theFur, String newName) {
        BunnyEyeColorObj newEye = new BunnyEyeColorObj();
        newEye.ColorName = newName;
        newEye.rarity = 100;
        ofy().save().entity(newEye).now();
        theFur.possibleEyeColors.add(newEye);
        ofy().save().entity(theFur).now();
        GameAPI.UpdateBreeds();
        return newEye;
    }


    private boolean DeleteBreed(long breedId) {
        BunnyBreedObj theBreed = BunnyBreedAPI.FetchById(breedId);
        for (BunnyFurColorObj curFur : theBreed.possibleFurColors) {
            DeleteFurColorFast(curFur);
        }
        ofy().delete().entity(theBreed).now();
        GameAPI.UpdateBreeds();
        return true;
    }

    private void DeleteFurColorFast(BunnyFurColorObj theFur) {
        for (BunnyEyeColorObj curEye : theFur.possibleEyeColors) {
            DeleteEyeColorFast(curEye);
        }
        ofy().delete().entity(theFur).now();
    }

    private boolean DeleteFurColor(long breedId, long furId) {
        boolean didIt = false;
        BunnyFurColorObj theFur = BunnyFurColorAPI.FetchById(furId);
        BunnyBreedObj theBreed = BunnyBreedAPI.FetchById(breedId);
        if (theFur != null && theBreed != null) {
            theBreed.possibleFurColors.remove(theFur);
            DeleteFurColorFast(theFur);
            ofy().save().entity(theBreed).now();
            GameAPI.UpdateBreeds();
            didIt = true;
        }

        return didIt;
    }

    private void DeleteEyeColorFast(BunnyEyeColorObj theEye) {
        ofy().delete().entity(theEye).now();
    }

    private boolean DeleteEyeColor(long furId, long eyeId) {
        boolean didIt = false;
        BunnyFurColorObj theFur = BunnyFurColorAPI.FetchById(furId);
        BunnyEyeColorObj theEye = BunnyEyeColorAPI.FetchById(eyeId);
        if (theFur != null && theEye != null) {
            theFur.possibleEyeColors.remove(theEye);
            DeleteEyeColorFast(theEye);
            ofy().save().entity(theFur).now();
            GameAPI.UpdateBreeds();
            didIt = true;
        }

        return didIt;
    }
}
