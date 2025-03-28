Test proj in X5 company. Middle/Senior level.


How to start to have fun:
1. Clone repo
2. Run docker-compose : docker compose up -d
3. Instruction for institution (Pun)
 GET / - healthcheck app
 GET /db/dividends - get all data from H2 DB. Returned JSON with parsed data.
 GET /db/dividend?timestamp={IS NEEDED VALUE}. - Request to get data from a timestamp table. Value time for the date field can be viewed from a previous query, where timestamps are specified in the table, or entry any other.
 POST /db/dividends - Query for saved in H2
 PUT /kafka/dividend - Query to send dividend in a Kafka by id.
 POST /kafka/dividends - Query to send all dividends in a Kafka.

 When working with kafka, before working with it using the handles indicated above, you need to go to localhost:8082 on the raised kafka UI by docker, and create a cluster: enter the name   and bootstrap server - PLAINTEXT://kafka on port 29092. In the line with metrics, select the JMX type with port 9997.
 After that, we send a request to send divs to Kafka either by ID or by post. POST and PUT query used Insomnia/BurpSuite.
 After creation, go to topics -> dividends (topic name) -> message -> click on any line and get the parsed divideds value.

4. Enjoy and HAVE FUN
