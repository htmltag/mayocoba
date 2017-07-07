podTemplate(label: 'greenland-jenkins-slave', containers: [
    containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat')
]) {

node ('greenland-jenkins-slave'){

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

    container('maven') {
        stage ('Package') {
                    sh "mvn -DskipTests clean package"
                }


        stage ('Test') {
            try {
                  sh 'mvn -Dmaven.test.failure.ignore=true'
                } catch (error) {

                } finally {
                  junit 'target/surefire-reports/**/*.xml'
                }
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
}