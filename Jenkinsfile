pipeline {
    agent any

    environment {
        DOCKERHUB_USER = "ehshanulla"
        VERSION = "v1-${env.BUILD_NUMBER}"
    }

    stages {

        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Maven Build (Skip Tests)') {
            steps {
                script {
                    def repos = [
                        "Eureka",
                        "API-Gateway",
                        "AccountMicroService",
                        "TransactionsMicroService",
                        "NotificationMicroService"
                    ]

                    repos.each { folder ->
                        bat """
                        cd ${folder}
                        mvn clean package -DskipTests
                        cd ..
                        """
                    }
                }
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-creds',
                    usernameVariable: 'USER',
                    passwordVariable: 'PASS'
                )]) {
                    bat """echo %PASS% | docker login -u %USER% --password-stdin"""
                }
            }
        }

        stage('Update .env') {
            steps {
                writeFile file: '.env', text: "VERSION=${VERSION}"
            }
        }

        stage('Build Images with Docker Compose') {
            steps {
                bat "docker compose build"
            }
        }

        stage('Push Images to DockerHub') {
            steps {
                script {
                    def images = [
                        "bank-eureka-server",
                        "bank-api-gateway",
                        "bank-account-micro-service",
                        "bank-transactions-micro-service",
                        "bank-notification-micro-service" // fixed typo
                    ]

                    images.each { img ->
                        def imageFull = "${DOCKERHUB_USER}/${img}:${VERSION}"
                        bat "docker push ${imageFull}"
                    }

                    bat "docker logout"
                }
            }
        }
    }

    post {
        success {
            echo "All images built & pushed successfully!"
        }
    }
}
