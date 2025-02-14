package org.apache.clusterbr.zupportl5.controller;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.service.dropbox.DropboxTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <!-- comment-processor-start -->
 * <p>REST-API Endpoints --</p>
 * <ul>
 * <li> GET /api/dropbox/start-auth: Initiates Dropbox OAuth-2 authorization-flow by generating and returning the authorization URL.</li>
 * <li> GET /api/dropbox/oauth2/callback: Handles the authorization callback, retrieves the refresh token, and stores it securely.</li>
 * </ul>
 * 
 *  
 * <p><b>Implementing the OAUTH-2 AUTHORIZATION-FLOW as required by Dropbox</b></p>
 * <ol>
 * <li>ADMIN user triggers the authorization process by visiting /api/dropbox/start-auth.</li>
 * <li>The ADMIN is redirected to Dropbox's authorization page, to log-in, once logged-in the Authorization workflow continues the next-step.</li>
 * <li>Dropbox sends the CODE to the following endpoint of the APP: /api/dropbox/oauth2/callback; the callback(code) function receives the CODE sent by Dropbox.</li>
 * <li>Then the application exchanges the CODE with the Dropbox-api requesting for-a FRESH-TOKEN. The APP saves the FRESH-TOKEN securely.</li>
 * <li>Then the application exchanges the CODE with the Dropbox-api requesting for-a FRESH-TOKEN. The APP saves the  FRESH-TOKEN.</li>
 * <li>
 * </ol>
 * 
 * <p><b>REFRESH-TOKEN Explained</b></p>
 * <ul>
 * <li><b>Purpose</b>: Used to obtain a new access token when the current one expires.</li>
 * <li><b>Scope</b>: Tied to the same permissions granted during initial authentication.</li>
 * <li><b>Lifespan</b>: Long-lived (can last days, months, or indefinitely depending on the provider).</li>
 * <li><b>Does Not Expire Quickly</b>: It stays valid as long as the user hasn’t revoked the authorization.</li>
 * </ul>
 * 
 * <p><b>ACCESS-TOKEN Explained</b></p>
 * <ul>
 * <li><b>Purpose</b>: Grants temporary access to a specific API or resource.</li>
 * <li><b>Scope</b>: Limited to the permissions granted during authentication (e.g., read/write files).</li>
 * <li><b>Lifespan</b>: Short-lived (usually minutes to hours).</li>
 * <li><b>Expires Quickly</b>: Requires renewal after expiration.</li>
 * </ul>
 * 
 * <a href='https://www.dropbox.com/developers/documentation/http/documentation' target="_blank">Dropbox API v2</a><br/>
 * <a href='https://developers.dropbox.com/es-es/oauth-guide' target="_blank">Dropbox OAuth Guide</a><br/>
 * 
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxAuthorizationController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxAuthorizationController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxAuthorizationController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxAuthorizationController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@RestController
@RequestMapping("/api/dropbox")
public class DropboxAuthorizationController {

    private static final Logger logger = LoggerFactory.getLogger(DropboxAuthorizationController.class);
    
    private DropboxTokenService dropboxTokenService;

    @Autowired
    public DropboxAuthorizationController(DropboxTokenService dropboxTokenService) {
        this.dropboxTokenService = dropboxTokenService;
    }

    /**
     * 
     * <p>Entry end-point to initiate the Dropbox Authorization.</p>
     * <p>https://server:port/api/dropbox/oauth2/callback</p>
     * <p>https://www.dropbox.com/developers/documentation/http/documentation#oauth2-authorize</p>
     */
    @GetMapping("/start-auth")
    public ResponseEntity<String> startAuthorization() {
        
        //-- GCP: https://zupportl5.uc.r.appspot.com/api/dropbox/start-auth
        
        String msg;

        logger.info("(DropboxAuthorizationController::startAuthorization) started...");

        try {

            String authorizationUrl = dropboxTokenService.getAuthorizationUrl();

            if ( authorizationUrl.equals(DropboxTokenService.NO_VALUE) ){
                msg = "Authorization URL not found, authorizationUrl: ".concat(authorizationUrl);
                logger.warn("(DropboxAuthorizationController::startAuthorization) ".concat(msg));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);

            } else {

                logger.info("(DropboxAuthorizationController::startAuthorization) authorizationUrl PASSED, redirecting...");

                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(authorizationUrl)).build();
            }

        } catch (Exception ex) {
            msg = "Failed during authorization start : ".concat( (ex.getMessage() != null) ? ex.getMessage() : "NO_MSG");
            logger.error("(DropboxAuthorizationController::startAuthorization) ".concat(msg), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }

    @GetMapping("/oauth2/callback")
    public ResponseEntity<String> handleCallback(@RequestParam Map<String, String> params, HttpServletRequest request) {

        logger.info("(DropboxAuthorizationController::handleCallback) started...");

        params.forEach( (key, value) -> logger.info(key + " = " + value) );

        String msg,         
            code = (String) params.get("code"),
            redirectUri = constructCurrentUriFromRequest(request)
            ;

        if (code == null || code.isEmpty()) {
            msg = "Authorization state is missing.";
            logger.warn("(DropboxAuthorizationController::handleCallback) ".concat(msg));
            return ResponseEntity.badRequest().body(msg);        
        }

        try {

            logger.info("(DropboxAuthorizationController::handleCallback) callback-URL PASSED. redirectUri: " + redirectUri );

            String refreshToken = dropboxTokenService.getRefreshToken(code, redirectUri);

            logger.info("(DropboxAuthorizationController::handleCallback) refreshToken: ".concat(refreshToken) );

            /** if refreshToken is VALID redirect to success-page */
            if( !refreshToken.equals(DropboxTokenService.NO_VALUE) ) {

                msg = "DONE: Authorization to Dropbox PASSED.";

                return ResponseEntity.status(HttpStatus.OK).body(msg);
            } 
            /* else if refreshToken is INVALID redirect to error-page */
            else {

                msg = "ERROR: Authorization failure refresh-token was not set.";

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(msg);
            }

        } catch (Exception ex) {
            msg = "Failed to handle callback: ".concat( (ex.getMessage() != null) ? ex.getMessage() : "NO_MSG");
            logger.error("(DropboxAuthorizationController::handleCallback) ".concat(msg), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    } 

    private String constructCurrentUriFromRequest(HttpServletRequest request) {
        /*
            request.getScheme()       // "http/https"
            request.getServerName()   // "localhost/server-ip"
            request.getServerPort()   // 8989/8080
            request.getContextPath()  // "/api" | can be null, instead use: request.getRequestURI()
            request.getRequestURI()   // "/dropbox/oauth2/callback" or "/api/dropbox/oauth2/callback"
        */
        String scheme = Optional.ofNullable( request.getScheme() ).orElse(""),
            serverName = Optional.ofNullable( request.getServerName() ).orElse(""),
            serverPort = Optional.ofNullable( String.valueOf(request.getServerPort()) ).orElse(""),
            contextPath = Optional.ofNullable( request.getContextPath() ).orElse(""),
            requestUri = Optional.ofNullable( request.getRequestURI() ).orElse("")
            ;

        String currentUri = scheme + "://" + serverName + ":" + serverPort + contextPath + requestUri;

        return currentUri;
    }    
  
}

