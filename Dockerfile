FROM java:8
EXPOSE 10000
ADD /target/*.jar demo.jar
ENTRYPOINT ["java","-jar","demo.jar"]