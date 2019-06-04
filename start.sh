#! /bin/bash

mvn install

cd weixin_1
mvn spring-boot:start

cd ../subscribe
mvn spring-boot:start
