ARG BONITA_BASE_IMAGE=bonita:latest
FROM $BONITA_BASE_IMAGE

ARG BONITA_CUSTOM_APPLICATION_FOLDER="my-application"
ARG BONITA_WEB_INF_PATH="/opt/bonita/server/webapps/bonita/WEB-INF"
ARG ARTIFACT_FINAL_NAME
ARG BONITA_ENVIRONMENT=local

# Copy bonita application resources (e.g. .zip and .bconf) to the dedicated bundle folder
COPY --chown=bonita:bonita ${ARTIFACT_FINAL_NAME}-${BONITA_ENVIRONMENT}.* ${BONITA_WEB_INF_PATH}/classes/${BONITA_CUSTOM_APPLICATION_FOLDER}/

# Copy extra dependencies in webapp classpath
COPY --chown=bonita:bonita ./classpath/* ${BONITA_WEB_INF_PATH}/lib
