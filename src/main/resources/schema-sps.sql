-- /******************************************************************************/
-- /*                            ─ zupportL5 ─                                   */
-- /* Author.:  abritoh                                                          */
-- /* Date...:    2024-11-05                                                     */
-- /* Description:                                                               */
-- /*   Place here any SQL-script for Database definition to be executed by JPA  */
-- /*   after entity-creation completed but before data.sql script-execution.    */
-- /* File...: schema-sps.sql                                                    */
-- /*                                                                            */
-- /* GitHub.: https://github.com/abritoh/zupportL5                              */
-- /* Email..: arcbrth@gmail.com                                                 */
-- /*                                                                            */
-- /******************************************************************************/


-- ******************************************
-- 5. Create Stored Procedures
-- ******************************************
DROP PROCEDURE IF EXISTS sp_get_document;
DROP PROCEDURE IF EXISTS sp_get_documents;
DROP PROCEDURE IF EXISTS sp_search_documents;
DROP PROCEDURE IF EXISTS sp_insert_document;

-- ----------------------------------
-- sp_get_document
-- ----------------------------------
DELIMITER $$
CREATE PROCEDURE sp_get_document(IN idsource VARCHAR(255))
BEGIN
    DECLARE docType CHAR(2);
    DECLARE docId INT;

    SET docType = LEFT(idsource, 2);
    SET docId = CAST(SUBSTRING(idsource, 3) AS UNSIGNED);

    IF docType = 'KB' THEN
        SELECT 
            'KB' AS document_type,
            category,
            xmlheader,
            xmlcontent,
            created_at,
            CONCAT('KB', id) AS idsource
        FROM knowledgebase
        WHERE id = docId;

    ELSEIF docType = 'PB' THEN
        SELECT 
            'PB' AS document_type,
            category,
            xmlheader,
            xmlcontent,
            created_at,
            CONCAT('PB', id) AS idsource
        FROM playbook
        WHERE id = docId;

    ELSEIF docType = 'RB' THEN
        SELECT 
            'RB' AS document_type,
            category,
            xmlheader,
            xmlcontent,
            created_at,
            CONCAT('RB', id) AS idsource
        FROM runbook
        WHERE id = docId;

    ELSE
        SELECT 'Invalid idsource format or unsupported document type' AS error_message;
    END IF;
END $$
DELIMITER ;


-- ----------------------------------
-- sp_search_documents
-- ----------------------------------
DELIMITER $$
CREATE PROCEDURE sp_search_documents(IN keyword VARCHAR(255))
BEGIN
    DROP TEMPORARY TABLE IF EXISTS tmp_results;
    CREATE TEMPORARY TABLE tmp_results (
        document_type VARCHAR(20),
        category VARCHAR(255),
        xmlheader VARCHAR(3000),
        xmlcontent MEDIUMTEXT,
        created_at DATETIME,
        idsource VARCHAR(255)
    );

    INSERT INTO tmp_results (document_type, category, xmlheader, xmlcontent, created_at, idsource)
    SELECT 
        'KB' AS document_type,
        category,
        xmlheader,
        xmlcontent,
        created_at,
        CONCAT('KB', id) AS idsource
    FROM knowledgebase
    WHERE category LIKE CONCAT('%', keyword, '%')
       OR xmlheader LIKE CONCAT('%', keyword, '%')
       OR xmlcontent LIKE CONCAT('%', keyword, '%');

    INSERT INTO tmp_results (document_type, category, xmlheader, xmlcontent, created_at, idsource)
    SELECT 
        'PB' AS document_type,
        category,
        xmlheader,
        xmlcontent,
        created_at,
        CONCAT('PB', id) AS idsource
    FROM playbook
    WHERE category LIKE CONCAT('%', keyword, '%')
       OR xmlheader LIKE CONCAT('%', keyword, '%')
       OR xmlcontent LIKE CONCAT('%', keyword, '%');

    INSERT INTO tmp_results (document_type, category, xmlheader, xmlcontent, created_at, idsource)
    SELECT 
        'RB' AS document_type,
        category,
        xmlheader,
        xmlcontent,
        created_at,
        CONCAT('RB', id) AS idsource
    FROM runbook
    WHERE category LIKE CONCAT('%', keyword, '%')
       OR xmlheader LIKE CONCAT('%', keyword, '%')
       OR xmlcontent LIKE CONCAT('%', keyword, '%');

    SELECT * FROM tmp_results;

    DROP TEMPORARY TABLE IF EXISTS tmp_results;
END $$


-- ----------------------------------
-- sp_get_documents;
-- ----------------------------------
DELIMITER $$
CREATE PROCEDURE sp_get_documents(IN limit_rows INT)
BEGIN
    DROP TEMPORARY TABLE IF EXISTS tmp_results;
    CREATE TEMPORARY TABLE tmp_results (
        document_type VARCHAR(20),
        category VARCHAR(255),
        xmlheader VARCHAR(3000),
        created_at DATETIME,
        idsource VARCHAR(255)
    );

    INSERT INTO tmp_results (document_type, category, xmlheader, created_at, idsource)
    SELECT 
        'KB' AS document_type,
        category,
        xmlheader,
        created_at,
        CONCAT('KB', id) AS idsource
    FROM knowledgebase;

    INSERT INTO tmp_results (document_type, category, xmlheader, created_at, idsource)
    SELECT 
        'PB' AS document_type,
        category,
        xmlheader,
        created_at,
        CONCAT('PB', id) AS idsource
    FROM playbook;

    INSERT INTO tmp_results (document_type, category, xmlheader, created_at, idsource)
    SELECT 
        'RB' AS document_type,
        category,
        xmlheader,
        created_at,
        CONCAT('RB', id) AS idsource
    FROM runbook;

    IF limit_rows = 0 THEN
        SELECT * FROM tmp_results ORDER BY created_at DESC;
    ELSE
        SELECT * FROM tmp_results ORDER BY created_at DESC LIMIT limit_rows;
    END IF;

    DROP TEMPORARY TABLE IF EXISTS tmp_results;
END $$
DELIMITER ;


-- ----------------------------------
-- sp_insert_document
-- ----------------------------------
DELIMITER $$
CREATE PROCEDURE sp_insert_document(
    IN document_type VARCHAR(100),
    IN category VARCHAR(1000),
    IN xmlheader VARCHAR(3000),
    IN xmlcontent MEDIUMTEXT,
    OUT status_code INT,
    OUT status_message VARCHAR(255)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1
            status_message = MESSAGE_TEXT;
        SET status_code = 500;
    END;

    IF document_type = 'KB' THEN
        INSERT INTO knowledgebase (category, xmlheader, xmlcontent) 
			VALUES (category, xmlheader, xmlcontent);
        SET status_code = 200;
        SET status_message = 'Success';
        
    ELSEIF document_type = 'PB' THEN
        INSERT INTO playbook (category, xmlheader, xmlcontent) 
			VALUES (category, xmlheader, xmlcontent);
        SET status_code = 200;
        SET status_message = 'Success';
        
    ELSEIF document_type = 'RB' THEN
        INSERT INTO runbook (category, xmlheader, xmlcontent) 
			VALUES (category, xmlheader, xmlcontent);
        SET status_code = 200;
        SET status_message = 'Success';
        
    ELSE
        SET status_code = 400;
        SET status_message = 'Invalid document document_type';
    END IF;
    
END $$
DELIMITER ;


