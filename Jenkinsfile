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

    stage ('Build image') {
        try {
              docker.build("festsentralen/mayocoba}")
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
}