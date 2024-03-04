# Use a imagem base do OpenJDK 17
FROM openjdk:17

# Defina o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o arquivo JAR do seu projeto para o diretório de trabalho no contêiner
COPY target/untitled.jar /app/rinha-java.jar

# Comando a ser executado quando o contêiner for iniciado
CMD ["java", "-jar", "rinha-java.jar"]
