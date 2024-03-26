#!/usr/bin/env groovy

pipeline {
    agent any
    options {
        timestamps()
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '5'))
        skipDefaultCheckout true
    }
    environment {
        JAVA_TOOL_OPTIONS = ''
        MAVEN_OPTS = '-Dstyle.color=always -Djansi.passthrough=true'
        JAVA_HOME = "${env.JAVA_HOME_17}"
    }
    stages {
        stage('Init') {
            steps {
                script {
                    git branch: params.BASE_BRANCH, url: 'git@github.com:bonitasoft/bonita-project.git'
                }
            }

        }
        stage('Update version') {
            steps {
                script {
                    sh " git config --global push.default matching"
                    sh """
                        git config --global push.default matching
                        ./mvnw -ntp versions:set -DnewVersion=${params.newVersion} -Ptests
                        ./mvnw -ntp -f parent versions:set-property -Dproperty=branding.version -DnewVersion=${params.NEW_BRANDING_VERSION}
                        ./mvnw -ntp -f parent versions:set-property -Dproperty=bonita.runtime.version -DnewVersion=${params.newVersion}
                        git commit -a -m "chore(release): prepare next development version ${params.newVersion}"
                        git push
                    """
                }
            }
        }
    }
}
