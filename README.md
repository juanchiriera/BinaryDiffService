# Diff finder application

The diff finder application, is a REST api written in Java under Spring framework.
The basic functionality of the application is to provide an endpoint in which a user can load binary JSON strings and compare Left and Right JSON objects.

## Some definitions
The applicaiton accepts only Base64 encoded JSON strings. If any other type of content is submitted, it will return an error.

In order to fulfill the requirements of the excercise, the difference it will provide between Left and Right items is as follows.

- If both items are equal, it will return the decoded JSON content of them.
- If the items are different it will return a JSON object containing each node where values are different and each node any of the items have that the oter one does not have.
- Different vales from nodes will be displayed: `{"key": "(leftValue, rightValue)"}`.
- Nodes not contained on any of both Items will show as: `{"key": "value"}`

## Installation

In order to install the application, it is required to have JDK 11 installed alongside with MVN.

### Testing

The application does have unit and integration tests, which can be ran with the following command:
```shell script
mvn test
```

### Install
In order to install the application, and it's dependencies, run:
```shell script
mvn clean install
```
If you prefer to skip tests:
```shell script
mvn clean install -DskipTests
```

### Run
Run the application with:
```shell script
java -jar ./target/binaryDiff-v0.1.jar
```

## Try it out!
You can access to API specification though `localhost:8080/swagger-ui.html`.

| Endpoint | CURL Command |
|---|---|
| /v1/diff/_{id}_/left | `curl --location --request POST 'localhost:8080/v1/diff/1/left' --header 'Content-Type: application/json' --data-raw 'eyJtZXNzYWdlIiA6ICJIZWxsbyB3b3JsZCEifQ=='` | 
| /v1/diff/_{id}_/right | `curl --location --request POST 'localhost:8080/v1/diff/1/right' --header 'Content-Type: application/json' --data-raw 'eyJtZXNzYWdlIiA6ICJIZWxsbyB3b3JsZCEifQ=='` |
| /v1/diff/_{id}_ | `curl --location --request GET 'localhost:8080/v1/diff/1'` 


## Suggested changes

Some of the changes I would make include:

- Create a component which centralizes the use of the ObjectMapper, in order to have cleaner code and reuse the comparator between Items.
- Add more test scenarios - I would add test cases with multiple iterations.
- Use an external database - Make use of an external DB, maybe a MongoDB would be of better choice for this structure.
- Provide more details on the differences between JSON objects.
- Provide better Exception handling - Throwing exceptions and caching them in the last layer is not a great practice!
- Better documented Swagger (api-docs).
