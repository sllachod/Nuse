# Nuse - Sistema de Gestión de Donaciones

Sistema Java para gestionar donaciones entre donantes y asociaciones benéficas.

## Requisitos Previos
- Docker Desktop (WSL2 en Windows)
- Java 21+
- Maven 3.9+

## Configuración (Docker)

1) Copia `.env.example` a `.env` y ajusta tu contraseña si la cambias:
```
DB_URL=jdbc:mysql://localhost:3307/donation_db
DB_USER=nuse_user
DB_PASSWORD=nuse_pass
```

2) Levanta MySQL en Docker (mapea a puerto 3307):
```powershell
docker compose up -d
```

3) Crear/recargar el schema (opcional si ya existe):
```powershell
docker exec -i nuse-mysql mysql -unuse_user -pnuse_pass -e "DROP DATABASE IF EXISTS donation_db; CREATE DATABASE donation_db;"
Get-Content src/main/resources/sql/schema.sql | docker exec -i nuse-mysql mysql -unuse_user -pnuse_pass donation_db
```

4) Compilar y ejecutar la app:
```powershell
mvn compile exec:java "-Dexec.mainClass=org.unsa.Main"
```

Deberías ver: `Database connected successfully!`

