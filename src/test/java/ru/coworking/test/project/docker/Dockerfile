# Подключаю образ PostgreSQL
FROM postgres:latest

# Установка параметров окружения postgreSQL
ENV POSTGRES_DB=coworkings_db
ENV POSTGRES_USER=root
ENV POSTGRES_PASSWORD=123456

# копирование файла со скриптом миграции в контейнер
COPY init-db.sql /docker-entrypoint-initdb.d/

# Старт PostgreSQL
CMD ["postgres"]