node {
    def app

    stage ('Initialize') {
        sh '''
            echo "PATH = ${PATH}"
            echo "M3_HOME = ${M3_HOME}"
        '''
    }

    stage ('Build') {
        mvn '-Dmaven.test.failure.ignore=true install'
        junit 'target/surefire-reports/**/*.xml'
    }

    stage('Build image') {
        app = docker.build('festsentralen/mayocoba')
    }

    stage('Push image') {
        pom = readMavenPom file: 'pom.xml'
        registry_url = 'https://index.docker.io/v1/'
        docker_creds_id = 'dockerhub'
        docker.withRegistry(registry_url, docker_creds_id) {
            app.push('${pom.version}')
            app.push('latest')
        }

    }
}