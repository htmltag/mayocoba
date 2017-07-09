podTemplate(label: 'greenland-jenkins-slave', containers: [
    containerTemplate(name: 'docker', image: 'docker:17.06-dind', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat')],
    volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')]
) {

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

    stage ('Package') {
        container('maven') {
            sh "mvn -DskipTests clean package"
        }
    }

    def pcImg
    container('docker') {
        stage ('Build'){
            try{
                withDockerRegistry(registry: [credentialsId: 'dockerhub']) {
                    pcImg = docker.build("festsentralen/mayacoba:${tag}")
                }
            }catch (e){
                throw e
            }

        }
    }

    stage ('Push'){
        container('docker') {
            withDockerRegistry(registry: [credentialsId: 'dockerhub']) {
                pcImg = docker.push('latest')
            }
        }
    }

}
}