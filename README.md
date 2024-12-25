# hsbcAntifraud

used only for hsbc examination purpose

## features/functions introduction
refers to ![image](featuresDescription.png)

* A rule, which can be configured and executed for a certain rule based on features, for example, is it a suspicious login (rule2)
* A feature/variable, which can be configured and executed for a certain feature based on user data, for example, how many failed login attempts an account tried (xIllegalLogins)
* A data, which simulates and mocks data which will be used for feature computation
* support for antifraud detecting & message processing, which process messages/payloads and gives fraud alert & logs 


## arch & docs
* logical arch regarding  ![image](hsbcAntifraudLogicArch.jpg)
* a sequence diagram which demonstrates a major flow of execution   ![image](sequenceDiagram.jpg)

## code structure & modules

* antifraud-web module, it provides messaging and web related functions, also configurations and tests are within
* antifraud-service module, it provides all antifraud related services and all regarding utils, model, etc

## tests 
all tests are under antifraud-web/src/test/java/...

### the unit test regarding each major feature and modules
* FeatureTest
* RuleTest
* EnvTest, it is regarding environtment settings
* MessagingTest, it is regarding messaging send & receiving to ali-cloud

### the integration test
* AntifraudTest

### test report and coverage
this project uses surefire and jacco, in which test results will be output under antifraud-web/target/site/jacco-aggregate/index.html

## deployments
it is deployed on ali-cloud ack 

### access aliyun using following account 

    -  loginname and password is ->    interviewer@1712034974828389.onaliyun.com        ape@1234
    -  login url -> csnew.console.aliyun.com
    -  then shall able to login and see this screen below 
    -  application nodes can be accessed through 工作负载>>无状态>>hsbc-antifraud-test 
    -  hpa settngs are under 工作负载>>无状态>>hsbc-antifraud-test>>容器伸缩
    -  mq using mns, can reach through following https://mns.console.aliyun.com/region/cn-hangzhou/queue/antifraud/detail
    -  logs are under 运维管理>>日志中心>>应用日志, an example is shown below
    

![image](ackenv.png)    

![image](logs.png)


## dependencies
in order to run this u need to have following criteria
- jdk8 
- maven 
- springboot