# risk-engine

Run with `gradlew bootRun`

Requires `kafka` instance reachable at `127.0.0.1:9092`

To call withdraw on the REST API:
```
curl "http://localhost:8080/withdraw?userId=100&token=BTC&requestedAmount=7"
```

To submit a kafka message make sure `rest-proxy` is reachable at `127.0.0.1:8082` and execute:
(note replace `reservationId` with one from withdraw REST API response first):

```
curl -X POST -H "Content-Type: application/vnd.kafka.json.v1+json" --data '{"records":[{"value":{ "userId":"100", "reservationId":"<insert-from-rest-service>", "boughtToken":"ETH", "boughtQuantity":"87.35", "soldToken":"BTC", "soldQuantity":"6.9" }}]}' "http://localhost:8082/topics/settled"
``` 

## test

Run unit tests with `gradlew test`

## run as a regular .jar

```
gradlew bootJar
java -jar build/libs/riskengine-0.0.1-SNAPSHOT.jar
```