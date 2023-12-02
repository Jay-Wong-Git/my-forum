FROM openjdk:17
ENV APP_PATH=/apps
WORKDIR $APP_PATH
ADD my-site.jar $APP_PATH/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar"]
CMD ["app.jar"]