# Execution steps

1. Install: `git`, `jdk 17++`, `maven`, `vscode`, `mysql8++` 
2. Download the src-code from this GitHub repository
3. Create a database `zupportL5_db`, and execute the sql-scripts: `schema.sql`, `schema-sps.sql`, `schema-sb-5.1.2.sql`, `data.sql`
4. Modify the file `start-zupportl5-api.cmd` to set database user-name (e.g. admin) and psw and set the appropriate path of `zupportL5-api-1.0.jar` (this jar will be created during compilation ─step-5)
5. Compile the project via command line using: `mvn clean install` 
(for just compile, or package use: `mvn clean compile`, `mvn clean package`).
6. Modify the path and execute the batch file `start-zupportl5-api.cmd` (for windows), in case of linux use the corresponding bash file (`start_zupportl5_api.sh`).
7. By default the app will run in port (`1000`): http://localhost:10000




