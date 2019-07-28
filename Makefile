VERSION := 0.1.0

#   _           _ _     _
#  | |__  _   _(_) | __| |
#  | '_ \| | | | | |/ _` |
#  | |_) | |_| | | | (_| |
#  |_.__/ \__,_|_|_|\__,_|
#

build-checker:
	./gradlew checker:unpack
	docker build --force-rm -t robotstxt/checker-api ./checker
	docker build --force-rm -t robotstxt/checker-api:$(VERSION) ./checker

build-crawler:
	./gradlew crawler:unpack
	docker build --force-rm -t robotstxt/crawler-api ./crawler
	docker build --force-rm -t robotstxt/crawler-api:$(VERSION) ./crawler

build-downloader:
	./gradlew downloader:unpack
	docker build --force-rm -t robotstxt/downloader ./downloader
	docker build --force-rm -t robotstxt/downloader:$(VERSION) ./downloader

build-parser:
	$(MAKE) -C parser/ generate

build-all: build-parser build-checker build-crawler build-downloader

#                   _
#   _ __  _   _ ___| |__
#  | '_ \| | | / __| '_ \
#  | |_) | |_| \__ \ | | |
#  | .__/ \__,_|___/_| |_|
#  |_|

push-crawler:
	docker push robotstxt/crawler-api:latest
	docker push robotstxt/crawler-api:$(VERSION)

push-checker:
	docker push robotstxt/checker-api:latest
	docker push robotstxt/checker-api:$(VERSION)

push-downloader:
	docker push robotstxt/downloader:latest
	docker push robotstxt/downloader:$(VERSION)

push-all: push-checker push-crawler push-downloader

#                              _   _
#    ___  _ __   ___ _ __ __ _| |_(_) ___  _ __  ___
#   / _ \| '_ \ / _ \ '__/ _` | __| |/ _ \| '_ \/ __|
#  | (_) | |_) |  __/ | | (_| | |_| | (_) | | | \__ \
#   \___/| .__/ \___|_|  \__,_|\__|_|\___/|_| |_|___/
#        |_|

start-all: start-redis-cluster build-all
	docker-compose up -d

stop-all:
	docker-compose down --volumes --remove-orphans
	$(MAKE) -C setup/ stop-redis

logs:
	docker-compose logs -f

start-redis-cluster:
	$(MAKE) -C setup/ start-redis
