# Define variables
DOCKER_COMPOSE = docker-compose
DOCKER_COMPOSE_FILE = docker-compose.yml

# Default target
.PHONY: all
all: up

# Start the services (in detached mode)
.PHONY: up
up:
	$(DOCKER_COMPOSE) -f $(DOCKER_COMPOSE_FILE) up -d

# Stop the services
.PHONY: down
down:
	$(DOCKER_COMPOSE) -f $(DOCKER_COMPOSE_FILE) down

# Rebuild the images and restart services
.PHONY: build
build:
	$(DOCKER_COMPOSE) -f $(DOCKER_COMPOSE_FILE) build

# View logs for services
.PHONY: logs
logs:
	$(DOCKER_COMPOSE) -f $(DOCKER_COMPOSE_FILE) logs

# Tail logs for all services (similar to `docker-compose logs -f`)
.PHONY: logs-f
logs-f:
	$(DOCKER_COMPOSE) -f $(DOCKER_COMPOSE_FILE) logs -f

# Remove all containers, networks, but not volumes
.PHONY: clean
clean:
	docker ps -aq | xargs -r docker stop
	docker ps -aq | xargs -r docker rm
	docker network prune -f

# Docker build image 
.PHONY: create-image
create-image:
	docker rmi 	coltip:1.0.0
	docker build -t coltip:1.0.0 .

# Build war with maven
.PHONY: compile
compile:
	mvn clean install -DskipTests

# From war building to docker-compose 
.PHONY: run-solution
run-solution:
	make clean
	make compile
	make create-image
	make up

# Only stop docker components without removing them	
.PHONY: stop-containers
stop-containers:
	docker ps -aq | xargs docker stop

# Reset database	
.PHONY: reset-db
stop-containers:
	docker cp SQL/reset_coltip_database.sql coltip_db: /home
	docker exec -it coltip_db psql -U postgres -d coltip_db -1 -f /home/reset_coltip_database.sql