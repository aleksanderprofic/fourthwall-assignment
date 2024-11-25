# Cinema web app backend

<!-- TOC -->
* [Cinema web app backend](#cinema-web-app-backend)
  * [API documentation](#api-documentation)
  * [Development](#development)
    * [Prerequisites](#prerequisites)
    * [How to run locally](#how-to-run-locally)
    * [Testing internal endpoints](#testing-internal-endpoints)
  * [Decisions and potential future improvements](#decisions-and-potential-future-improvements)
    * [Decisions made at the beginning of the development:](#decisions-made-at-the-beginning-of-the-development)
    * [Important things to add in the nearest future](#important-things-to-add-in-the-nearest-future)
    * [Potential future improvements / requirements](#potential-future-improvements--requirements)
  * [Summary](#summary)
    * [Miro board (for reference)](#miro-board-for-reference)
<!-- TOC -->

It is a Kotlin/Spring web application that allows users to view and act upon movie catalog.

## API documentation

Basic Open API v3 documentation is generated automatically. You can access it via Swagger UI through:
`/swagger-ui/index.html` or as a json through: `/v3/api-docs`

This documentation will be improved in the future.

## Development

### Prerequisites
- IDE (with Kotlin compiler)
- Java 17
- Maven
- Docker (for running locally and integration tests)

### How to run locally

You need to set active profile as `local` and set these environment variables:
1. `OMDB_API_KEY`, which can be obtained [here](https://www.omdbapi.com/apikey.aspx)
   - Click on free version, fill and submit the form, and you will receive the key on your email
2. `JWT_SECRET_KEY`, which can be generated online e.g. [here](https://jwtsecret.com/generate)

![readme1.png](images%2Freadme1.png)

Then, go to: `src/test/resources/infrastructure` folder, execute following command in the terminal: `docker compose up` 
(which will create a docker container with postgres database) and run the Spring application.

To fully test the application, you need to connect to a database (via Database tab in IntelliJ Ultimate, terminal or 
any other tool of your choice). Use below credentials:
```
url: jdbc:postgresql://localhost:5432/fourthwall_assignment
user: fourthwall_assignment
password: fourthwall_assignment
```

Now, insert some movies as you need data to work with:
```postgresql
insert into movies values (gen_random_uuid(), 'tt0232500', 'The Fast and the Furious');
insert into movies values (gen_random_uuid(), 'tt0322259', '2 Fast 2 Furious');
insert into movies values (gen_random_uuid(), 'tt0463985', 'The Fast and the Furious: Tokyo Drift');
insert into movies values (gen_random_uuid(), 'tt1013752', 'Fast & Furious');
insert into movies values (gen_random_uuid(), 'tt1596343', 'Fast Five');
insert into movies values (gen_random_uuid(), 'tt1905041', 'Fast & Furious 6');
insert into movies values (gen_random_uuid(), 'tt2820852', 'Furious 7');
insert into movies values (gen_random_uuid(), 'tt4630562', 'The Fate of the Furious');
insert into movies values (gen_random_uuid(), 'tt5433138', 'F9: The Fast Saga');
```

Application is ready for testing!

### Testing internal endpoints

Internal endpoints are secured for admin use only. If you want to test them, create a new admin user in the database 
(you can encode your password online [here](https://bcrypt-generator.com/)):
```postgresql
insert into users values (gen_random_uuid(), '<your_username>', '{bcrypt}<generated_encoded_password>', 'ADMIN');
```

Then, you need to log in with `/login` endpoint:
```shell
curl -X POST -H 'Content-Type: application/json' -d '{
  "username": "<your_username>",
  "password": "<your_password>"
}' http://localhost:8080/login
```

This call will return a token (which is valid for 5 minutes by default) which you can use in any subsequent calls in 
an authorization header (`-H 'Authorization: Bearer <token>'`), which will allow you to access secured endpoints. Request example:
```shell
curl -X POST -H 'Content-Type: application/json' \
-H 'Authorization: Bearer ' \
-d '{
    "movieId": "<uuid>",
    "startsAt": "2024-11-25T20:00:00+00:00",
    "price": "20.00"
}' http://localhost:8080/v1/movie-shows
```

## Decisions and potential future improvements

### Decisions made at the beginning of the development:
1. For now only admin users are added to the database (manually). In the future we can add a `/register` endpoint 
and extend roles with e.g. `STAFF`, `CUSTOMER` etc.
2. Whole movie catalog will be inserted into a database manually.
3. Environment specific variables are kept in environment specific application properties file, such as 
`application-local.properties` so it will be easy to inject proper values in the future (e.g. with AWS Secrets Manager)
4. Initial requirements were extended with below features:
   1. Fetch all movie catalog
      1. This should be the first endpoint called when someone opens the web application through UI to see what's available
   2. Login
      1. Needed for admin users to authenticate to restricted endpoints
   3. Create show time and price
      1. To separate update from creation
      2. We'd like to keep old show times and prices to track how they change over time and be able to create show 
      times in the future (e.g. a few weeks from now)
5. Some initial requirements were removed:
   1. Rate movie - seems that this feature is not critical to a success of this web application, so its development was postponed
6. Tech stack used for development
   1. Kotlin and Spring
   2. Postgres
      1. Efficient, we don't anticipate to have lots of data, but in case we will, it's easy to scale and work with, 
      data model looks suitable for SQL
   3. Flyway
      1. Easy to set up and useful to keep track of database changes
   4. Mybatis
      1. Easy to work with, does exactly what you tell it to do
   5. OpenAPI/Swagger
   6. Docker
      1. Easy to set up, it allows for a close enough environment similarity (local vs prod) and integration tests on a real environment
      2. Easy to pack the application with and deploy on any cloud provider

### Important things to add in the nearest future
1. Improve data types
2. When deploying the app to production environment I'd add `application-prod.properties` file, so we could set 
production specific properties e.g. url of prod db.
3. Add logging
4. Better error handling, such as for OMDbClient
5. Improve API responses
6. Improve Swagger
7. Add more tests because some functionalities are not covered at all
8. 

### Potential future improvements / requirements
1. Buy a ticket
2. Register user
3. Fetch movie times by day and time
4. Introduce other cinemas with watching rooms to choose from
5. Internal endpoint for adding more movies
6. Introduce cache e.g. for OMDb API calls
7. Automatically fetch movie shows from external source such as Excel file or different application
8. Add currency for price
9. Different types of prices, e.g. VIP, regular, discounted etc.

## Summary

I might have not included every decision and scenario in this document. In case of any questions, feel free to reach 
out to me via e-mail: `aleksanderprofic@gmail.com`

### Miro board (for reference)

At the beginning I created this miro board to gather my thoughts. Maybe it will be useful to you.

![miro_board.png](images%2Fmiro_board.png)
