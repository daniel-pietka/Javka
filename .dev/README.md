# Javka - Local DEV Environment

## Containers

### Application Container

```
localhost:8080
```

### PhpMyAdmin Container

```
localhost:8081
```
### MySQL Container

```
database host: localhost
database name: mydatabase
Port: 3306

### user account ###
username: user
password: password

### root account ###
user: root
password: rootpassword
```

## Docker Compose Command List

### Run all commands in path: ROOT/.dev/ 

```bash
cd .dev
```

### Build and Start Docker Environment in Detached Mode (Daemon)

```bash
docker compose up -d --build
```

### Start Docker Environment in Detached Mode (Daemon)

```bash
docker compose up -d
```

### Remove All Containers and Volumes

```bash
docker compose down -v
```

### Stop Running Containers

```bash
docker compose down
```

### Build and Start Docker Environment (with Console Output)

```bash
docker compose up --build
```

### Start Docker Environment (with Console Output)

```bash
docker compose up
```

### Stop Docker Environment (with Console Output)

```bash
use keys: "ctrl" + "c"
```

### Restart Docker Environment

```bash
docker compose restart
```

### Check the Status of Running Containers

```bash
docker compose ps
```

### Connect to the Java Application Container (SSH)

```bash
docker exec -it java_app bash
```

### Connect to the MySQL Container (SSH)

```bash
docker exec -it java_db bash
```

### View Logs of All Containers in Detached Mode

```bash
docker compose logs -f
```

### View Logs of All Containers

```bash
docker compose logs
```
