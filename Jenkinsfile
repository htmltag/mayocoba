pipeline {
    agent any
    tools {
        maven 'M3'
        jdk 'jdk8'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M3_HOME = ${M3_HOME}"
                '''
            }
        }

        stage ('Build') {
            steps {
                sh 'mvn -Dmaven.test.failure.ignore=true install'
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }

        stage('Docker-build') {
            steps {
                sh 'DOCKER_HOST=tcp://127.0.0.1:4243 mvn clean package docker:build'
            }
        }
    }
}