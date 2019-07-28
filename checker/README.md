# `robots.txt` public API

[![License](https://img.shields.io/badge/License-GPLv3%202.0-brightgreen.svg?style=for-the-badge)](https://www.gnu.org/licenses/gpl-3.0)

Use this API to check if a resource can be accessed using the given `user-agent`.

## Configuration

You can change this properties to change server behavior:

### Server

| **Variable**              	| **Description**                	| **Default**      	|
|---------------------------	|--------------------------------	|------------------	|
| `SERVER_PORT` 	| Default server port        	| `8080` 	|
| `SERVER_ADDRESS` | Default server address | `localhost` |
| `CACHE_ENTRY_TTL` | Cache TTL (in minutes) | `5` |
| `LOGGING_FILE` | Server logging file | |

### Database

>You can change database configuration using the values documented [here](../database/README.md)

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
$ make build-checker
```

This command will create a new docker image with all project dependencies.
