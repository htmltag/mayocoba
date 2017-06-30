#!groovy

node {
    checkout scm
    env.PATH = "${tool 'M3'}/bin:${env.PATH}"
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
        version = getVersion()
        try {
          def newImage = buildImage(version)
          echo "newImage: ${newImage.id}"
        } catch(err) {
          echo "${err}"
        }
      }
    }

/*
    stage ('Build image') {
        try {
              docker.build("festsentralen/mayocoba")
            } catch (error) {

            } finally {

            }
    }

    stage('Build and push image') {
        try {
            registry_url = 'https://index.docker.io/v1/'
            docker_creds_id = 'dockerhub'
            docker.withRegistry(registry_url, docker_creds_id).build("festsentralen/mayocoba:${env.BUILD_NUMBER}").push()
            } catch (error) {

            }
    }
    /*
}


def buildImage(version) {
    def image
  stage("Build Image") {
    if (!version) {
      error 'No version to build'
    }
    image = docker.build("festsentralen/mayocoba")
  }
  return image
}

String getVersion() {
      def latestTag = ''
      try {
        latestTag = sh script: "git describe --tags", returnStdout: true
      } catch(err) {
        return latestTag.trim()
      }
      return latestTag.trim()
    }