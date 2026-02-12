# Primera etapa de compilación
# Utiliza una imagen Alpine de Maven como base para compilar el proyecto
FROM maven:3.9.6-eclipse-temurin-21 AS build
LABEL authors="angel.morando@metamedicsvr.com"

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo pom.xml para instalar las dependencias
COPY pom.xml .

# Instala las dependencias del proyecto
RUN mvn dependency:go-offline

# Copia el resto de los archivos del proyecto
COPY src ./src

# Variables de entorno para los tests al momento de compilar
ARG JWT_SECRET
ENV JWT_SECRET=$JWT_SECRET

ARG MAIL_USERNAME
ENV MAIL_USERNAME=$MAIL_USERNAME

ARG MAIL_PASSWORD
ENV MAIL_PASSWORD=$MAIL_PASSWORD

ARG DB_HOST
ENV DB_HOST=$DB_HOST

ARG DB_PORT
ENV DB_PORT=$DB_PORT

ARG DB_USER
ENV DB_USER=$DB_USER

ARG DB_PASS
ENV DB_PASS=$DB_PASS

ARG SPRING_PROFILES_ACTIVE
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE

ARG PASS_CERT_TEMPLATE_BACK
ENV PASS_CERT_TEMPLATE_BACK=$PASS_CERT_TEMPLATE_BACK

ARG S3_ACCESS_KEY
ENV S3_ACCESS_KEY=$S3_ACCESS_KEY

ARG S3_SECRET_KEY
ENV S3_SECRET_KEY=$S3_SECRET_KEY

ARG OPENAI_API_KEY
ENV OPENAI_API_KEY=$OPENAI_API_KEY

# Compila el proyecto
RUN mvn package

# Segunda etapa de compilación
# Imagen para ejecutar el archivo JAR compilado en la etapa anterior
# Se utiliza una imagen alpine para reducir el tamaño de la imagen final
# Version de Java 21
FROM eclipse-temurin:21-jre-alpine

# Establece el directorio de trabajo en /app
WORKDIR /app

# Actualiza los paquetes del sistema
RUN apk update && apk upgrade

# Copia el archivo JAR compilado desde la etapa de compilación anterior
COPY --from=build app/target/template-0.0.1-SNAPSHOT.jar .

# Variables de entorno para la aplicación
ARG JWT_SECRET
ENV JWT_SECRET=$JWT_SECRET

ARG MAIL_USERNAME
ENV MAIL_USERNAME=$MAIL_USERNAME

ARG MAIL_PASSWORD
ENV MAIL_PASSWORD=$MAIL_PASSWORD

ARG DB_HOST
ENV DB_HOST=$DB_HOST

ARG DB_PORT
ENV DB_PORT=$DB_PORT

ARG DB_USER
ENV DB_USER=$DB_USER

ARG DB_PASS
ENV DB_PASS=$DB_PASS

ARG SPRING_PROFILES_ACTIVE
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE

ARG PASS_CERT_TEMPLATE_BACK
ENV PASS_CERT_TEMPLATE_BACK=$PASS_CERT_TEMPLATE_BACK

ARG S3_ACCESS_KEY
ENV S3_ACCESS_KEY=$S3_ACCESS_KEY

ARG S3_SECRET_KEY
ENV S3_SECRET_KEY=$S3_SECRET_KEY

ARG OPENAI_API_KEY
ENV OPENAI_API_KEY=$OPENAI_API_KEY

# Expone el puerto en el que se ejecuta la aplicación
EXPOSE 8443

# Comando para ejecutar la aplicación cuando se inicie el contenedor
CMD ["java", "-jar", "template-0.0.1-SNAPSHOT.jar"]