# Data access layer

[![License](https://img.shields.io/badge/License-GPLv3%202.0-brightgreen.svg?style=for-the-badge)](https://www.gnu.org/licenses/gpl-3.0)

This project handle database operations like save, retrieve, update and delete. It uses PostgreSQL as backend,
and uses [JSONB](https://www.postgresql.org/docs/9.6/functions-json.html) as type to save host rules.

>Database schema can be found [here](src/main/resources/schema.sql)

## Configuration

You can change this properties to change server behavior:

### Database

| **Variable**              	| **Description**                	| **Default**      	|
|---------------------------	|--------------------------------	|------------------	|
| `DATASOURCE_URL` 	| Default database url        	| `jdbc:postgresql://localhost:5432/robotstxt` 	|
| `DATASOURCE_DRIVER`        	| Database driver name                     	| `org.postgresql.Driver`       	|
| `DATASOURCE_USERNAME`    	| Database username     	| `postgresql`              	|
| `DATASOURCE_PASSWORD` 	| Database user password 	| `changeme`              	|

### Redis

| **Variable**              	| **Description**                	| **Default**      	|
|---------------------------	|--------------------------------	|------------------	|
| `REDIS_HOST` 	| Default redis url        	| `127.0.0.1` 	|
| `REDIS_PORT`        	| Default redis port                     	| `6379`       	|

## Build

If you want to build a `.jar`, just execute the command:

```
$ ./gradlew assemble
```

>The resulting executable will be located inside the `build/libs` directory.
