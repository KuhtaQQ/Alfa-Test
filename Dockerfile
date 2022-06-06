FROM openjdk:17

EXPOSE 8045

RUN mkdir ./app

COPY ./test-task-for-alfa-0.0.1.jar ./app

CMD java -jar ./app/test-task-for-alfa-0.0.1.jar