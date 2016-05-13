package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.PlayerObj;
import com.googlecode.objectify.Key;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

import javax.crypto.Mac;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.googlecode.objectify.Key;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;


/**
 * Created by davidvronay on 5/10/16.
 */
public class Authenticator {

    private static final Logger log = Logger.getLogger(Authenticator.class.getName());
    private static Authenticator instance = null;
    public static final String USERID = "userid";
    private final static int ITERATIONS = 1000;
    private static final String TWO_WAY_CRYPT_METHOD = "PBEWithMD5AndDES";
    private static final char[] MASTER_PASSWORD = "23&-*/F43v02!s_83jJ@=a".toCharArray();
    private static final byte[] MASTER_SALT = {
            (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
            (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
    };



    protected Authenticator() {
        // Exists only to defeat instantiation.
    }
    public static Authenticator getInstance() {
        if(instance == null) {
            instance = new Authenticator();
        }
        return instance;
    }

    public static String[] createSaltedPassword(final String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        final byte[] bSalt = new byte[8];
        SecureRandom.getInstance("SHA1PRNG").nextBytes(bSalt);

        final byte[] bDigest = getHash(ITERATIONS, password, bSalt);

        return new String[]{new String(Base64.encodeBase64(bDigest)), new String(Base64.encodeBase64(bSalt))};
    }

    public static boolean authenticate(String digest, String salt, final String password) throws Exception {
        final byte[] proposedDigest = getHash(ITERATIONS, password, Base64.decodeBase64(salt));

        return Arrays.equals(proposedDigest, Base64.decodeBase64(digest));
    }



    public static PlayerObj AuthenticateUser(HttpSession session, String username, String password)
    {
        if (UserIsLoggedIn(session)) {
            Logout(session);
        }

        username = username.toLowerCase();
        PlayerObj newUser = ofy().load().type(PlayerObj.class).filter("username =", username).first().now();

        try {

            if ((newUser != null) && authenticate(newUser.passwordhash, newUser.passwordsalt, password)) {
                // we are in
                session.setAttribute(USERID, newUser.id);
                newUser.signedOn = true;
                newUser.lastActiveDate = new DateTime();
                ofy().save().entity(newUser);
                return newUser;
            } else {
                // passwords did not match
                return null;
            }
        }
        catch (Exception exp)
        {
            // authenticate failed
            return null;
        }
    }

    public static PlayerObj CreateNewUserNoPassword(HttpSession session) {
        PlayerObj newUser = PlayerAPI.CreateInstance();
        newUser.creationDate = new DateTime();

        ofy().save().entity(newUser).now();
        session.setAttribute(USERID, newUser.id);

        String username = "player" + newUser.id.toString();
        newUser.username = username;
        String password = username;
        try {
            String[] saltAndHash = createSaltedPassword(password);
            newUser.passwordhash = saltAndHash[0];
            newUser.passwordsalt = saltAndHash[1];
        } catch (Exception exp) {
            log.log(Level.SEVERE, exp.getMessage());
        }

        ofy().save().entity(newUser).now();

        return newUser;
    }




    public static PlayerObj CreateAndAuthenticateUser(HttpSession session, String username, String password)  throws Exception {
        if (UserIsLoggedIn(session)) {
            Logout(session);
        }

        PlayerObj newUser = null;
        username = username.toLowerCase();

        newUser = ofy().load().type(PlayerObj.class).filter("username =", username).first().now();

        if (newUser != null) {
            // username exists
            return null;
        } else {
            // user does not exist - create

            newUser = new PlayerObj();
            newUser.username = username;
            String[]    saltAndHash = createSaltedPassword(password);
            newUser.passwordhash = saltAndHash[0];
            newUser.passwordsalt = saltAndHash[1];
            newUser.signedOn = true;
            newUser.lastActiveDate = new DateTime();
            newUser.creationDate = new DateTime();

            ofy().save().entity(newUser).now();
            session.setAttribute(USERID, newUser.id);

            return newUser;

        }
    }

    public static long CurrentUserId(HttpSession session) {
        Object theId = session.getAttribute(USERID);
        if (theId != null)
            return (long)theId;
        else
            return 0;
    }

    public static PlayerObj CurrentUser(HttpSession session) {
        Object theId = session.getAttribute(USERID);
        if (theId != null) {
            PlayerObj newUser = ofy().load().key(Key.create(PlayerObj.class, (long)theId)).now();
            return newUser;
        }
        else
            return null;
    }

    public static Boolean UserIsLoggedIn(HttpSession session) {
        return session.getAttribute(USERID) != null;
    }


    public static Boolean Logout(HttpSession session)
    {
        if (session.getAttribute(USERID) != null) {
            // TODO:  update user's online status
            long userId = CurrentUserId(session);
            session.removeAttribute(USERID);
            PlayerObj newUser = ofy().load().key(Key.create(PlayerObj.class, userId)).now();
            newUser.signedOn = false;
            newUser.lastActiveDate = new DateTime();
            ofy().save().entity(newUser);
            return true;
        }
        else
            return false;

    }

    public static byte[] getHash(final int iterationNb, final String password, final byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(salt);
        byte[] input = digest.digest(password.getBytes("UTF-8"));
        for (int i = 0; i < iterationNb; i++) {
            digest.reset();
            input = digest.digest(input);
        }
        return input;
    }
}
