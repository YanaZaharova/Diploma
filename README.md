# Дипломный проект по профессии «Тестировщик»
*Дипломный проект — автоматизация тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка.*
---
### Подготовка и запуск на MySQL:

1. Запустить все контейнеры:

```
docker-compose up
```

2. Запустить SUT:

```
java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar
```

3. Запустить автотесты:

```
./gradlew clean test "-Ddatasource.url=jdbc:mysql://localhost:3306/app"
```

4. Сгенерировать отчет на базе Allure:

```
./gradlew allureServe
```

### Подготовка и запуск на PostgreSQL:

1. Запустить все контейнеры:

```
docker-compose up
```

2. Запустить SUT:

```
java "-Dspring.datasource.url=jdbc:postgres://localhost:5432/postgres" -jar artifacts/aqa-shop.jar
```

3. Запустить автотесты:

```
./gradlew clean test "-Dspring.datasource.url=jdbc:postgres://localhost:5432/postgres"
```

4. Сгенерировать отчет на базе Allure:

```
./gradlew allureServe
```
