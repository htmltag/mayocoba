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
        try {
          def newImage = buildImage()
          echo "newImage: ${newImage.id}"
        } catch(err) {
          echo "${err}"
        }
      }
}


def buildImage() {
    def image
  stage("Build Image") {
    image = docker.build("festsentralen/mayocoba")
  }
  return image
}