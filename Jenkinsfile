// Version 2.0 Jenkinsfile
pipeline {
    agent any
    environment {
        // Variables comunes para todas las ramas
        JWT_SECRET = credentials('JWT_SECRET_TEMPLATE')
        MAIL_USERNAME = credentials('MAIL_USERNAME')
        MAIL_PASSWORD = credentials('MAIL_PASSWORD')
        SPRING_PROFILES_ACTIVE = credentials('SPRING_PROFILES_ACTIVE')
        PASS_CERT_AIPLATFORM_BACK = credentials('PASS_CERT_TEMPLATE_BACK')
        S3_ACCESS_KEY = credentials('S3_ACCESS_KEY')
        S3_SECRET_KEY = credentials('S3_SECRET_KEY')
        OPENAI_API_KEY = credentials('OPENAI_API_KEY_TEMPLATE')
    }

    parameters {
        choice(name: 'BRANCH', choices: ['prod', 'preprod'], description: 'Rama a desplegar')
    }

    stages {
        stage('Configuración por entorno') {
            steps {
                script {
                    // Configuración específica según la rama
                    if (params.BRANCH == 'prod') {
                        env.DEPLOY_ENV = 'production'
                        env.SERVER_IP = 'PLACEHOLDER_IP_PROD' // Cambiar por el IP real de producción
                        env.SSH_CREDENTIAL = 'credential-ssh-vps-template'
                        env.SPRING_PROFILES_ACTIVE = credentials('SPRING_PROFILES_ACTIVE_PROD')
                        env.DB_HOST = credentials('DB_HOST_TEMPLATE')
                        env.DB_PORT = credentials('DB_PORT_TEMPLATE')
                        env.DB_USER = credentials('DB_USER_TEMPLATE')
                        env.DB_PASS = credentials('DB_PASS_TEMPLATE')
                        env.EMAIL_RECIPIENTS = '${recipients_backend_template}'
                    } else if (params.BRANCH == 'preprod') {
                        env.DEPLOY_ENV = 'preprod'
                        env.SERVER_IP = 'PLACEHOLDER_IP_PREPROD' // Cambiar por el IP real de preproducción
                        env.SSH_CREDENTIAL = 'credential-ssh-vps-template-preprod'
                        env.SPRING_PROFILES_ACTIVE = credentials('SPRING_PROFILES_ACTIVE_PREPROD')
                        env.DB_HOST = credentials('DB_HOST_TEMPLATE_PREPROD')
                        env.DB_PORT = credentials('DB_PORT_TEMPLATE_PREPROD')
                        env.DB_USER = credentials('DB_USER_TEMPLATE_PREPROD')
                        env.DB_PASS = credentials('DB_PASS_TEMPLATE_PREPROD')
                        env.EMAIL_RECIPIENTS = '${recipients_backend_template_preprod}'
                    }

                    // Prefijo para la imagen según entorno
                    env.IMAGE_PREFIX = params.BRANCH == 'prod' ? 'back-template' : 'back-template-preprod'
                    env.CONTAINER_NAME = params.BRANCH == 'prod' ? 'backend' : 'backend-preprod'
                    env.DOCKER_COMPOSE_PATH = params.BRANCH == 'prod' ? '/home/ubuntu/backend_prod/docker-compose.yml' : '/home/ubuntu/backend_preprod/docker-compose.yml'
                }
            }
        }

        stage('Clonar Repositorio') {
            steps {
                dir ('backend-template') {
                    git branch: params.BRANCH, credentialsId: 'git_credentials_mmvr',
                        url: 'https://github.com/MetamedicsVR-org/back-ai-platform.git'
                }
            }
        }

        stage('Construir imagen de Docker') {
            steps {
                script {
                    docker.build("${env.IMAGE_PREFIX}:${env.BUILD_ID}",
                        "-f backend-template/Dockerfile \
                        --build-arg JWT_SECRET='${env.JWT_SECRET}' \
                        --build-arg DB_HOST='${env.DB_HOST}' \
                        --build-arg DB_PORT='${env.DB_PORT}' \
                        --build-arg DB_USER='${env.DB_USER}' \
                        --build-arg DB_PASS='${env.DB_PASS}' \
                        --build-arg MAIL_USERNAME='${env.MAIL_USERNAME}' \
                        --build-arg MAIL_PASSWORD='${env.MAIL_PASSWORD}' \
                        --build-arg SPRING_PROFILES_ACTIVE='${env.SPRING_PROFILES_ACTIVE}' \
                        --build-arg PASS_CERT_TEMPLATE_BACK='${env.PASS_CERT_TEMPLATE_BACK}' \
                        --build-arg S3_ACCESS_KEY='${env.S3_ACCESS_KEY}' \
                        --build-arg S3_SECRET_KEY='${env.S3_SECRET_KEY}' \
                        --build-arg OPENAI_API_KEY='${env.OPENAI_API_KEY}' \
                        .")
                }
            }
        }

        stage('Enviar imagen a servidor') {
            steps {
                script {
                    def dockerImageWithTag = "${env.IMAGE_PREFIX}:${env.BUILD_ID}"
                    env.DOCKER_IMAGE_NAME = dockerImageWithTag
                    sh "docker tag ${env.DOCKER_IMAGE_NAME} ${env.SERVER_IP}:5000/${env.DOCKER_IMAGE_NAME}"
                    sh "docker push ${env.SERVER_IP}:5000/${env.DOCKER_IMAGE_NAME}"
                    sh "docker rmi -f \$(docker images | grep '${env.IMAGE_PREFIX}' | awk '{print \$3}')"
                }
            }
        }

        stage('Desplegar en Contenedor') {
            steps {
                sshagent([env.SSH_CREDENTIAL]) {
                    sh "ssh ubuntu@${env.SERVER_IP} 'docker rm -f ${env.CONTAINER_NAME}'"
                    sh "ssh ubuntu@${env.SERVER_IP} 'docker pull localhost:5000/${env.DOCKER_IMAGE_NAME}'"
                    echo "Ejecutando contenedor con la nueva imagen en entorno ${env.DEPLOY_ENV}"
                    echo "DOCKER_IMAGE_NAME=${env.DOCKER_IMAGE_NAME}"
                    sh "ssh ubuntu@${env.SERVER_IP} 'DOCKER_IMAGE_NAME=${env.DOCKER_IMAGE_NAME} docker compose -f ${env.DOCKER_COMPOSE_PATH} up --force-recreate -d ${env.CONTAINER_NAME}'"
                }
            }
        }

        stage('Eliminar imagenes antiguas sin usar') {
            steps {
                sshagent([env.SSH_CREDENTIAL]) {
                    sh "ssh ubuntu@${env.SERVER_IP} 'docker system prune -a -f'"
                }
            }
        }

        stage('Verificar contenedor en ejecución') {
            steps {
                sshagent([env.SSH_CREDENTIAL]) {
                    sh "ssh ubuntu@${env.SERVER_IP} 'docker ps | grep ${env.CONTAINER_NAME}'"
                    sh "ssh ubuntu@${env.SERVER_IP} 'docker logs ${env.CONTAINER_NAME}'"
                }
            }
        }
    }

    post {
        always {
            script {
                if (currentBuild.currentResult == 'SUCCESS') {
                    emailext body: "SUCCESS: Job ${env.JOB_NAME} build #${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}\nTEMPLATE Backend (${env.DEPLOY_ENV}) Deployed Successfully!",
                        subject: "Jenkins Build SUCCESS: Job ${env.JOB_NAME} ${env.DEPLOY_ENV} Deploy AUTO Finalizado :-)",
                        to: "${env.EMAIL_RECIPIENTS}"
                } else {
                    emailext body: "FAILURE: Job ${env.JOB_NAME} build #${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}\nTEMPLATE Backend (${env.DEPLOY_ENV}) Deploy Failed!",
                        subject: "Jenkins Build FAILURE: Job ${env.JOB_NAME} ${env.DEPLOY_ENV} Deploy AUTO Fallido :-(",
                        to: "${env.EMAIL_RECIPIENTS}"
                }
            }
        }
    }
}