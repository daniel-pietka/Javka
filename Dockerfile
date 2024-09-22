FROM openjdk:22-jdk-slim
RUN apt-get update && apt-get install -y maven
WORKDIR /app
EXPOSE 8080
CMD ["bash"]
