pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    stages {

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                        sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=springboot-demo \
                        -Dsonar.host.url=http://sonarqube:9000 \
                        -Dsonar.login=$SONAR_TOKEN
                        """
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Upload to Nexus') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'nexus-creds',
                    usernameVariable: 'NEXUS_USER',
                    passwordVariable: 'NEXUS_PASS'
                )]) {
                    sh '''
                    cat > settings.xml <<EOF
<settings>
  <servers>
    <server>
      <id>nexus</id>
      <username>$NEXUS_USER</username>
      <password>$NEXUS_PASS</password>
    </server>
  </servers>
</settings>
EOF

                    mvn deploy -s settings.xml
                    '''
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t rajiv84docker/springboot-demo:latest .'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                    echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                    docker push rajiv84docker/springboot-demo:latest
                    '''
                }
            }
        }
    }
}
