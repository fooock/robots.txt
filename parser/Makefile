CURRENT_DIR := $(shell pwd)

generate:
	rm -rf ./src/main/java/com/fooock/robotstxt/parser/generated && \
	mkdir -p ./src/main/java/com/fooock/robotstxt/parser/generated
	cd grammar && \
	java -jar $(CURRENT_DIR)/antlr/antlr-4.7.2.jar \
	-o ../src/main/java/com/fooock/robotstxt/parser/generated \
	-package com.fooock.robotstxt.parser.generated RobotsTxt.g4
