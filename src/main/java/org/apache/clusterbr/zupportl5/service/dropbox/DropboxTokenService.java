package org.apache.clusterbr.zupportl5.service.dropbox;

import java.net.URI;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.clusterbr.zupportl5.component.DatabaseSessionStore;
import org.apache.clusterbr.zupportl5.dto.AppInfoHolder;
import org.apache.clusterbr.zupportl5.dto.SettingsDto;
import org.apache.clusterbr.zupportl5.entity.Settings;
import org.apache.clusterbr.zupportl5.mapper.SettingsMapper;
import org.apache.clusterbr.zupportl5.service.SettingsService;
import org.apache.clusterbr.zupportl5.utils.AppLangUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.IncludeGrantedScopes;
import com.dropbox.core.TokenAccessType;
import com.dropbox.core.oauth.DbxCredential;

import jakarta.annotation.PostConstruct;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxTokenService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxTokenService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxTokenService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxTokenService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
*/    
@Service
public class DropboxTokenService {

    private static final Logger logger = LoggerFactory.getLogger(DropboxTokenService.class);

    public static final int 
        MIN_REFRESH_TOKEN_SIZE = 30
        , ACCESS_TOKEN_EXPIRATION_IN_MINUTES = 120
        ;

    public static final String 
        NO_VALUE = "_NO_VALUE_",        
        TOKEN_SAVED_SUCCESSFULLY = "TOKEN SAVED SUCCESSFULLY",
        DROPBOX_REFRESH_TOKEN_KEY = "DROPBOX_REFRESH_TOKEN",
        DROPBOX_ACCESS_TOKEN_01_KEY = "DROPBOX_ACCESS_TOKEN_01",
        DROPBOX_ACCESS_TOKEN_02_KEY = "DROPBOX_ACCESS_TOKEN_02",
        DROPBOX_CLIENT_ID_KEY = "DROPBOX_CLIENT_ID",
        DROPBOX_CLIENT_SECRET_KEY = "DROPBOX_CLIENT_SECRET"
        ;

    //-- static since this URI does not changes during App execution
    private static String appDropboxRedirectUri;    

    //-- below variables take their value from ENVIRONMENT-VARIABLES (if initialized)
    @Value("${property.application.dropbox.client-id:_CLIENT_ID_NOT_SET_}")
    private String clientId;
    @Value("${property.application.dropbox.client-secret:_SECRET_WRD_NOT_SET_}")
    private String clientSecret;

    //-- below variables take their value from application.properties always
    @Value("${property.application.dropbox.temp.token:_TEMP_TOKEN_NOT_SET_}")
    private String tempAccessToken;
    @Value("${property.application.dropbox.oauth2.token-service:https://api.dropbox.com/oauth2/token}")
    private String DROPBOX_OAUTH2_TOKEN_SERVICE;
    @Value("${property.application.dropbox.oauth2.callback:/api/dropbox/oauth2/callback}")
    private String DROPBOX_OAUTH2_CALLBACK;
    @Value("${property.application.dropbox.oauth2.callback.full:https://zupportl5.uc.r.appspot.com/api/dropbox/oauth2/callback}")
    private String DROPBOX_OAUTH2_CALLBACK_FULL;

    //-- final instance-variables
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private final DatabaseSessionStore databaseSessionStore;
    @Autowired
    private final SettingsService settingsService;
    
    private final Map<String, String> dropboxCredentials = new HashMap<>();

    @Autowired
    public DropboxTokenService(
        JdbcTemplate jdbcTemplate, 
        NamedParameterJdbcTemplate namedParameterJdbcTemplate,
        DatabaseSessionStore databaseSessionStore,
        SettingsService settingsService) {
                
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.databaseSessionStore = databaseSessionStore;
        this.settingsService = settingsService;
    }

    @PostConstruct
    private void init() {
        try {
            appDropboxRedirectUri = AppInfoHolder.getInstance().getServerUrl().concat(DROPBOX_OAUTH2_CALLBACK);
            //--> FIX: 2024-1229
            if( appDropboxRedirectUri.contains("null") ) {
                appDropboxRedirectUri = DROPBOX_OAUTH2_CALLBACK_FULL;
            }
            loadDropboxCredentials();
            logger.info("(DropboxTokenService::init) appDropboxRedirectUri: {}", appDropboxRedirectUri);
        } catch(Exception ex) {
            logger.error("(DropboxTokenService::init())", ex);
            String msg = (ex.getMessage() != null) ? ex.getMessage() : "";
            logger.error("(DropboxTokenService::init()) message = " + msg);
        }
    }

    private void loadDropboxCredentials() {
        
        //-- uses jdbcTemplate template to read values from settings table
        
        String sql = "SELECT id_key, value FROM settings WHERE id_key IN (?, ?)";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, DROPBOX_CLIENT_ID_KEY, DROPBOX_CLIENT_SECRET_KEY);

        for (Map<String, Object> row : results) {
            dropboxCredentials.put((String) row.get("id_key"), (String) row.get("value"));
        }

        validateIfNullGetFromEnvironmentVars(DROPBOX_CLIENT_ID_KEY, clientId, "_CLIENT_ID_NOT_SET_");
        validateIfNullGetFromEnvironmentVars(DROPBOX_CLIENT_SECRET_KEY, clientSecret, "_SECRET_WRD_NOT_SET_");
    }

    private void validateIfNullGetFromEnvironmentVars(String key, String environmentValue, String unsetValue) {
        if (!dropboxCredentials.containsKey(key)) {
            dropboxCredentials.put(key, environmentValue);
            if (environmentValue.equals(unsetValue)) {
                logger.warn("(DropboxTokenService::validateIfNullGetFromEnvironmentVars) {} is NOT SET in environment.", key);
            }
        }
    }

    /**
     * <p>Entry end-point to initiate the Dropbox Authentication.</p>
     * <p><i>http://APP-HOST-SERVER:PORT/api/dropbox/oauth2/callback</i><p>
     * 
     * <p>If LOGIN success the <b>RAW authorizationUrl</b> generated by Dropbox will have below format:</p>
     * <ul>
     * <li><b>https://www.dropbox.com/oauth2/authorize</b> ─ The Dropbox OAuth2 authorization endpoint</li>
     * <li><b>?response_type</b>=<i>code</i> ─ Specifies that the response should include an authorization code</li>
     * <li><b>&amp;redirect_uri</b>=<i>http://localhost:8989/api/dropbox/oauth2/callback</i> ─ The URL to redirect to once the ADMIN/user grants or denies permission</li>
     * <li><b>&amp;state</b>=<i>Dj3gdRlt3jIfAvL-UMcUKQ==</i> ─ The UUID value generated in the AAP to maintain state between the request and callback</li>
     * <li><b>&amp;client_id</b>=<i>ynakgz3ugz49a8l</i> ─ The unique-identifier for the APP, as it was registered in Dropbox</li>
     * </ul>
     * 
     */    
    public String getAuthorizationUrl() {

        String msg;

        /**
         * if Dropbox credentials are invalid return <NO_VALUE>
         */
        if( !dropboxCredentialsAreValid() ) {
            msg = "(DropboxTokenService::getAuthorizationUrl) Credentials are not valid, returning: ";
            logger.warn(msg.concat(NO_VALUE));
            return NO_VALUE;
        }

        /**
         * else, try to request the AuthorizationUrl
         */

        try {
            DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox-java-sdk").build();            

            String clientId = dropboxCredentials.get(DROPBOX_CLIENT_ID_KEY), 
                   clientSecret = dropboxCredentials.get(DROPBOX_CLIENT_SECRET_KEY);

            DbxAppInfo appInfo = new DbxAppInfo(clientId, clientSecret);

            DbxWebAuth webAuth = new DbxWebAuth(config, appInfo);

            //-- generate a unique request-id (e.g., UUID) for the authorization request
            String uniqueRequestId = UUID.randomUUID().toString();

            //-- saves the unique request-id to database as required when invoking: DbxWebAuth.newRequestBuilder
            databaseSessionStore.set(uniqueRequestId, NO_VALUE, clientId);

            Collection<String> scopeCollection = Arrays.asList( 
                "files.metadata.write", "files.metadata.read", "files.content.write", "files.content.read" );

            DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder()
                    .withScope(scopeCollection) 
                    .withTokenAccessType(TokenAccessType.OFFLINE)
                    .withIncludeGrantedScopes(IncludeGrantedScopes.USER)
                    .withRedirectUri(appDropboxRedirectUri, this.databaseSessionStore)
                    .build();

            //-- Starts authorization, redirecting the ADMIN/user to dropbox log-in page
            String authorizationURL = webAuth.authorize(authRequest);
            logger.info("(DropboxTokenService::getAuthorizationUrl) rawUrl = webAuth.authorize(authRequest): ");
            logger.info(authorizationURL);             

            /**
             * authorizationURL should look like this:
             * 
             * https://www.dropbox.com/oauth2/authorize
             *      ?response_type=code
             *      &redirect_uri=http://localhost:8989/api/dropbox/oauth2/callback
             *      &state=UE0juXAQPehxz90anY7wkA==
             *      &client_id=ynakgz3ugz49a8l
             * 
             * Note: "state" is the same as uniqueRequestId
             * 
             * Parses the authorizationURL to create a Map object with the params
             */         

            URI uri = new URI(authorizationURL);
            Map<String, String> params = Arrays.stream(uri.getQuery().split("&"))
                    .map(pair -> pair.split("="))
                    .collect(Collectors.toMap(
                            kv -> decode(kv[0]),
                            kv -> kv.length > 1 ? decode(kv[1]) : ""
                    ));
            
            logger.info("DropboxTokenService::getAuthorizationUrl() params :");
            params.forEach( (key, value) -> logger.info(key + " = " + value) );

            String state = params.get("state");

            //-- saves the state to the database
            databaseSessionStore.set(uniqueRequestId, state, clientId);               

            return authorizationURL;

        } catch(Exception ex) {
            msg = "(DropboxTokenService::getAuthorizationUrl) Error in authorizationUrl, Url: ".concat(NO_VALUE);
            logger.error(msg, ex);            
        }
        return NO_VALUE;
    }

    public String getRefreshToken(String code, String redirectUri) {

        String refreshToken = NO_VALUE, msg;

        try {
            dropboxCredentialsAreValid();

            DbxRequestConfig config = DbxRequestConfig
            .newBuilder("dropbox-java-sdk")
            .build();          

            DbxAppInfo appInfo = new DbxAppInfo(
                dropboxCredentials.get(DROPBOX_CLIENT_ID_KEY), dropboxCredentials.get(DROPBOX_CLIENT_SECRET_KEY)
            );

            DbxWebAuth webAuth = new DbxWebAuth(config, appInfo);
            
            DbxAuthFinish authFinish = webAuth.finishFromCode(code, redirectUri);

            refreshToken = authFinish.getRefreshToken();

            if (refreshToken != null && !refreshToken.equals(NO_VALUE)) {

                //-- saves refreshToken to database
                //-- theoretically: Refresh tokens do not expire!
                saveSettings(DROPBOX_REFRESH_TOKEN_KEY, refreshToken, null, null);

                //-- creates a new accessToken and saves it to database
                //-- ------------>>>
                String accessToken01 = authFinish.getAccessToken();

                //-- returns the time when {@link DbxAuthFinish#accessToken} expires in millisecond. If null then
                //-- it won't expire. Pass this in to the {@link com.dropbox.core.v2.DbxClientV2} constructor.
                Long accessToken01ExpiresAt = authFinish.getExpiresAt();
                String accessTokenExpiresAtStr = AppLangUtil.convertInstantToDateString(accessToken01ExpiresAt);                
                saveSettings(DROPBOX_ACCESS_TOKEN_01_KEY, accessToken01, accessToken01ExpiresAt, accessTokenExpiresAtStr);
                //-- ------------>>>

                logger.info("(DropboxTokenService::getRefreshToken) refreshToken and accessToken01 saved to database.");
                logger.info("refreshToken="+refreshToken);
                logger.info("accessToken01="+accessToken01);
                logger.info("accessToken01ExpiresAt="+accessToken01ExpiresAt);

            } else {
                logger.warn("(DropboxTokenService::getRefreshToken) refreshToken is null");
            }

        } catch (Exception ex) {
            refreshToken = NO_VALUE;
            msg = "(DropboxTokenService::getRefreshToken) Error in getRefreshToken(), refresh-token: ".concat(refreshToken);
            logger.error(msg, ex);
        }

        return refreshToken; 
   }

   public void saveSettings(String idKey, String value, Long expiresAtLong, String expiresAtTimeStr) {

       /**
        * protects from saving invalid token VALUE to database
        */
        if(value == null || value.equals(NO_VALUE)) {
            logger.warn("(DropboxTokenService::saveSettings) Attempting to save Invalid token to database.");
            logger.warn("idKey: " + idKey);
            logger.warn("value: " + ( (value==null) ? "null" : value) );
            return;
        }

       String checkQuery = "SELECT COUNT(*) FROM settings WHERE id_key = :id_key";      

       MapSqlParameterSource params = new MapSqlParameterSource();
       params.addValue("id_key", idKey);
       params.addValue("value", value);
       params.addValue("expires_at_long", expiresAtLong);
       params.addValue("expires_at_time", expiresAtTimeStr);

       try {
            Integer count = namedParameterJdbcTemplate.queryForObject(checkQuery, params, Integer.class);
            if (count != null && count == 0) {
                String insertQuery = "INSERT INTO settings (id_key, value, expires_at_long, expires_at_time) "
                        .concat("VALUES (:id_key, :value, :expires_at_long, :expires_at_time)");
                namedParameterJdbcTemplate.update(insertQuery, params);
            } else {
                String updateQuery = "UPDATE settings SET value=:value, expires_at_long=:expires_at_long, "
                        .concat("expires_at_time=:expires_at_time WHERE id_key=:id_key");
                namedParameterJdbcTemplate.update(updateQuery, params);
            }
        } catch (Exception ex) {
            logger.error("(DropboxTokenService::saveSettings) Error: ", ex);
        }
    }
    
    public SettingsDto getAccessTokenFromDBOrRequestNew() {

        SettingsDto refreshTokenDB = getRefreshTokenFromDatabase();
        SettingsDto accessTokenDB = getAccessTokenFromDatabase();

        /**
         * if refreshToken.equals(NO_VALUE) means that the Dropbox-Authorization flow
         * has not been yet initialized, since the first authorization requires human intervention
         * hence the <tempAccessToken> is returned instead. 
         * This <tempAccessToken> can already be expired, but avoids throwing exceptions
         */
        if ( refreshTokenDB == null || refreshTokenDB.getValue().equals(NO_VALUE) ) {            
            String msg = "(DropboxTokenService::getCachedAccessTokenOrRequestNew) refreshToken IS INVALID, "
                    .concat("using tempAccessToken instead: ").concat(tempAccessToken);
            logger.warn(msg);
            
            //-- gets old access-token from database
            SettingsDto oldAccessToken = getAccessTokenFromDatabase();
            if(oldAccessToken != null ) {
                return oldAccessToken; 
            } else {
                //-- if no access-token in database return tempAccessToken value
                accessTokenDB.setValue(tempAccessToken);
                return accessTokenDB;
            }
        }

        /**
         * If refreshToken is valid, try to generate a valid AccessToken
         */    
        try {
            // -- >>> Method-1
            DbxCredential credential = new DbxCredential(
                null, //-- No initial access token
                -1L,  //-- No initial expiration time
                refreshTokenDB.getValue(), //-- the refresh-token value from database
                dropboxCredentials.get(DROPBOX_CLIENT_ID_KEY),
                dropboxCredentials.get(DROPBOX_CLIENT_SECRET_KEY)
            );
            // -- use the credential to get a valid access token 
            //--  the method refreshes it automatically if already expired
            String newAccessToken = credential.getAccessToken();
            Long newAccessTokenExpireAt = credential.getExpiresAt();
            String newAccessTokenExpiresAtStr = AppLangUtil.convertInstantToDateString(newAccessTokenExpireAt);
            // -- >>>

            //-- asign new values to access-token
            accessTokenDB.setValue(newAccessToken);
            accessTokenDB.setExpiresAtLong(newAccessTokenExpireAt);
            accessTokenDB.setExpiresAtTime( AppLangUtil.convertToTimestamp(newAccessTokenExpiresAtStr));

            //-- saves access token to database                
            this.saveSettings(DROPBOX_ACCESS_TOKEN_02_KEY, newAccessToken, newAccessTokenExpireAt, newAccessTokenExpiresAtStr);

            logger.info("(getCachedAccessTokenOrRequestNew) Success in getAccessToken()");
            logger.info("newAccessToken: " + newAccessToken); 
            logger.info("newAccessTokenExpireAt: " + String.valueOf(newAccessTokenExpireAt) ); 
            logger.info("newAccessTokenExpiresAtStr: " + newAccessTokenExpiresAtStr);
                        
        } catch (Exception ex) {
            refreshTokenDB.setValue(NO_VALUE);
            logger.warn("refreshTokenDB:");
            logger.warn(refreshTokenDB.toString());
            String msg = (ex.getMessage() != null) ? ex.getMessage() : "ERROR when attempting to get new access-token.";
            logger.warn("[ERROR] (DropboxTokenService::getCachedAccessTokenOrRequestNew) Error in getAccessToken()");
            logger.warn(msg);
        }
        return accessTokenDB;
    }    
    
    private boolean isTokenExpired(Settings token) {
        /*
         * If the absolute milliseconds stored in cachedAccessTokenExpireAt is less than 
         * current-time absolute milliseconds, means that the token already has expired
         */
        return token.getExpiresAtLong() <= System.currentTimeMillis();
    }

    private boolean dropboxCredentialsAreValid() {        
        String msg = "";
        boolean result = true;        
        if (dropboxCredentials.containsKey(DROPBOX_CLIENT_ID_KEY) &&
            dropboxCredentials.containsKey(DROPBOX_CLIENT_SECRET_KEY)) {
            msg = "Dropbox credentials (DROPBOX_CLIENT_ID_KEY and DROPBOX_CLIENT_SECRET_KEY) were found.";
            logger.info("(DropboxTokenService::dropboxCredentialsAreValid) ".concat(msg));
            result = true;
        } else {
            msg = "Dropbox credentials (DROPBOX_CLIENT_ID_KEY and DROPBOX_CLIENT_SECRET_KEY) WERE NOT FOUND !!.";
            logger.warn("(DropboxTokenService::dropboxCredentialsAreValid) ".concat(msg));
            result = false;
        }        
        return result;
    }

    //-- Database operations

    public SettingsDto getRefreshTokenFromDatabase() {        
        return this.getTokenFromDatabase(DROPBOX_REFRESH_TOKEN_KEY);
    }

    public SettingsDto getAccessTokenFromDatabase() {
        return this.getTokenFromDatabase(DROPBOX_ACCESS_TOKEN_01_KEY);
    }

    public SettingsDto getTokenFromDatabase(String tokenKey) {

        SettingsDto dtoToken = new SettingsDto(tokenKey, NO_VALUE);

        try {       

            Optional<Settings> entityToken = settingsService.getByKey(tokenKey);
            
            if(entityToken.isPresent() ) {
                dtoToken = SettingsMapper.toDTO(entityToken.get());
            }
            
            checkIfTokenIsValid(dtoToken);

        } catch (Exception ex) {
            logger.error("(DropboxTokenService::getTokenFromDatabase) Error retrieving refresh-token from database", ex);            
        }
        return dtoToken;
    }

    /**
     * if token is less than MIN_REFRESH_TOKEN_SIZE-chars, is not a valid token.
     * refresh-token and access-token should look-like:
     *   Ub_xFPugihAAAAAAAAAAAUAa66Yxj7spOiA43LaNrsRC3O9en5A8jROHGSf1DOd4
     *   sl.CB-cuyeOJ7ZmvGIweXxWgnJ65gOg1N0DtwTykd8VqYwd59_vpjyCrfiZgKeduqyU6hnA9QUaZgDwsAhjLuSnMc7V-ekd1FU8cS39dwCDdyZUqkG8E9eabvHFx04rMpRveAaqmQaI1Tdv
     * */ 
    private boolean checkIfTokenIsValid(SettingsDto token) {
        boolean result = true;
        if ( token == null || token.getValue().length() <= MIN_REFRESH_TOKEN_SIZE ) {
            String msg = "Invalid token from database, token is less than "
                .concat(String.valueOf(MIN_REFRESH_TOKEN_SIZE)).concat("-chars: ").concat(token.getValue());
            logger.warn("(DropboxTokenService::checkIfTokenIsValid) ".concat(msg) );
            result = false;
        }
        return result;
    }

    private static String decode(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }
}
