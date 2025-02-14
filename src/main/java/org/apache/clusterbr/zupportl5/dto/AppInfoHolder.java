package org.apache.clusterbr.zupportl5.dto;

import org.apache.clusterbr.zupportl5.utils.PropertiesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/AppInfoHolder_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class AppInfoHolder {
    
    private static final Logger logger = LoggerFactory.getLogger(AppInfoHolder.class);

    private static AppInfoHolder instance;
    
    private String protocol;    
    private String hostName;
    private int serverPort;

    /**
     * prevent instantiation, since is singleton-class
     */
    private AppInfoHolder() {
    }

    public static synchronized AppInfoHolder getInstance() {
        if (instance == null) {
            instance = new AppInfoHolder();
        }
        return instance;
    }

    //-- getters & setters

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }    

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerUrl() {

        StringBuilder url = new StringBuilder();

        protocol = (protocol == null) ? "https" : protocol;

        url.append(protocol).append("://").append(hostName);

        /* check if not default ports for http (80) and https (443) to concatenate */
        if ( (protocol.equals("http") && serverPort != 80) 
            || (protocol.equals("https") && serverPort != 443)) {
            url.append(":").append(serverPort);
        }

        String rawUrl = url.toString();

        String finalUrl = correctUrl(rawUrl);

        return finalUrl;
    }

    private String correctUrl(String rawUrl) {
        PropertiesProvider provider = PropertiesProvider.getInstance();

        String defaultUrl = provider.getProperty("property.prod.gcp.app.engine.host");
        String finalUrl = (rawUrl.length() <= 15) ? defaultUrl : rawUrl;        

        String profile = PropertiesProvider.getInstance().getActiveProfile();

        //-- fix for: https://zupportl5-api.onrender.com
        String prodRenderDockerInternalHost = provider.getProperty("property.prod.render.docker.internal.host");
        if ( profile.equals(PropertiesProvider.PROD) && rawUrl.contains(prodRenderDockerInternalHost) ) {
            finalUrl =  provider.getProperty("property.prod.render.docker.external.host");

        // GCP engine: https://zupportl5.uc.r.appspot.com
        } else if (profile.equals(PropertiesProvider.PROD) && rawUrl.contains("appspot.com") ) {
            finalUrl =  provider.getProperty("property.prod.gcp.app.engine.host");  
        }

        logger.info("(AppInfoHolder::correctUrl) defaultUrl=" + defaultUrl);
        logger.info("(AppInfoHolder::correctUrl) profile=" + profile);
        logger.info("(AppInfoHolder::correctUrl) finalUrl=" + finalUrl);

        return (finalUrl.length() <= 15) ? defaultUrl : finalUrl;
    }

}
