-- /******************************************************************************/
-- /*                            ─ zupportL5 ─                                   */
-- /* Author.:  abritoh                                                          */
-- /* Date...:    2024-11-05                                                     */
-- /* Description:                                                               */
-- /*   zupportL5 SQL-scripts.                                                   */
-- /* File...: data.sql                                                          */
-- /*                                                                            */
-- /* GitHub.: https://github.com/abritoh/zupportL5                              */
-- /* Email..: arcbrth@gmail.com                                                 */
-- /*                                                                            */
-- /******************************************************************************/


-- ******************************************
-- A. Load Data
-- ******************************************

-- ------------------------------------------
-- A.1 Truncate Tables
-- ------------------------------------------

-- SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM settings; COMMIT;
DELETE FROM incidentlogs; COMMIT;
DELETE FROM performance_metrics; COMMIT;
DELETE FROM resolution_step; COMMIT;
DELETE FROM knowledgebase; COMMIT;
DELETE FROM runbook; COMMIT;
DELETE FROM handover; COMMIT;
DELETE FROM incident; COMMIT;
DELETE FROM engineer; COMMIT;
DELETE FROM team; COMMIT;
DELETE FROM priority; COMMIT;
DELETE FROM status; COMMIT;

ALTER TABLE incidentlogs AUTO_INCREMENT = 1;
ALTER TABLE performance_metrics AUTO_INCREMENT = 1;
ALTER TABLE resolution_step AUTO_INCREMENT = 1;
ALTER TABLE knowledgebase AUTO_INCREMENT = 1;
ALTER TABLE runbook AUTO_INCREMENT = 1;
ALTER TABLE handover AUTO_INCREMENT = 1;
ALTER TABLE incident AUTO_INCREMENT = 1;
ALTER TABLE engineer AUTO_INCREMENT = 1;
ALTER TABLE team AUTO_INCREMENT = 1;
ALTER TABLE priority AUTO_INCREMENT = 1;
ALTER TABLE status AUTO_INCREMENT = 1;

-- SET FOREIGN_KEY_CHECKS = 1;


-- ------------------------------------------
-- A.2 Catalog Tables
-- ------------------------------------------

INSERT INTO settings (id_key, value, expires_at_long, expires_at_time, last_update) VALUES ('DROPBOX_CLIENT_ID', '<NO_VALUE>', NULL, NULL, NOW());
INSERT INTO settings (id_key, value, expires_at_long, expires_at_time, last_update) VALUES ('DROPBOX_CLIENT_SECRET', '<NO_VALUE>', NULL, NULL, NOW());
UPDATE settings SET value = 'ynakgz3ugz49a8l', last_update = NOW(), expires_at_long=NULL, expires_at_time=NULL WHERE id_key = 'DROPBOX_CLIENT_ID';
UPDATE settings SET value = 'vq4ramdu0f15vop', last_update = NOW(), expires_at_long=NULL, expires_at_time=NULL WHERE id_key = 'DROPBOX_CLIENT_SECRET';

INSERT INTO status (name) VALUES
('open'),
('in progress'),
('resolved'),
('closed'),
('pending');

INSERT INTO priority (name) VALUES
('low'),
('medium'),
('high'),
('critical'),
('urgent');

INSERT INTO team (name, description) VALUES
('Networking Team', 'Responsible for network-related incidents and issues'),
('Database Team', 'Handles database-related issues and incidents'),
('System Team', 'Manages system infrastructure and hardware'),
('Support Team', 'Provides first-line support to customers'),
('DevOps Team', 'Handles deployment and operational tasks');

COMMIT;

-- ------------------------------------------
-- A.3 Transactional Tables
-- ------------------------------------------

INSERT INTO engineer (first_name, last_name, email, role, team_id) VALUES
('Monsi', 'Tatham', 'monsi.tatham@zupportl5.org', 'Network Engineer', 1),
('Wayni', 'Ratliff', 'wayni.ratliff@zupportl5.org', 'Database Engineer', 2),
('Drew', 'Tanenbaum', 'drew.tanenbaum@zupportl5.org', 'System Administrator', 3),
('Divad', 'Axmark', 'Divad.axmark@zupportl5.org', 'DevOps Engineer', 5),
('Bob', 'Ross', 'bob.ross@zupportl5.org', 'Support Engineer', 4);
COMMIT;

INSERT INTO incident (title, description, status_id, priority_id, team_id) VALUES
('Network issue', 'Network latency problem in data center', 1, 3, 1),
('Database outage', 'Critical database service is down', 2, 4, 2),
('Server crash', 'A server crashed unexpectedly', 2, 5, 3),
('Slow performance', 'App is running slower than usual', 1, 2, 4),
('Login failure', 'Users cannot log in to the system', 3, 1, 5);
COMMIT;

INSERT INTO handover (incident_id, from_engineer_id, to_engineer_id, notes) VALUES
(1, 1, 2, 'Assigned to DB team for further investigation ─ sql optimization'),
(2, 2, 3, 'Handover to system team for infrastructure check ─ storage issue'),
(3, 3, 4, 'Transferred to DevOps for pipeline-review ─ wrong jar dependency'),
(4, 4, 1, 'Transferred back to network engineer ─ DNS failure'),
(5, 5, 2, 'Handover to DB engineer ─ deadlock issues');
COMMIT;

INSERT INTO knowledgebase (category, xmlheader, xmlcontent) VALUES
('IT Operations|Troubleshooting|Networking|Network Troubleshooting','<title>Network Troubleshooting Knowledge Base</title>','<steps><step>1. Verify physical connections to ensure cables are properly connected.</step><step>2. Check the status of network devices (routers, switches) for errors.</step><step>3. Use the ping command to test connectivity between devices.</step><step>4. Analyze routing tables to ensure correct network paths.</step><step>5. Run a traceroute to detect where the connection fails.</step><step>6. Examine firewall settings to ensure traffic is not being blocked.</step><step>7. Check for DNS resolution issues by using nslookup or dig commands.</step><step>8. Test network speed using tools like iperf to identify bottlenecks.</step></steps>')
,('IT Operations|Troubleshooting|Database|Database Troubleshooting','<title>Database Connection Issue Knowledge Base</title>','<steps><step>1. Check database server availability by pinging the host.</step><step>2. Review database logs for errors or timeouts.</step><step>3. Verify connection credentials such as username and password.</step><step>4. Ensure that the database is listening on the correct port.</step><step>5. Validate firewall and security group settings for the database.</step><step>6. Check for maximum connection limits or resource exhaustion.</step><step>7. Confirm that database user permissions are correctly configured.</step><step>8. Restart the database service if necessary and retest the connection.</step></steps>')
,('IT Operations|Performance|Application|Application Performance','<title>Application Performance Tuning Knowledge Base</title>','<steps><step>1. Identify slow-performing transactions using profiling tools.</step><step>2. Review application logs for performance bottlenecks.</step><step>3. Optimize queries by indexing frequently accessed columns.</step><step>4. Improve memory management by tuning the JVM heap size.</step><step>5. Use caching mechanisms to reduce database load.</step><step>6. Profile code execution and refactor inefficient methods.</step><step>7. Monitor the system after making changes to confirm performance improvements.</step><step>8. Document all optimizations and their impact on performance.</step></steps>')
,('IT Security|Security|Risk Management|Security','<title>Security Breach Response Knowledge Base</title>','<steps><step>1. Verify the authenticity of the security breach.</step><step>2. Contain the breach by isolating affected systems.</step><step>3. Notify key stakeholders and security teams about the breach.</step><step>4. Analyze the logs to understand the method of attack.</step><step>5. Apply patches or mitigation measures to vulnerable systems.</step><step>6. Recover compromised data and systems from secure backups.</step><step>7. Conduct a post-mortem analysis to understand the cause of the breach.</step><step>8. Update security policies and procedures to prevent future breaches.</step></steps>')
,('IT Continuity|Recovery|Business Continuity|Disaster Recovery','<title>Disaster Recovery Knowledge Base</title>','<steps><step>1. Verify that backup systems are available and up-to-date.</step><step>2. Review the disaster recovery plan to ensure it is current.</step><step>3. Identify critical systems and services to recover first.</step><step>4. Initiate failover to a disaster recovery site if necessary.</step><step>5. Perform recovery procedures for servers, databases, and applications.</step><step>6. Test the recovered systems to ensure functionality.</step><step>7. Notify users and stakeholders when systems are back online.</step><step>8. Document the recovery process and update the disaster recovery plan.</step></steps>')
;
COMMIT;

INSERT INTO playbook (category, xmlheader, xmlcontent) VALUES 
('IT Incident Management|Response|Incident|Incident Response','<title>Network Outage Response Playbook</title>','<steps><step>1. Identify affected areas by checking network monitoring tools.</step><step>2. Assess the scope of the outage, including internal and external services.</step><step>3. Check router and switch configurations for issues.</step><step>4. Contact upstream providers if the issue is external.</step><step>5. Begin troubleshooting using basic network commands like ping, traceroute.</step><step>6. Isolate the root cause and implement the necessary fixes.</step><step>7. Test the network after changes to ensure full functionality.</step><step>8. Notify users about the resolution and monitor the system.</step></steps>')
,('IT Database|Management|Database|Database Management','<title>Database Failover Playbook</title>','<steps><step>1. Monitor database failover events using database management tools.</step><step>2. Verify the health of the standby server.</step><step>3. If failover occurs, manually switch to the standby database server.</step><step>4. Update configuration files to reflect the new primary database.</step><step>5. Perform application testing to ensure data integrity and application connectivity.</step><step>6. Contact the database team for investigation and root cause analysis.</step><step>7. Revert the failover procedure if necessary.</step><step>8. Document the failover event and steps taken.</step></steps>')
,('IT Performance|Performance|Application|Application Performance','<title>Application Performance Tuning Playbook</title>','<steps><step>1. Identify slow-performing applications using performance monitoring tools.</step><step>2. Review application logs to spot areas of concern.</step><step>3. Profile the application to identify bottlenecks in the code.</step><step>4. Optimize database queries and indexes.</step><step>5. Review server resource utilization (CPU, memory, disk I/O).</step><step>6. Implement code optimizations, such as caching or load balancing.</step><step>7. Test the changes in a staging environment.</step><step>8. Deploy the optimizations to production and monitor the results.</step></steps>')
,('IT Security|Security|Risk Management|Security','<title>Application Performance Tuning Playbook</title>','<steps><step>1. Identify slow-performing applications using performance monitoring tools.</step><step>2. Review application logs to spot areas of concern.</step><step>3. Profile the application to identify bottlenecks in the code.</step><step>4. Optimize database queries and indexes.</step><step>5. Review server resource utilization (CPU, memory, disk I/O).</step><step>6. Implement code optimizations, such as caching or load balancing.</step><step>7. Test the changes in a staging environment.</step><step>8. Deploy the optimizations to production and monitor the results.</step></steps>')
,('IT Continuity|Recovery|Business Continuity|Disaster Recovery','<title>Security Vulnerability Patch Playbook</title>','<steps><step>1. Review security vulnerability reports from external and internal sources.</step><step>2. Prioritize vulnerabilities based on their severity and impact.</step><step>3. Coordinate with the development and IT operations teams for patch deployment.</step><step>4. Test the patch in a staging environment before production deployment.</step><step>5. Apply patches during scheduled maintenance windows to minimize disruption.</step><step>6. Monitor the system after patching for any regressions or issues.</step><step>7. Document all changes made during the patching process.</step><step>8. Conduct a post-patch review and follow-up with any remaining actions.</step></steps>')
;
COMMIT;

INSERT INTO runbook (category, xmlheader, xmlcontent) VALUES
('IT Incident Management|Management|Incident|Incident Management','<title>Incident Escalation Process Runbook</title>','<steps><step>1. Identify the severity of the incident based on predefined SLAs.</step><step>2. If the incident is critical, notify the appropriate support teams immediately.</step><step>3. If the incident is unresolved after the first response time, escalate to the next level of support.</step><step>4. Gather detailed information about the incident and provide it to the escalation team.</step><step>5. Continuously monitor the incident until it is resolved.</step><step>6. Ensure all incident actions are logged for post-incident review.</step><step>7. Review and close the incident after resolution, ensuring all steps are documented.</step></steps>')
,('IT Systems|Administration|System|System Administration','<title>Server Restart Runbook</title>','<steps><step>1. Verify the reason for restarting the server (e.g., resource exhaustion, patching, performance).</step><step>2. Notify users about the planned restart and possible service disruption.</step><step>3. Check for any ongoing tasks that need to be completed or saved before restarting.</step><step>4. Log into the server with administrator credentials.</step><step>5. Use the appropriate command to restart the server (e.g., reboot command on Linux, Restart-Service on Windows).</step><step>6. Monitor the server after the restart to ensure all services come back online.</step><step>7. Notify users when the server is back up and fully operational.</step><step>8. Log all actions taken for future reference and audits.</step></steps>')
,('IT Systems|Administration|System|System Administration','<title>Backup Verification Runbook</title>','<steps><step>1. Verify the last successful backup timestamp.</step><step>2. Check the storage medium to ensure backups are being saved properly (e.g., disk space, cloud storage).</step><step>3. Retrieve the most recent backup and test restoration on a test system.</step><step>4. Ensure that all required files, databases, and configurations are backed up.</step><step>5. Verify the integrity of the restored data.</step><step>6. Document any failures during the verification process and resolve issues immediately.</step><step>7. Notify stakeholders when the backup verification is complete and successful.</step><step>8. Ensure the backup verification process is run on a regular basis.</step></steps>')
,('IT Incident Management|Management|Incident|Incident Management','<title>Incident Recovery Runbook</title>','<steps><step>1. Confirm the incident’s impact and categorize the severity.</step><step>2. Gather all necessary logs and details about the incident.</step><step>3. Work with relevant teams (e.g., network, database, application) to isolate the root cause.</step><step>4. Apply the necessary fixes or workarounds to recover the service or system.</step><step>5. Once recovery is complete, test the affected services to ensure they are fully operational.</step><step>6. Notify affected users and stakeholders about the recovery.</step><step>7. Document the incident, actions taken, and the root cause for future reference.</step><step>8. Review and update the incident recovery procedures to improve response times in the future.</step></steps>')
,('IT Incident Management|Management|Incident|Incident Management','<title>Patch Deployment Runbook</title>','<steps><step>1. Verify the patch release notes and prerequisites.</step><step>2. Schedule the deployment during a maintenance window to minimize user impact.</step><step>3. Backup critical systems and data before starting the deployment.</step><step>4. Deploy the patch on a test environment first, ensuring no issues occur.</step><step>5. Deploy the patch on production systems, ensuring to monitor for any immediate failures.</step><step>6. Verify that all systems and services are functioning as expected post-patch deployment.</step><step>7. Communicate to users about the successful patch deployment and any required reboots.</step><step>8. Review the patch process and document any improvements for future deployments.</step></steps>')
;
COMMIT;

INSERT INTO resolution_step (incident_id, step_number, description, engineer_id) VALUES
(1, 1, 'Investigated network issues and verified equipment settings', 1),
(2, 1, 'Checked database logs and identified the failure', 2),
(3, 1, 'Rebooted the server and checked for hardware faults', 3),
(4, 1, 'Reconfigured the app server to improve performance', 4),
(5, 1, 'Verified DB access permissions and fixed authentication issues', 5);
COMMIT;

INSERT INTO performance_metrics (incident_id, resolution_time, response_time, satisfaction_score) VALUES
(1, 120, 30, 4),
(2, 180, 45, 5),
(3, 150, 60, 3),
(4, 100, 25, 4),
(5, 110, 35, 5);
COMMIT;

INSERT INTO incidentlogs (incident_id, engineer_id, action) VALUES
(1, 1, 'Initial diagnosis of network latency'),
(2, 2, 'Investigated database server performance'),
(3, 3, 'Restarted server to resolve crash'),
(4, 4, 'Optimized app performance on server'),
(5, 5, 'Troubleshooted login issues with DB team');
COMMIT;

