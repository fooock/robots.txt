#!/usr/bin/env bash

# Download and compile latest stable redis version
wget http://download.redis.io/redis-stable.tar.gz
tar xvzf redis-stable.tar.gz
cd redis-stable
make
