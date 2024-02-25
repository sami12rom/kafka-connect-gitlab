ARG CONFLUENT_CONNECT_VERSION=7.4.0
ARG CONFLUENT_IMAGE=confluentinc/cp-kafka-connect:$CONFLUENT_CONNECT_VERSION
FROM --platform=linux/amd64 $CONFLUENT_IMAGE
USER root

# Update the image
RUN yum update -y

# Install Dependencies
RUN \
    yum install -y \
    wget vim jq \
    curl sudo nano findutils \
    unzip tar ncurses zip


ADD ./connectors /usr/share/confluent-hub-components