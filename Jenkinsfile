pipeline {
    agent any
    options {
        timestamps()
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '3'))
    }
    environment {
        JAVA_HOME = "${env.JAVA_HOME_11}"
        JAVA_TOOL_OPTIONS = ''
        MAVEN_OPTS = '-Dstyle.color=always -Djansi.passthrough=true'
    }
    stages {
        stage('Build') {
            steps {
                configFileProvider([configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {
                    script {
                        try {
                            sh("./mvnw -s $MAVEN_SETTINGS --no-transfer-progress -B verify -Ptests")
                        } finally {
                            junit allowEmptyResults : true, testResults: 'tests/**/target/*-reports/TEST-*.xml'
                            archiveArtifacts artifacts: 'tests/target/it/**/*.log', allowEmptyArchive: true
                        }
                    }
                }
            }
        }
        stage('Deploy') {
            when {
                expression { return env.BRANCH_NAME == 'master' || env.BRANCH_NAME == 'dev' || env.BRANCH_NAME?.startsWith('release-') || env.BRANCH_NAME?.matches('9\\..+\\.x') }
            }
            steps {
                configFileProvider([configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {
                    sh("./mvnw -s $MAVEN_SETTINGS --no-transfer-progress -B deploy -Possrh")
                }
            }
        }
    }
}