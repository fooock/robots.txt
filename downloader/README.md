# `robots.txt` downloader

[![License](https://img.shields.io/badge/License-GPLv3%202.0-brightgreen.svg?style=for-the-badge)](https://www.gnu.org/licenses/gpl-3.0)

This project downloads *robots.txt* files from internet.

## Configuration

You can change this properties to change server behavior:

### Server

| **Variable**              	| **Description**                	| **Default**      	|
|---------------------------	|--------------------------------	|------------------	|
| `SERVER_PORT` 	| Default server port        	| `8080` 	|
| `SERVER_ADDRESS` | Default server address | `localhost` |
| `ASYNC_CORE_SIZE` | Number of cores used for async processing | `2` |
| `ASYNC_QUEUE_SIZE` | Queue size | `1000` |
| `LOGGING_FILE` | Server logging file | |
| `STREAM_CONSUMER_GROUP` | Stream consumer group | `group-1` |
| `STREAM_CONSUMER_NAME` | Stream consumer name | `consumer-1` |

### Database

>You can change database configuration using the values documented [here](../database/README.md#Database)

### Redis

>You can change database configuration using the values documented [here](../database/README.md#Redis)

## Build

If you want to build a `.jar` with all server dependencies, just execute the next command from the project root directory:

```
$ ./gradlew assemble
```

>The resulting executable will be located inside the `build/libs` directory.

Also, if you want to execute this project using Docker, from the project root directory execute:

```
$ make build-downloader
```

This command will create a new docker image with all project dependencies.

## References

* [rfc 1738 - Uniform Resource Locators (URL)](https://www.ietf.org/rfc/rfc1738.txt)
* [rfc 7233 - Hypertext Transfer Protocol (HTTP/1.1): Range Requests](https://tools.ietf.org/html/rfc7233#section-3.1)
