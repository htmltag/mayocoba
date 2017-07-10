podTemplate(label: 'greenland-jenkins-slave', containers: [
    containerTemplate(name: 'docker', image: 'docker:17.06-dind', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat')],
    volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')]
) {

    node ('greenland-jenkins-slave'){

        checkout scm

        sh "git rev-parse --short HEAD > commit-id"

        def tag = readFile('commit-id').replace("\n", "").replace("\r", "")
        def appName = "mayocoba"
        def maintainer = "festsentralen"
        def imageName = "${maintainer}/${appName}:${tag}"

        stage ('Initialize') {
            sh '''
                    echo "PATH = ${PATH}"
                    echo "M3_HOME = ${M3_HOME}"
                '''
        }

        stage ('Package') {
            container('maven') {
                sh "mvn clean package test"
            }
        }

        container('docker') {
            stage ('Build'){
                try{
                    withDockerRegistry(registry: [credentialsId: 'dockerhub']) {
                        sh "docker build -t ${imageName}  ."
                        def img = docker.image(imageName)
                        img.push()
                    }
                }catch (e){
                    throw e
                }

            }
        }
    }
}