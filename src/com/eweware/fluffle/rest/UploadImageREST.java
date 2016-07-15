package com.eweware.fluffle.rest;

import com.eweware.fluffle.api.Authenticator;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.repackaged.com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by davidvronay on 7/14/16.
 */
public class UploadImageREST extends HttpServlet {
    private static final Logger log = Logger.getLogger(UploadImageREST.class.getName());
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (Authenticator.getInstance().UserIsLoggedIn(request.getSession())) {
            BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
            String theUrl = blobstoreService.createUploadUrl("/api/v1/uploadImage");

            PrintWriter out = response.getWriter();
            out.write(theUrl);
            out.flush();
            out.close();
        } else {
            log.log(Level.WARNING, "Failed to authenticate user");
            response.setStatus(HttpStatusCodes.STATUS_CODE_FORBIDDEN);
        }
    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // ensure user is signed in
        HttpSession session = request.getSession();

        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);

        List<BlobKey> blobKeys = blobs.get("file");

        if (blobKeys == null || blobKeys.isEmpty()) {
            log.log(Level.WARNING, "Failed to find image data");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        else {
            ImagesService imagesService = ImagesServiceFactory.getImagesService();
            ServingUrlOptions servingOptions = ServingUrlOptions.Builder.withBlobKey(blobKeys.get(0));
            String servingUrl = imagesService.getServingUrl(servingOptions);

            // write it to the user
            PrintWriter out = response.getWriter();
            out.write(servingUrl);
            out.flush();
            out.close();
        }
    }
}
