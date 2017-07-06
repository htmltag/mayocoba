node {

    checkout scm

    sh "git rev-parse --short HEAD > commit-id"

    tag = readFile('commit-id').replace("\n", "").replace("\r", "")
    appName = "mayocoba"
    registryHost = "docker.io/"
    imageName = "${registryHost}${appName}:${tag}"
    env.BUILDIMG=imageName

    env.DOCKER_API_VERSION="1.23"
    stage ('Initialize') {
            sh '''
                echo "PATH = ${PATH}"
                echo "M3_HOME = ${M3_HOME}"
            '''
    }

    stage ('Test') {
        try {
              sh 'mvn clean -Dmaven.test.failure.ignore=true install'
            } catch (e) {
                throw e
            } finally {
              junit 'target/surefire-reports/**/*.xml'
            }
    }


    stage ('Build'){
        try{
            withDockerRegistry(registry: [credentialsId: 'dockerhub']) {
              def image = docker.build("festsentralen/mayocoba:${tag}")
              image.push()
            }
        }catch(e){
            throw e
        }

    }

}