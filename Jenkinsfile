pipeline {
    agent any

    environment {
        IMAGE_NAME = 'gimnasio-sport'
        IMAGE_TAG = 'latest'
        REGISTRY = 'tuusuario/dockerhub'   // o el nombre de tu registry interno
    }
  
    stages {
        stage('Clonar código') {
            steps {
                git branch: 'main', url: 'https://github.com/AriC001/IS2-2025.git'
            }
        }
      
        stage('Compilar y Testear') {
            steps {
                dir('Proyecto-Gym/Proyecto') {
                  echo 'Ejecutando build y test unitarios...'
                  sh 'mvn clean test'
                }
            }
        }

        stage('Construir imagen Docker') {
            steps {
              dir('Proyecto-Gym/Proyecto') {
                  echo 'Construyendo imagen Docker...'
                  sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }
        stage('Probar contenedor') {
            steps {
                echo 'Levantando contenedor para verificación...'
                sh "docker run -d --name test_app -p 8090:8090 ${IMAGE_NAME}:${IMAGE_TAG}"
                // Espera 10 segundos para ver si inicia correctamente
                sh "sleep 10"
                // Podés verificar el endpoint de salud
                sh "curl -f http://localhost:8090/actuator/health || echo 'Health check falló'"
                sh "docker stop test_app && docker rm test_app"
            }
        }

        stage('Publicar imagen') {
            steps {
                echo 'Publicando imagen en DockerHub...'
                withCredentials([usernamePassword(credentialsId: 'dockerhub-cred', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                    sh "echo $PASS | docker login -u $USER --password-stdin"
                    sh "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}"
                    sh "docker push ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('Desplegar en servidor') {
            steps {
                echo 'Desplegando nueva versión...'
                // Ejemplo si usás docker-compose remoto:
                // sh 'ssh usuario@192.168.1.100 "cd /opt/gimnasio && docker-compose pull && docker-compose up -d"'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline completado correctamente.'
        }
        failure {
            echo '❌ Error en el pipeline.'
        }
    }
}
