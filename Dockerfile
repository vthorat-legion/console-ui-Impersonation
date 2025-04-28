FROM circleci/openjdk:8u232-jdk-node-browsers

RUN cd ~ && \
    mkdir project && \
    cd project && \
    mkdir screenshots && \
    mkdir downloads

COPY *.xml /home/circleci/project/
COPY src /home/circleci/project/src/

ENV ENVCFG_FILE_LOCATION /home/circleci/project/src/test/resources/ciEnvCfg.json

RUN cd ~/project && \
    mvn install -DskipTests

# Fix for error message on start
RUN mkdir /tmp/.X11-unix && \
    sudo chmod 1777 /tmp/.X11-unix && \
    sudo chown root /tmp/.X11-unix/

WORKDIR /home/circleci/project

CMD ["mvn", "-e", "test"]
