pipeline {
    agent any

    parameters {
        choice(name: 'Server', choices: ['Development', 'Production'], description: 'Select the server environment')
    }

    stages {
        stage('Preparation') {
            steps {
                sh 'rm -rf *'
            }
        }

        stage('Cloning the Project') {
            steps {
                script {
                    try {
                        echo "Cloning the code"
                        git url: "https://gitlab.webelight.co.in/webelight/devops/training.git", branch: "yatharth-jenkins", credentialsId: "gitup"
                    } catch(Exception e) {
                        echo "FAILED ${e}"
                        currentBuild.result = 'FAILURE'
                        throw e
                    }
                }
            }
        }

        stage("Building the Code") {
            steps {
                script {
                    try {
                        if ("${params.Server}" == 'Production') {
                            echo "Building for Production"
                            sh 'docker build -t my-new-app .'
                        } else if ("${params.Server}" == 'Development') {
                            echo "Building for Development"
                        }
                    } catch(Exception e) {
                        echo "FAILED ${e}"
                        currentBuild.result = 'FAILURE'
                        throw e
                    }
                }
            }
        }

        stage("Push to Docker Hub") {
            steps {
                script {
                    try {
                        echo "Pushing the image to Docker Hub"
                        if ("${params.Server}" == 'Production') {
                            withCredentials([usernamePassword(credentialsId: "dockerhub", passwordVariable: "dockerHubPass", usernameVariable: "dockerHubUser")]) {
                                sh "docker tag my-new-app ${env.dockerHubUser}/my-new-app:latest"
                                sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPass}"
                                sh "docker push ${env.dockerHubUser}/my-new-app:latest"
                            }
                        } else if ("${params.Server}" == 'Development') {
                            echo "Skipping"
                            //sh "docker build -t your-image-name ."
                        }
                    } catch(Exception e) {
                        echo "FAILED ${e}"
                        currentBuild.result = 'FAILURE'
                        throw e
                    }
                }
            }
        }

        stage("Run Docker Container") {
            steps {
                script {
                    try {
                        if ("${params.Server}" == 'Production') {
                            echo "Running the Docker container"
                            // sh "docker run -d -p 3000:3000 yatharthchauhan2024/yc-app-jenkins:latest"
                            sh "docker-compose down"
                            sh "docker-compose up -d"
                        } else if ("${params.Server}" == 'Development') {
                            echo "Skipping"
                            // sh "docker run -p 3000:3000 your-image-name"
                        }
                    } catch(Exception e) {
                        echo "FAILED ${e}"
                        currentBuild.result = 'FAILURE'
                        throw e
                    }
                }
            }
        }
    }
}
