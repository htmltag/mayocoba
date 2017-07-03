#!groovy

node {
    checkout scm
    env.PATH = "${tool 'M3'}/bin:${env.PATH}"
    env.DOCKER_API_VERSION="1.23"
    stage ('Initialize') {
            sh '''
                echo "PATH = ${PATH}"
                echo "M3_HOME = ${M3_HOME}"
            '''
    }

    stage ('Package') {
        sh 'mvn clean package -DskipTests'
    }

    stage ('Test') {
        try {
              sh 'mvn clean -Dmaven.test.failure.ignore=true install'
            } catch (error) {

            } finally {
              junit 'target/surefire-reports/**/*.xml'
            }
    }

    docker.withTool('docker') {
        try {
          buildImage()
          echo "newImage Yeah!"
        } catch(err) {
          echo "${err}"
        }
      }
}


def buildImage() {
  stage("Build Image") {
    sh 'docker build -t  festsentralen/mayocoba .'
  }
}