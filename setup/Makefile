start-pg:
	docker-compose -f ./postgresql/docker-compose.yml up -d

stop-pg:
	docker-compose -f ./postgresql/docker-compose.yml down --volumes --remove-orphans

start-redis:
	cd redis && ./create-cluster.sh start && ./create-cluster.sh create

stop-redis:
	cd redis && ./create-cluster.sh stop && rm *.rdb *.conf *.aof *.log

start-all: start-pg start-redis
stop-all: stop-pg stop-redis
