THIS DOC IS WIP.

# net-promoter-score-collector

This is a [NPS](https://www.medallia.com/net-promoter-score/) collector made by John to replicate the same feature built by [Zendesk](https://support.zendesk.com/hc/en-us/articles/203755166-Creating-and-sending-a-Net-Promoter-Score-survey-to-measure-customer-loyalty) whom requires a commercial license and is expenseive. 

This project is built with [Spring Boot Data JPA](https://spring.io/guides/gs/accessing-data-mongodb/) and Spring Boot Starter to serve static assets (survey site) and Object Mapping MongoDb objects. Front-end dashboard is still under development with Vue.js.

With the power of Spring Boot starter, you can boost this app in a minute by following:

# Step 1
```
./graldew build
```

# Step 2
create a Spring Boot application runner in your IDE:
![Image of Swagger](http://www.zhuoran.li/assets/img/static/readme/nps-app-setup.png)


# Step 3
Run it!

![Image of Swagger](http://www.zhuoran.li/assets/img/static/readme/nps-app-start.png)

# Bonus Track!
You can run the front-end dashboard by following
```
cd frontend
yarn install
yarn run dev
```

# Swagger
API Documents are available via Swagger: http://localhost:8000/swagger-ui.html
![Image of Swagger](http://www.zhuoran.li/assets/img/static/readme/nps-swagger.png)
