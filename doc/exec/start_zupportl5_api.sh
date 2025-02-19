#!/bin/bash

# ------------------------------------
# 2025-0219
# arcebrito@gmail.com
# ------------------------------------

# Enable echo for debugging
set -x

# Local-Host
export DATASOURCE_PLATFORM=mysql
export DATASOURCE_URL="jdbc:mysql://localhost:3306/zupportL5_db"
export DATASOURCE_USERNAME=
export DATASOURCE_PSW=
export SPRING_PROFILES_ACTIVE=dev

echo $DATASOURCE_PLATFORM
echo $DATASOURCE_URL
echo $DATASOURCE_USERNAME
echo $DATASOURCE_PSW
echo $SPRING_PROFILES_ACTIVE

ZUPPORTL5_API_JAR="/home/<USER>/ClusterBR/.../zupportl5-api-src/target/zupportL5-api-1.0.jar"
echo $ZUPPORTL5_API_JAR
java -jar $ZUPPORTL5_API_JAR