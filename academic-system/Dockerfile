# ==========================================
# ESTÁGIO 1: Compilação da Aplicação
# ==========================================
FROM maven:3.9.9-eclipse-temurin-25 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia apenas o arquivo pom.xml para baixar as dependências (otimiza o cache do Docker)
COPY pom.xml .

# Baixa as dependências do Maven sem compilar o código ainda
RUN mvn dependency:go-offline -B

# Copia todo o código fonte do projeto para dentro do container
COPY src ./src

# Compila o projeto e gera o arquivo JAR executável ignorando os testes temporariamente se necessário
RUN mvn clean package -DskipTests

# ==========================================
# ESTÁGIO 2: Ambiente de Execução (Runtime)
# ==========================================
FROM eclipse-temurin:25-jre

# Instala bibliotecas de sistema necessárias para o JavaFX rodar em ambientes Linux Linux/Container
RUN apt-get update && apt-get install -y \
    libgl1-mesa-glx \
    libgtk-3-0 \
    libgstreamer1.0-0 \
    libgstreamer-plugins-base1.0-0 \
    libxxf86vm1 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copia o arquivo JAR gerado no primeiro estágio para este estágio limpo
# Nota: Verifique se o nome do JAR gerado pelo seu pom.xml corresponde a 'academic-system-1.0-SNAPSHOT.jar'
COPY --from=build /app/target/academic-system-1.0-SNAPSHOT.jar app.jar

# Variável de ambiente para o JavaFX conseguir se conectar com a tela do seu computador hospedeiro
ENV DISPLAY=:0

# Comando para iniciar a aplicação através do Launcher/MainGUI
ENTRYPOINT ["java", "-cp", "app.jar", "org.example.academic.system.Main"]