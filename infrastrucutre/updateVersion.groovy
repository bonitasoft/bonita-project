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
        stage('Update version') {
            steps {
                script {
                    sh " git config --global push.default matching"
                    sh "./mvnw -ntp -f parent versions:set-property -Dproperty=bonita.runtime.version -DnewVersion=${params.newVersion}"
                    sh "./mvnw -ntp -f parent versions:set-property -Dproperty=branding.version -DnewVersion=${params.NEW_BRANDING_VERSION}"
                    sh "./infrastructure/release.sh ${params.newVersion} false"
                }
            }
        }
    }
}
