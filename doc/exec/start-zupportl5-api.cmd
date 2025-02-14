REM ------------------------------------
REM 2024-1203
REM arcebrito@gmail.com
REM ------------------------------------

@echo on

REM Local-Host
set DATASOURCE_PLATFORM=mysql
set DATASOURCE_URL=jdbc:mysql://localhost:3306/zupportL5_db
set DATASOURCE_USERNAME=
set DATASOURCE_PSW=
set SPRING_PROFILES_ACTIVE=dev

echo %DATASOURCE_PLATFORM%
echo %DATASOURCE_URL%
echo %DATASOURCE_USERNAME%
echo %DATASOURCE_PSW%
echo %SPRING_PROFILES_ACTIVE%

set ZUPPORTL5_API_JAR="C:\ClusterBR\Localhost\2025\Dev\Java\zupportl5-api-src\target\zupportL5-api-1.0.jar"
echo %ZUPPORTL5_API_JAR%
java -jar %ZUPPORTL5_API_JAR%

