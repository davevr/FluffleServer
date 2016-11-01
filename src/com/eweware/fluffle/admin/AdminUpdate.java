package com.eweware.fluffle.admin;

import com.eweware.fluffle.api.*;
import com.eweware.fluffle.obj.*;
import com.eweware.fluffle.rest.RestUtils;
import com.google.api.client.util.ArrayMap;
import com.googlecode.objectify.Key;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            } else if (typeStr.equals("remap")) {
                UpdateColorDataModel();
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                RestUtils.get_gson().toJson(true, out);
                out.flush();
                out.close();
            } else if (typeStr.equals("repair")) {
                didIt = RepairDataModel();
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                RestUtils.get_gson().toJson(didIt, out);
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
        PlayerObj curUser = Authenticator.CurrentUser(request.getSession());

        if (curUser != null && curUser.isAdmin) {
            String typeStr = request.getParameter("type");
            String idStr = request.getParameter("id");
            String nameStr = request.getParameter("name");
            String rarityStr = request.getParameter("rarity");
            Boolean didIt = false;

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

            if (didIt)
                GameAPI.UpdateBreeds();

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            RestUtils.get_gson().toJson(didIt, out);
            out.flush();
            out.close();
        } else {
            log.severe("non-admin attempting to post to adminUpdate");
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PlayerObj curUser = Authenticator.CurrentUser(request.getSession());

        if (curUser != null && curUser.isAdmin) {
            String typeStr = request.getParameter("type");
            String idStr = request.getParameter("id");

            Boolean didIt = true;

            if (typeStr.equals("furcolor")) {
                long furId = Long.parseLong(idStr);
                didIt = DeleteFurColor(furId);

            } else if (typeStr.equals("eyecolor")) {
                long eyeColorId = Long.parseLong(idStr);
                didIt = DeleteEyeColor(eyeColorId);
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
        } else {
            log.severe("non-admin attempting to post to adminUpdate");
        }
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
        newFur.parentBreedId = theBreed.id;
        ofy().save().entity(newFur).now();
        GameAPI.UpdateBreeds();
        return newFur;
    }

    private BunnyEyeColorObj CreateNewEyeColor(BunnyFurColorObj theFur, String newName) {
        BunnyEyeColorObj newEye = new BunnyEyeColorObj();
        newEye.ColorName = newName;
        newEye.rarity = 100;
        newEye.parentFurColorId = theFur.id;
        ofy().save().entity(newEye).now();
        GameAPI.UpdateBreeds();
        return newEye;
    }


    private boolean DeleteBreed(long breedId) {
        BunnyBreedObj theBreed = BunnyBreedAPI.FetchById(breedId);
        if (theBreed.possibleFurColors != null) {
            for (BunnyFurColorObj curFur : theBreed.possibleFurColors) {
                DeleteFurColorFast(curFur);
            }
        }
        ofy().delete().entity(theBreed).now();
        GameAPI.UpdateBreeds();
        return true;
    }

    private void DeleteFurColorFast(BunnyFurColorObj theFur) {
        if (theFur.possibleEyeColors != null) {
            for (BunnyEyeColorObj curEye : theFur.possibleEyeColors) {
                DeleteEyeColorFast(curEye);
            }
        }
        ofy().delete().entity(theFur).now();
    }

    private boolean DeleteFurColor(long furId) {
        boolean didIt = false;
        BunnyFurColorObj theFur = BunnyFurColorAPI.FetchById(furId);
        if (theFur != null) {
            DeleteFurColorFast(theFur);
            GameAPI.UpdateBreeds();
            didIt = true;
        }

        return didIt;
    }

    private void DeleteEyeColorFast(BunnyEyeColorObj theEye) {
        ofy().delete().entity(theEye).now();
    }

    private boolean DeleteEyeColor(long eyeId) {
        boolean didIt = false;
        BunnyEyeColorObj theEye = BunnyEyeColorAPI.FetchById(eyeId);
        if (theEye != null) {
            DeleteEyeColorFast(theEye);
            GameAPI.UpdateBreeds();
            didIt = true;
        }

        return didIt;
    }

    private void UpdateColorDataModel() {
        List<BunnyBreedObj> breedList = ofy().load().type(BunnyBreedObj.class).list();

        for (BunnyBreedObj curBreed : breedList) {
            if (curBreed.possibleFurColors != null) {
                RemapFurColors(curBreed);
            }
        }



        GameAPI.UpdateBreeds();
    }

    private boolean RepairDataModel() {
        List<BunnyObj> bunList = ofy().load().type(BunnyObj.class).list();

        for (BunnyObj curBuns : bunList) {
            RepairBunny(curBuns);
        }

        return true;
    }

    private void RepairBunny(BunnyObj curBuns) {

    }


    private void RemapFurColors(BunnyBreedObj curBreed) {
        if (curBreed.possibleFurColors.size() > 0) {
            Map<Long, BunnyFurColorObj> furIdMap = new ArrayMap<>();
            Map<Long, BunnyEyeColorObj> eyeIdMap = new ArrayMap<>();

            for (BunnyFurColorObj curFur : curBreed.possibleFurColors) {
                BunnyFurColorObj newFur = new BunnyFurColorObj();
                newFur.ColorName = curFur.ColorName;
                newFur.rarity = curFur.rarity;
                newFur.parentBreedId = curBreed.id;
                ofy().save().entity(newFur).now();
                if (curFur.possibleEyeColors != null) {
                    RemapEyeColors(curFur, newFur.id, eyeIdMap);
                }
                furIdMap.put(curFur.id, newFur);
            }

            // now remap all of the bunnies
            List<BunnyObj> bunnyObjs = ofy().load().type(BunnyObj.class).filter("BreedID =", curBreed.id).list();
            for (BunnyObj curBuns : bunnyObjs) {
                BunnyFurColorObj newFurMap = furIdMap.get(curBuns.FurColorID);
                BunnyEyeColorObj newEyeMap = eyeIdMap.get(curBuns.EyeColorID);
                if (newFurMap != null) {
                    curBuns.FurColorID = newFurMap.id;
                    curBuns.FurColorName = newFurMap.ColorName;

                }
                if (newEyeMap != null) {
                    curBuns.EyeColorName = newEyeMap.ColorName;
                    curBuns.EyeColorID = newEyeMap.id;
                }
                ofy().save().entity(curBuns).now();
            }
        }
    }

    private void RemapEyeColors(BunnyFurColorObj curFurColor, long newColorId, Map<Long, BunnyEyeColorObj> eyeIdMap) {
        for (BunnyEyeColorObj curEye : curFurColor.possibleEyeColors) {
            BunnyEyeColorObj newEye = new BunnyEyeColorObj();
            newEye.ColorName = curEye.ColorName;
            newEye.rarity = curEye.rarity;
            newEye.parentFurColorId = newColorId;
            ofy().save().entity(newEye).now();
            eyeIdMap.put(curEye.id, newEye);
        }
    }

}
