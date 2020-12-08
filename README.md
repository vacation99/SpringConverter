# SpringConverter

Необходимо в консоли PsotgreSQL создать БД с любым названием, далее в application.properties изменить следующие параррметры: 
- spring.datasource.url=jdbc:postgresql://localhost:5432(стандартный порт, изменить если другой)/вставить сюда навзание своей БД
- spring.datasource.username=ввести свой username
- spring.datasource.password=ввести свой password
- spring.jpa.hibernate.ddl-auto=поствить create, делее необходимо поменять на update после первого запуска приложения, иначе история будет создаваться заново

Авторизация на сайте:
- Логин: 1
- Пароль: 1
