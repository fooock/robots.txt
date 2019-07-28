#!/bin/bash

# This script is the original one from the redis official repository
# with some changes for this project. If you want to see original one
# go the official redis repository and see under the utils/create-cluster
# directory

# Settings
PORT=30000
TIMEOUT=2000
NODES=6
REPLICAS=1

# You may want to put the above config parameters into config.sh in order to
# override the defaults without modifying this script.

if [ -a config.sh ]
then
    source "config.sh"
fi

# Load env vars
source ././../../.env

# Computed vars
ENDPORT=$((PORT+NODES))

if [ "$1" == "start" ]
then
    while [ $((PORT < ENDPORT)) != "0" ]; do
        PORT=$((PORT+1))
        echo "Starting $PORT"
        redis-server --bind $DOCKER_NETWORK --port $PORT --cluster-enabled yes --cluster-config-file nodes-${PORT}.conf --cluster-node-timeout $TIMEOUT --appendonly yes --appendfilename appendonly-${PORT}.aof --dbfilename dump-${PORT}.rdb --logfile ${PORT}.log --daemonize yes --protected-mode no
    done
    exit 0
fi

if [ "$1" == "create" ]
then
    HOSTS=""
    while [ $((PORT < ENDPORT)) != "0" ]; do
        PORT=$((PORT+1))
        HOSTS="$HOSTS $DOCKER_NETWORK:$PORT"
    done
    echo 'yes' | redis-cli --cluster create $HOSTS --cluster-replicas $REPLICAS
    # Create streams and consumer groups
    redis-cli --cluster call "$DOCKER_NETWORK:30001" xadd income 1-0 url "" priority ""
    redis-cli --cluster call "$DOCKER_NETWORK:30001" xadd update 1-0 url "" priority ""
    redis-cli --cluster call "$DOCKER_NETWORK:30001" xgroup create income group-1 $
    redis-cli --cluster call "$DOCKER_NETWORK:30001" xgroup create update group-1 $
    redis-cli --cluster call "$DOCKER_NETWORK:30001" xgroup create income group-2 $
    redis-cli --cluster call "$DOCKER_NETWORK:30001" xgroup create update group-2 $
    exit 0
fi

if [ "$1" == "stop" ]
then
    while [ $((PORT < ENDPORT)) != "0" ]; do
        PORT=$((PORT+1))
        echo "Stopping $PORT"
        redis-cli -h $DOCKER_NETWORK -p $PORT shutdown nosave
    done
    exit 0
fi

if [ "$1" == "watch" ]
then
    PORT=$((PORT+1))
    while [ 1 ]; do
        clear
        date
        redis-cli -h $DOCKER_NETWORK -p $PORT cluster nodes | head -30
        sleep 1
    done
    exit 0
fi

if [ "$1" == "tail" ]
then
    INSTANCE=$2
    PORT=$((PORT+INSTANCE))
    tail -f ${PORT}.log
    exit 0
fi

if [ "$1" == "call" ]
then
    while [ $((PORT < ENDPORT)) != "0" ]; do
        PORT=$((PORT+1))
        redis-cli -h $DOCKER_NETWORK -p $PORT $2 $3 $4 $5 $6 $7 $8 $9
    done
    exit 0
fi

if [ "$1" == "clean" ]
then
    rm -rf *.log
    rm -rf appendonly*.aof
    rm -rf dump*.rdb
    rm -rf nodes*.conf
    exit 0
fi

if [ "$1" == "clean-logs" ]
then
    rm -rf *.log
    exit 0
fi

echo "Usage: $0 [start|create|stop|watch|tail|clean]"
echo "start       -- Launch Redis Cluster instances."
echo "create      -- Create a cluster using redis-cli --cluster create."
echo "stop        -- Stop Redis Cluster instances."
echo "watch       -- Show CLUSTER NODES output (first 30 lines) of first node."
echo "tail <id>   -- Run tail -f of instance at base port + ID."
echo "clean       -- Remove all instances data, logs, configs."
echo "clean-logs  -- Remove just instances logs."
