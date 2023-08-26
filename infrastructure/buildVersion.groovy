pipeline {
    agent any
    options {
        timestamps()
    }
    environment {
      JAVA_TOOL_OPTIONS = ''
      MAVEN_OPTS = '-Dstyle.color=always -Djansi.passthrough=true'
      GPG_PASSPHRASE = credentials('gpg-passphrase')
      BUILD_VERSION = sh(script: "./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
    }
    stages {
        // Do not deploy weekly builds on Central
        stage('Deploy to Maven Central') {
            when{
                expression { return !env.BUILD_VERSION.contains('W')}
            }
            steps {
                configFileProvider([configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {
                    sh("./mvnw --no-transfer-progress -B deploy -Dgpg.passphrase=$GPG_PASSPHRASE -Prelease,ossrh")
                }
            }
        }
        // Weekly builds only deployed on Artifactory
        stage('Deploy to Artifactory') {
            when{
                expression { return env.BUILD_VERSION.contains('W')}
            }
            steps {
                configFileProvider([configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {
                    sh("./mvnw --no-transfer-progress -B deploy -Dgpg.passphrase=$GPG_PASSPHRASE -Prelease -DaltDeploymentRepository=${env.ALT_DEPLOYMENT_REPOSITORY_STAGING}")
                }
            }
        }
    }
}
