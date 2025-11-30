pipeline {
    agent any
    
    environment {
        DOCKERHUB_USER = "ehshanulla"
        VERSION = "v1-${env.BUILD_NUMBER}"
    }

    stages {

        stage('Update .env Version') {
            steps {
                writeFile file: '.env', text: "VERSION=${VERSION}"
            }
        }

        stage('Push Images') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-creds',
                    usernameVariable: 'USER',
                    passwordVariable: 'PASS'
                )]) {
                    script {
                        bat """echo %PASS% | docker login -u %USER% --password-stdin"""

                        def images = [
                            "bank-eureka-server",
                            "bank-api-gateway",
                            "bank-account-micro-service",
                            "bank-transactions-micro-service",
                            "bank-notificaion-micro-service"
                        ]

                        images.each { name ->
                            def imageFull = "${DOCKERHUB_USER}/${name}:${VERSION}"
                            bat "docker push ${imageFull} || echo Image already exists"
                        }

                        bat "docker logout"
                    }
                }
            }
        }
    }
}
