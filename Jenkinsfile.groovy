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
        maintainer = "festsentralen"
        imageName = "${maintainer}${appName}:${tag}"

        stage ('Package') {
            container('maven') {
                sh "mvn clean package test"
                junit 'target/surefire-reports/**/*.xml'
            }
        }

        def doc
        container('docker') {
            stage ('Build'){
                try{
                    withDockerRegistry(registry: [credentialsId: 'dockerhub']) {
                        doc = docker.build("${maintainer}/${appName}:${tag}")
                        doc.push('latest')
                    }
                }catch (e){
                    throw e
                }

            }
        }
    }
}