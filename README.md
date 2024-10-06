# Javka - Local DEV Environment

## Maven

Maven was used to handle dependencies, compilation and packaging.

### Install Maven

To install Maven on Ubuntu, execute commands:

```bash
sudo apt update
```

```bash
sudo apt install maven
```

```bash
mvn -version
```

## Maven commands list

### Compile Java Application

The classes and config files will be located in the `/target/classes/` directory

```bash
mvn clean compile
```

### Run Compiled Java Application

```bash
mvn exec:java -Dexec.mainClass="com.danielpietka.app.Main"
```

### Package the Java Application into a JAR File

The JAR and config files will be located in the `/target/JAR/` directory

```bash
mvn clean package
```

### Run the packed Java Application (JAR file)

```bash
mvn exec:java -jar target/JAR/javka-app-1.0.jar
```

## Docker Containers

Docker has been implemented for compiling and run the Java application in development process.
During the startup of the Java application container, the application is compiled automatically in docker container,
allowing the most current version of the code to be executed.
In case of code changes, simply stop the Docker containers and remove the volumes with a command.
Upon restarting Docker, the application will run the latest version of the code.

## Docker commands list

### Build Docker

```bash
docker compose build
```

### Start Docker, Compile and Run Java app (Logs Attached)

- Start docker containers `java_app_compile` `java_db` `java_phpmyadmin` with attached logs
- Compile the java application to classes
- Run the java application classes

```bash
docker compose up java_app_compile
```

### Start Docker, Run Java JAR app (Logs Attached)

- Start docker containers `java_app_package` `java_db` `java_phpmyadmin` with attached logs
- Run the java JAR application

```bash
docker compose up java_app_package
```

### Start Docker, Compile and Run Java app (Daemon mode)

- Start docker containers `java_app_compile` `java_db` `java_phpmyadmin` with attached logs
- Compile the java application to classes
- Run the java application classes

```bash
docker compose up -d java_app_compile
```

### Start Docker, Run Java JAR app (Daemon mode)

- Start docker containers `java_app_package` `java_db` `java_phpmyadmin` with attached logs
- Run the java JAR application

```bash
docker compose up -d java_app_package
```

### Stop Docker (Logs Attached)

- stop all docker containers

```bash
use keys: "ctrl" + "c"
```

### Stop Docker

- stop all docker containers

```bash
docker compose down
```

### Stop Docker and Remove

- stop all docker containers
- remove all containers and volumes (database will be removed)

```bash
docker compose down -v
```

### Restart Docker

- restart all docker containers

```bash
docker compose restart
```

### Connect Java Container (java_app_compile)

- connect to the Java Application Container (SSH)

```bash
docker exec -it java_app_compile bash
```

### Connect Java Container (java_app_package)

- connect to the Java Application Container (SSH)

```bash
docker exec -it java_app_package bash
```

### Connect to MySQL Container

- connect to the MySQL database Container (SSH)

```bash
docker exec -it java_db bash
```

### Logs (Daemon mode)

- view Logs of All Containers in Detached Mode

```bash
docker compose logs
```

### Status

- check the Status of running containers

```bash
docker compose ps
```

## Docker container list

### Docker Container for compile and run Java App (classes)

```
container name: java_app_compile
host:port: localhost:8080
```

### Docker Container for run Java App (JAR)

```
container name: java_app_package
host:port: localhost:8080
```

### MySQL Container

```
container name: java_db
database host: localhost
database name: mydatabase
database port: 3306

### user account ###
username: user
password: password

### root account ###
user: root
password: rootpassword
```

### PhpMyAdmin Container

host:port [localhost:8081](http://localhost:8081)

```
container name: java_phpmyadmin
host:port: localhost:8081
```
