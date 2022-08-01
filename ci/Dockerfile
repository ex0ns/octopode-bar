# docker build . -t scala-mill-ci
# Pull base image
FROM openjdk:11-jre

# Env variables
ENV SCALA_VERSION 2.13.8
ENV MILL_VERSION 0.10.5

# Define working directory
WORKDIR /root

# Install Scala
RUN \
  curl -fsL https://downloads.typesafe.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz | tar xfz - -C /root/ && \
  echo >> /root/.bashrc && \
  echo "export PATH=~/scala-$SCALA_VERSION/bin:$PATH" >> /root/.bashrc

# Install mill
RUN \
  curl -L -o /usr/local/bin/mill https://github.com/lihaoyi/mill/releases/download/$MILL_VERSION/$MILL_VERSION && \
  chmod +x /usr/local/bin/mill && \
  touch build.sc && \
  mill -i resolve _ && \
  rm build.sc && rm -rf out

# Install npm
RUN \
  curl -sL https://deb.nodesource.com/setup_18.x |  bash - && \
  apt-get install -y nodejs && \
  apt-get clean && rm -rf /var/lib/apt/lists/*

# Install yarn 
RUN  \
  curl -sL https://dl.yarnpkg.com/debian/pubkey.gpg | gpg --dearmor --yes -o /usr/share/keyrings/yarnkey.gpg && \
  echo "deb [signed-by=/usr/share/keyrings/yarnkey.gpg] https://dl.yarnpkg.com/debian stable main" | tee /etc/apt/sources.list.d/yarn.list && \
  apt-get update && apt-get install yarn -y && \
  apt-get clean && rm -rf /var/lib/apt/lists/*

# Install docker and setup buildkit
RUN \
  curl -fsSL https://get.docker.com | sh - && \
  mkdir -p /etc/docker && \
  echo '{ "features": { "buildkit": true } }' > /etc/docker/daemon.json && \
  apt-get clean && rm -rf /var/lib/apt/lists/*

# Install other tools
RUN \
  apt-get update && apt-get install tree -y && \
  apt-get clean && rm -rf /var/lib/apt/lists/*

RUN wget -c https://github.com/zmwangx/ets/releases/download/v0.2.1/ets_0.2.1_linux_amd64.tar.gz -O -| tar -xz -C /usr/local/bin

