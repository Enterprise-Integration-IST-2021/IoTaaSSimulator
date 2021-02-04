FROM ubuntu

RUN apt update
RUN apt-get install -y sudo mysql-server mysql-client vim

COPY db.sql /root
RUN service mysql start && mysql -u root < /root/db.sql

RUN sed 's/^bind-address/#bind-address/' /etc/mysql/mysql.conf.d/mysqld.cnf > /etc/mysql/mysql.conf.d/tmp ; mv /etc/mysql/mysql.conf.d/tmp /etc/mysql/mysql.conf.d/mysqld.cnf

ENTRYPOINT service mysql restart && /bin/bash


