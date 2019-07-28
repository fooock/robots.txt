# :robot: `robots.txt` as a service :robot:

[![License](https://img.shields.io/badge/License-GPLv3%202.0-brightgreen.svg?style=for-the-badge)](https://www.gnu.org/licenses/gpl-3.0)

>:construction: Project in early development

Distributed *robots.txt* parser and rule checker through API access. If you are working on
a distributed web crawler and you want to be **polite** in your action, then you will find
this project very useful. Also, this project can be used to integrate into any *SEO* tool to check if 
the content is being indexed correctly by robots.

>For this first version, we are trying to comply with the specification used by Google to analyze websites. You can see it [here](https://developers.google.com/search/reference/robots_txt?hl=en).
Expect support from other robot specifications soon!

## Why this project?

If you are building a distributed web crawler, you know that manage *robots.txt* rules from websites is a hard task, 
and can be complicated to maintain in a scalable way. You need to focus on your business requirements. `robots.txt` 
can help by acting as a service to check if a given url resource can be crawled using a specified user agent 
(or robot name). It can be easily integrated in existing software through a web API, and start to work in less than a second!

## Requirements

In order to build this project in your machine you will need to have installed in your system:

* [Redis 5](https://redis.io/topics/quickstart), including `redis-cli` and `redis-server`
* `Java 8` and [Kotlin](https://kotlinlang.org/docs/tutorials/command-line.html)
* [Docker](https://docs.docker.com/install/)
* [docker-compose](https://docs.docker.com/compose/install/)
* `make`

## Getting started

If you want to test this project locally, then you will need to be installed in your system [Docker](https://www.docker.com/), 
[docker-compose](https://docs.docker.com/compose/install/) and `Make`. When done, then execute the following
command to compile all projects, build docker images and run it:

>:point_right: Change the `DOCKER_NETWORK` environment variable from [`.env`](.env) file to match
you docker host interface

```bash
$ make start-all
```

>You can execute `make logs` to see how things have gone

Now you can send some URL's to the crawler system to download the rules found in the *robots.txt* file
and persist it in the database. For example, you can invoke the crawl API using this command:

```bash
$ curl -X POST http://localhost:8081/v1/send \
       -d 'url=https://news.ycombinator.com/newcomments' \
       -H 'Content-Type: application/x-www-form-urlencoded'
```
>Also, there is another method in the API to make a crawl request but using a `GET` method.

This command will send the URL to the streaming service, and when received, the `robots.txt` file
will be downloaded, parsed and saved into the database.

The next step is to check if you can access any resource of a known host using a `user-agent` directive. For this 
purpose, you will need to use the checker API. Imagine that you need to check if your crawler can access the 
`newest` resource from [hacker news](https://news.ycombinator.com/newest). You will execute:

```bash
$ curl -X POST http://localhost:8080/v1/allowed \
       -d '{"url": "https://news.ycombinator.com/newest","agent": "AwesomeBot"}' \
       -H 'Content-Type: application/json'
```

The response will be:

```json
{
  "url":"https://news.ycombinator.com/newest",
  "agent":"AwesomeBot",
  "allowed":true
}
```

This is like saying: *Hey!, you can crawl content from `https://news.ycombinator.com/newest`*

When you finish your test, execute the next command to stop and remove all docker containers and 
associated volumes:

```bash
$ make stop-all
```

>If you want to start hacking, stop all containers and use the instructions from [this directory](setup) to start all
project required services

