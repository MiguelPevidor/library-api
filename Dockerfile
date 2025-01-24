# Fase de build
FROM maven:3.8.8-amazoncorretto-21-al2023 as build
WORKDIR /build

# Copiar o código para o container
COPY . .

# Construir o projeto com Maven
RUN mvn clean package -DskipTests

# Fase de runtime
FROM amazoncorretto:21.0.5
WORKDIR /app

# Copiar o JAR gerado na fase de build
COPY --from=build /build/target/*.jar ./libraryapi.jar

# Expõe a porta para acesso
EXPOSE 8080

# Definição das variáveis de ambiente
ENV DATASOURCE_URL="jdbc:postgresql://localhost:5433/library"
ENV DATASOURCE_USERNAME="postgres"
ENV DATASOURCE_PASSWORD="postgres"
ENV GOOGLE_CLIENT_ID="client_id"
ENV GOOGLE_CLIENT_SECRET="client_secret"
ENV SPRING_PROFILES_ACTIVE="production"
ENV TZ="America/Sao_Paulo"

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "libraryapi.jar"]
