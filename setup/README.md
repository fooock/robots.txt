# Setup environment

[![License](https://img.shields.io/badge/License-GPLv3%202.0-brightgreen.svg?style=for-the-badge)](https://www.gnu.org/licenses/gpl-3.0)

In order to run this project successfully, you need to have multiple services 
running. Use this setup only for **testing** purposes.

## Postgresql

To start the database just execute this command:

```
$ make start-pg
```

>To stop it execute `stop-pg`

## Redis

To start a test redis cluster then execute:

```
$ make start-redis
```

It will start six redis servers and try to join in cluster automatically.

>To stop it execute `stop-redis`
