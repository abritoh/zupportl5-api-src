package org.apache.clusterbr.zupportl5.config;

import javax.sql.DataSource;

import org.apache.clusterbr.zupportl5.utils.GCPSecretManagerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DataSourceConfig_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DataSourceConfig_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1122
* <!-- comment-processor-end -->
 */
@Configuration
public class DataSourceConfig {

    private static final String MY_SQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Lazy
    @Primary
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        logger.info("(newHikariDataSource) executing... *****************");

        String dbUrl = System.getenv("DATASOURCE_URL");
        String dbUsername = System.getenv("DATASOURCE_USERNAME");
        String dbPassword = System.getenv("DATASOURCE_PSW");

        if (dbUrl == null || dbUsername == null || dbPassword == null) {
            logger.error("(newHikariDataSource) Database connection details are not set in environment variables.");
        }

        if (dbUrl.startsWith("projects") ) {
            logger.info("(newHikariDataSource) Using GCP SDK to read secrets from secret manager.");
            dbUrl = GCPSecretManagerUtil.getSecret(dbUrl); 
            dbUsername = GCPSecretManagerUtil.getSecret(dbUsername); 
            dbPassword = GCPSecretManagerUtil.getSecret(dbPassword);
        }

        logger.info("(DataSourceConfig::newHikariDataSource) dbUrl: " + dbUrl);
        logger.info("(DataSourceConfig::newHikariDataSource) dbUsername: " + dbUsername);

        config.setDriverClassName(MY_SQL_DRIVER);
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.setMaximumPoolSize(50);
        config.setMinimumIdle(10);
         //-- milliseconds (5 minutes)
        config.setIdleTimeout(300000); 

        return new HikariDataSource(config);
    }

    @Lazy
    @Primary
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public JdbcTemplate jdbcTemplate(DataSource dataSource) { 
        return new JdbcTemplate(dataSource); 
    } 
        
    @Lazy
    @Primary
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) { 
        return new NamedParameterJdbcTemplate(dataSource); 
    }

}
