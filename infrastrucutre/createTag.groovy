#!/usr/bin/env groovy

pipeline {
    agent any
    options {
        timestamps()
        skipDefaultCheckout true
    }
    stages {
        stage('Init') {
            steps{
                script{
                  git branch: params.BASE_BRANCH, url: 'git@github.com:bonitasoft/bonita-project.git'
                }
            }

        }
        stage('Tag') {
            steps {
                script {
                    sh "./mvnw -ntp -f parent versions:set-property -Dproperty=bonita.runtime.version -DnewVersion=${params.TAG_NAME}"
                    sh "./mvnw -ntp -f parent versions:set-property -Dproperty=branding.version -DnewVersion=${params.NEW_BRANDING_VERSION}"
                    sh "./infrastructure/release.sh ${params.TAG_NAME} true"
                }
            }
        }
    }
}
