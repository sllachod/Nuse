# Nuse - Sistema de Gestión de Donaciones

Sistema Java para gestionar donaciones entre donantes y asociaciones benéficas.

## Requisitos Previos
- Java 21+
- Maven 3.9+
- MySQL 8.0

## Inicio Rápido

### 1. Crear la Base de Datos
```powershell
# Crear la base de datos
mysql -uroot -proot -e "CREATE DATABASE IF NOT EXISTS donation_db;"

# Crear las tablas
mysql -uroot -proot donation_db < src/main/resources/sql/schema.sql
```

### 2. Compilar
```powershell
mvn clean install
```

### 3. Ejecutar
```powershell
mvn exec:java -Dexec.mainClass="org.unsa.Main"
```

