# Nuse - Sistema de Gestión de Donaciones

Sistema Java para gestionar donaciones entre donantes y asociaciones benéficas.

## Requisitos Previos
- Java 24+
- Maven 3.9+
- Docker Desktop

## Inicio Rápido

### 1. Levantar la Base de Datos
```powershell
docker compose up -d
```

### 2. Crear las Tablas
```powershell
docker exec -i nuse-mysql mysql -unuse_user -pnuse_pass donation_db < src/main/resources/sql/schema.sql
```

### 3. Compilar
```powershell
mvn clean install
```

### 4. Ejecutar
```powershell
mvn exec:java -Dexec.mainClass="org.unsa.Main"
```

