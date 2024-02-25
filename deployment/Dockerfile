ARG CONFLUENT_CONNECT_VERSION=7.4.0
ARG CONFLUENT_IMAGE=confluentinc/cp-kafka-connect:$CONFLUENT_CONNECT_VERSION
FROM --platform=linux/arm64 $CONFLUENT_IMAGE
USER root

##################################
# Bootstrap
##################################
# Update the image
RUN yum update -y

# Install Dependencies
RUN \
    yum install -y \
    wget vim jq \
    curl sudo nano findutils \
    unzip tar ncurses zip

##################################
# Add Connectors
##################################
ADD connectors /usr/share/confluent-hub-components

##################################
# Expose Ports
##################################
# Curl
EXPOSE 80/tcp
# Kafka Connect
EXPOSE 8083/tcp
# Kafka Broker
EXPOSE 9092/tcp
# Control Center
EXPOSE 9021/tcp
