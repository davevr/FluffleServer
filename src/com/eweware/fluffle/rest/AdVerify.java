package com.eweware.fluffle.rest;

import com.eweware.fluffle.api.PlayerAPI;
import com.eweware.fluffle.obj.PlayerObj;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;
import java.nio.charset.Charset;
import java.security.MessageDigest;

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
        String digest = request.getParameter("digest");
        String txid = request.getParameter("txid");

        if (userIdStr != null) {

            long userId = Long.parseLong(userIdStr);
            PlayerObj theUser = PlayerAPI.FetchById(userId);
            if (theUser != null) {
                // verify the transaction is valid
                if (isDigestValid(txid, digest)) {
                    PlayerAPI.GiveCarrots(theUser, 50);
                    log.severe("user " + userIdStr + " watched an ad");
                }
                else {
                    log.severe("user " + userIdStr + " had invalid digest");
                }
            }
            else {
                log.severe("call to ad verify with invalid");
            }
        } else {
            log.severe("call to ad verify without a user");
        }

    }

    protected boolean isDigestValid(String transactionId, String verificationDigest)  {
        String theKey =  createSecurityDigest(transactionId, vungleKey);
        log.severe("digest=" + theKey + " - verify = " + verificationDigest);
        return theKey.equals(verificationDigest);
    }

    protected String createSecurityDigest(String transactionId, String secretKey)  {
        try {
            final String verificationString = secretKey + ":" + transactionId;
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return toHexString(
                    messageDigest.digest(
                            messageDigest.digest(
                                    verificationString.getBytes(Charset.forName("US-ASCII")))));
        }
        catch (Exception exp) {
            log.severe(exp.getMessage());
        }
        return "invalid digest";
    }

    protected String toHexString(byte[] bytes) {
        final StringBuffer hexStringBuffer = new StringBuffer();
        for (final byte byt : bytes) {
            hexStringBuffer.append(
                    Integer.toString((byt & 0xff) + 0x100, 16)
                            .substring(1));
        }
        return hexStringBuffer.toString();
    }
}
