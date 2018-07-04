# net-promoter-score-collector

![Image of NPS Collector](http://www.zhuoran.li/assets/img/static/readme/nps-complete-survey.png)

This is a [NPS](https://www.medallia.com/net-promoter-score/) collector.

This project is built with [Spring Data](https://spring.io/guides/gs/accessing-data-mongodb/) and Spring Boot. Backed by MongoDB as storage. It's also my first try out with [Vue](https://vuejs.org/) + [Vuex](https://vuex.vuejs.org/en/intro.html).

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

![Image of App Start](http://www.zhuoran.li/assets/img/static/readme/nps-app-start.png)

# Develop Front-end
You can run 
```
cd nps-collector-web
yarn dev
```

# Swagger
API Documents are available via Swagger: http://localhost:8000/swagger-ui.html
![Image of Swagger](http://www.zhuoran.li/assets/img/static/readme/nps-swagger.png)
