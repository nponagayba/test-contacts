[![Build Status](https://travis-ci.com/nponagayba/test-contacts.svg?branch=master)](https://travis-ci.com/nponagayba/test-contacts)


## Building and running locally
The application uses PostgreSQL DBMS. See datasource configuration in `src/main/resources/application.yml`

To start your application:
1. Start PostgreSQL DBMS locally, you can use the docker compose file `src/main/docker/postgresql.yml`
2. Run command:
    ```bash
    ./mvnw
    ```
## Building and running in Docker
1. Build image to the local Docker daemon using Jib plugin:
    ```bash
    ./mvnw package verify jib:dockerBuild
    ```
2. To run this image, use the Docker Compose configuration located in the src/main/docker folder of your application:
    ```bash
    docker-compose -f src/main/docker/app.yml up
    ```

## Application usage
### Create new contacts:
`PUT http://localhost:8080/hello/contacts/bulk`:
```json
[
	{
		"name": "abc"
	},
	{
		"name": "bcd"
	},
	{
		"name": "cde"
	},
	{
		"name": "cdf"
	}
]
```
### Search contacts by name filter
`GET http://localhost:8080/hello/contacts?nameFilter=^c.*$` - get all contacts with `name` not starting with `c`