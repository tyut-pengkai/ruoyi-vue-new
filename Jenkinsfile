pipeline {
    agent {
        kubernetes {
            cloud 'prod-k8s'
            slaveConnectTimeout 1200
            workspaceVolume hostPathWorkspaceVolume(hostPath:"/opt/workspace", readOnly: false)
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - args: [\'$(JENKINS_SECRET)\', \'$(JENKINS_NAME)\']
    image: "registry.cn-hangzhou.aliyuncs.com/hujiaming/jnlp-slave:1.0.0"
    name: jnlp
    imagePullPolicy: IfNotPresent
    volumeMounts:
    - mountPath: "/etc/localtime"
      name: "localtime"
      readOnly: false
  - command:
    - "cat"
    env:
    - name: "LANGUAGE"
      value: "en_US:en"
    - name: "LC_ALL"
      value: "en_US.UTF-8"
    - name: "LANG"
      value: "en_US.UTF-8"
    image: "registry.cn-hangzhou.aliyuncs.com/hujiaming/node:alpine-lts"
    imagePullPolicy: "IfNotPresent"
    name: "build"
    tty: true
    volumeMounts:
    - mountPath: "/etc/localtime"
      name: "localtime"
    - mountPath: "/root/.m2/"
      name: "cachedir"
      readOnly: false
  - command:
    - "cat"
    env:
    - name: "LANGUAGE"
      value: "en_US:en"
    - name: "LC_ALL"
      value: "en_US.UTF-8"
    - name: "LANG"
      value: "en_US.UTF-8"
    image: "registry.cn-hangzhou.aliyuncs.com/hujiaming/kubectl:alpine"
    imagePullPolicy: "IfNotPresent"
    name: "kubectl"
    tty: true
    volumeMounts:
    - mountPath: "/etc/localtime"
      name: "localtime"
      readOnly: false
  - command:
    - "sleep"
    args:
    - "99d"
    env:
    - name: "LANGUAGE"
      value: "en_US:en"
    - name: "LC_ALL"
      value: "en_US.UTF-8"
    - name: "LANG"
      value: "en_US.UTF-8"
    image: "registry.cn-hangzhou.aliyuncs.com/hujiaming/kaniko-executor:1.0.0"
    imagePullPolicy: "IfNotPresent"
    name: "kaniko"
    tty: true
    volumeMounts:
    - mountPath: "/etc/localtime"
      name: "localtime"
      readOnly: false
    - mountPath: "/kaniko/.docker"
      name: "docker-registry-config"
  restartPolicy: "Never"
  nodeSeletor:
    build: "true"
  volumes:
  - name: docker-registry-config
    configMap:
      name: docker-registry-config
  - hostPath:
      path: "/usr/share/zoneinfo/Asia/Shanghai"
    name: "localtime"
  - name: "cachedir"
    hostPath:
      path: "/opt/m2"
        '''
        }
    }
    stages {
        stage('Pulling Code') {
            parallel {
              stage('Pulling Code by Jenkins') {
                when {
                  expression {
                   env.gitlabBranch == null
                  }
                }
          steps {
            git(changelog: true, poll: true, url: "${GIT_URL}", branch:"${BRANCH}", credentialsId: 'jenkinskey')
            script {
              COMMIT_ID = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
              TAG = BUILD_TAG + '-' + COMMIT_ID
              println "Current branch is ${BRANCH}, Commit ID is ${COMMIT_ID}, Image TAG is ${TAG}"
            }
          }
        }
    stage('Pulling Code by trigger') {
      when {
        expression {
          env.gitlabBranch != null
        }
      }
      steps {
        git(url: "${GIT_URL}", branch: env.gitlabBranch, changelog:true, poll: true, credentialsId: 'jenkinskey')
        script {
          COMMIT_ID = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
          TAG = BUILD_TAG + '-' + COMMIT_ID
          println "Current branch is ${env.gitlabBranch}, Commit ID is ${COMMIT_ID}, Image TAG is ${TAG}"
        }
      }
    }
   }
  }
    stage('Building') {
      steps {
        container(name: 'build') {
          sh """
             cd ruoyi-ui
             npm install --registry=https://registry.npmmirror.com/
             npm run build:prod
             ls 
             """
         }
       }
    }
    stage('Build for creating image') {
      steps {
        container(name: 'kaniko') {
          sh """
            executor -d ${HARBOR_ADDRESS}/${REGISTRY_DIR}/${IMAGE_NAME}:${TAG} -c . --insecure --skip-tls-verify
             """
        }
      }
    }
    stage('Deploying to K8s') {
      environment {
        MY_KUBECONFIG = credentials('prod-k8s')
      }
      steps {
        container(name: 'kubectl'){
        sh """
           kubectl --kubeconfig $MY_KUBECONFIG set image deploy -l app=${IMAGE_NAME} ${IMAGE_NAME}=${HARBOR_ADDRESS}/${REGISTRY_DIR}/${IMAGE_NAME}:${TAG} -n $NAMESPACE
           """
         }
      }
    }
  }
  environment {
    COMMIT_ID = ""
    HARBOR_ADDRESS = "192.168.88.171"
    REGISTRY_DIR = "ruoyi"
    IMAGE_NAME = "ruoyi-ui"
    NAMESPACE = "ruoyi"
    TAG = ""
    GIT_URL = "git@gitee.com:follow_fates/ruoyi-ui.git"}
  parameters {
    gitParameter(
        branch: '', 
        branchFilter: 'origin/(.*)', 
        defaultValue: '', 
        description: 'Branch for build and deploy', 
        name: 'BRANCH',
        quickFilterEnabled: false, 
        selectedValue: 'NONE', 
        sortMode: 'NONE',
        tagFilter: '*', 
        type: 'PT_BRANCH'
    )
  }
}