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
        JAVA_HOME = "${env.JAVA_HOME_11}"
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
                   	sh """
						git checkout -B release/${params.TAG_NAME}
						./mvnw -ntp versions:set -DnewVersion=${params.TAG_NAME}
						./mvnw -ntp -f parent versions:set-property -Dproperty=bonita.runtime.version -DnewVersion=${params.TAG_NAME}
						./mvnw -ntp -f parent versions:set-property -Dproperty=branding.version -DnewVersion=${params.NEW_BRANDING_VERSION}
						git commit -a -m "release(${params.TAG_NAME}) create release ${params.TAG_NAME}"
					    git tag -a ${params.TAG_NAME} -m "Release ${params.TAG_NAME}"
					    git push origin ${params.TAG_NAME}:${params.TAG_NAME}
					"""
                }
            }
        }
    }
}
