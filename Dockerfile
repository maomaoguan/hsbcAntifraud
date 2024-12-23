FROM registry.cn-beijing.aliyuncs.com/hub-mirrors/maven:3-jdk-8 AS build
LABEL maintainer=mau
ARG active=prod
ENV active ${active}
ADD ./antifraud-web/target/antifraud-web.jar antifraud-web.jar
ENTRYPOINT ["java", "-jar", "antifraud-web.jar","--spring.profiles.active=${active}"]
