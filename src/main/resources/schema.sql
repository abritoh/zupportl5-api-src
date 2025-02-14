-- /******************************************************************************/
-- /*                            ─ zupportL5 ─                                   */
-- /* Author.:  abritoh                                                          */
-- /* Date...:    2024-11-05                                                     */
-- /* Description:                                                               */
-- /*   Place here any SQL-script for Database definition to be executed by JPA  */
-- /*   after entity-creation completed but before data.sql script-execution.    */
-- /* File...: schema.sql                                                        */
-- /*                                                                            */
-- /* GitHub.: https://github.com/abritoh/zupportL5                              */
-- /* Email..: arcbrth@gmail.com                                                 */
-- /*                                                                            */
-- /******************************************************************************/


-- ******************************************
-- 1. Drop Tables if they exist 
-- ******************************************

-- SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS incidentlogs;
DROP TABLE IF EXISTS performance_metrics;
DROP TABLE IF EXISTS resolution_step;
DROP TABLE IF EXISTS handover;
DROP TABLE IF EXISTS engineer;
DROP TABLE IF EXISTS incident;
DROP TABLE IF EXISTS knowledgebase;
DROP TABLE IF EXISTS playbook;
DROP TABLE IF EXISTS runbook;
DROP TABLE IF EXISTS team;
DROP TABLE IF EXISTS priority;
DROP TABLE IF EXISTS status;
DROP TABLE IF EXISTS dropbox_auth_state;
DROP TABLE IF EXISTS settings;
-- SET FOREIGN_KEY_CHECKS = 1;



-- ******************************************
-- 2. Create Catalog Tables
-- ******************************************

CREATE TABLE settings (
    id_key VARCHAR(200) PRIMARY KEY,
    value VARCHAR(2600),
    label VARCHAR(600) NULL,
    expires_at_long BIGINT NULL,
    expires_at_time TIMESTAMP NULL,    
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE dropbox_auth_state (
    request_id VARCHAR(255) PRIMARY KEY,
    state VARCHAR(255) NOT NULL,
    client_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE status (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE priority (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE team (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(200)
);



-- ******************************************
-- 3. Create Transactional Tables
-- ******************************************

CREATE TABLE knowledgebase (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(1000) NOT NULL,
    xmlheader VARCHAR(3000) NOT NULL,
    xmlcontent MEDIUMTEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE playbook (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(1000) NOT NULL,
    xmlheader VARCHAR(3000) NOT NULL,
    xmlcontent MEDIUMTEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE runbook (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(1000) NOT NULL,
    xmlheader VARCHAR(3000) NOT NULL,
    xmlcontent MEDIUMTEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE engineer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(100) NOT NULL,
    team_id INT,
    FOREIGN KEY (team_id) REFERENCES team(id)
);

CREATE TABLE incident (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status_id INT,
    priority_id INT,
    team_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL DEFAULT NULL,
    FOREIGN KEY (status_id) REFERENCES status(id),
    FOREIGN KEY (priority_id) REFERENCES priority(id),
    FOREIGN KEY (team_id) REFERENCES team(id)
);

CREATE TABLE handover (
    id INT AUTO_INCREMENT PRIMARY KEY,
    incident_id INT,
    from_engineer_id INT,
    to_engineer_id INT,
    handover_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,
    FOREIGN KEY (incident_id) REFERENCES incident(id),
    FOREIGN KEY (from_engineer_id) REFERENCES engineer(id),
    FOREIGN KEY (to_engineer_id) REFERENCES engineer(id)
);

CREATE TABLE resolution_step (
    id INT AUTO_INCREMENT PRIMARY KEY,
    incident_id INT,
    step_number INT,
    description MEDIUMTEXT NOT NULL,
    engineer_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (incident_id) REFERENCES incident(id),
    FOREIGN KEY (engineer_id) REFERENCES engineer(id)
);

CREATE TABLE performance_metrics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    incident_id INT,
    resolution_time INT,  -- Resolution time in minutes
    response_time INT,  -- Response time in minutes
    -- * -- User satisfaction score (1 to 5) :::
    -- *       1-Unsatisfactory, 2-Fair
    -- *       3-Average, 4-Good, 5-Excellent
    satisfaction_score INT,  
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (incident_id) REFERENCES incident(id),
    CONSTRAINT CHK_satisf_score CHECK (satisfaction_score>=1 AND satisfaction_score<=5)
);

CREATE TABLE incidentlogs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    incident_id INT,
    engineer_id INT,
    action VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (incident_id) REFERENCES incident(id),
    FOREIGN KEY (engineer_id) REFERENCES engineer(id)
);


-- ******************************************
-- 4. Create Views
-- ******************************************

-- 
-- view_incident_details
-- 
DROP VIEW IF EXISTS view_incident_details;
CREATE VIEW view_incident_details as 
SELECT i.id
    ,title
    ,i.description
    ,CASE WHEN i.status_id IS NULL THEN 0 ELSE i.status_id END AS status_id
    ,CASE WHEN s.name IS NULL THEN "***" ELSE s.name END AS status_name
    ,CASE WHEN i.priority_id IS NULL THEN 0 ELSE i.priority_id END AS priority_id
    ,CASE WHEN p.name IS NULL THEN "***" ELSE p.name END AS priority_name
    ,CASE WHEN i.team_id IS NULL THEN 0 ELSE i.team_id END AS team_id 
    ,CASE WHEN t.name IS NULL THEN "***" ELSE t.name END AS team_name
    ,created_at
    ,updated_at
    ,resolved_at
FROM incident i 
LEFT JOIN status s ON i.status_id = s.id
LEFT JOIN priority p ON i.priority_id = p.id
LEFT JOIN team t ON i.team_id = t.id
;

--
-- view_handover_details
-- 
DROP VIEW IF EXISTS view_handover_details;
CREATE VIEW view_handover_details as 
SELECT h.id
	,CASE WHEN i.title IS NULL THEN "***" ELSE i.title END AS incident_title
    ,CASE WHEN i.description IS NULL THEN "***" ELSE i.description END AS incident_description
    ,CASE WHEN s.name IS NULL THEN "***" ELSE s.name END AS status_name
    ,CASE WHEN p.name IS NULL THEN "***" ELSE p.name END AS priority_name
    ,CASE WHEN e1.first_name IS NULL THEN "***" ELSE concat(e1.first_name,' ',e1.last_name) END AS from_engineer_name
    ,CASE WHEN e2.first_name IS NULL THEN "***" ELSE concat(e2.first_name,' ',e2.last_name) END AS to_engineer_name
    ,handover_time
    ,notes
    ,CASE WHEN e1.email IS NULL THEN "***" ELSE e1.email END AS from_engineer_email    
    ,CASE WHEN e2.email IS NULL THEN "***" ELSE e2.email END AS to_engineer_email
FROM handover h
LEFT JOIN incident i ON h.incident_id = i.id
LEFT JOIN engineer e1 ON h.from_engineer_id = e1.id
LEFT JOIN engineer e2 ON h.to_engineer_id = e2.id
LEFT JOIN status s ON i.status_id = s.id
LEFT JOIN priority p ON i.priority_id = p.id
;

