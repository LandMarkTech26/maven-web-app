node ('master'){
    def mavenHome = tool name: 'maven3.8.4'
stage ('1.Gitclone'){
  //sh "echo hello"
  git 'https://github.com/LandMarkTech26/maven-web-app'  
}
stage ('2.Mavenbuild'){
   sh "${mavenHome}/bin/mvn clean package"
}
stage ('3.Codequality'){
   sh "${mavenHome}/bin/mvn sonar:sonar" 
}
stage ('4.UploadArtifacts'){
    sh "${mavenHome}/bin/mvn deploy"
}
stage ('5.DeployTomcatUAT'){
    deploy adapters: [tomcat9(credentialsId: 'tomcatcredential-team13', path: '', url: 'http://3.144.22.54:8080/')], contextPath: null, war: 'target/*.war'
}
stage('6.Approval'){
  timeout(time:8, unit: 'HOURS')  {
  input message: 'Please verify and approve'
}
}
stage ('7.DeployTomcatPROD'){
    deploy adapters: [tomcat9(credentialsId: 'tomcatcredential-team13', path: '', url: 'http://3.144.22.54:8080/')], contextPath: null, war: 'target/*.war'
}
}
