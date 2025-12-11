# Nuse - Sistema de Gestión de Donaciones

## Descripción
Aplicación Java para gestionar donaciones entre donantes y asociaciones benéficas.

## Requisitos
- Java 24 (o compatible)
- Maven 3.9+
- Docker Desktop
- MySQL 8.0 (vía Docker)

## Configuración Inicial

### 1. Variables de Entorno (.env)
El archivo `.env` ya está configurado con:
```
DB_URL=jdbc:mysql://localhost:3306/donation_db
DB_USER=nuse_user
DB_PASSWORD=nuse_pass
```

### 2. Levantar la Base de Datos
```powershell
# Iniciar contenedor MySQL
docker compose up -d

# Verificar que está corriendo
docker ps
```

### 3. Crear las Tablas
```powershell
# Ejecutar el schema SQL en el contenedor
docker exec -i nuse-mysql mysql -unuse_user -pnuse_pass donation_db < src/main/resources/sql/schema.sql
```

Alternativamente, usa MySQL Workbench o DBeaver:
- Host: `localhost`
- Puerto: `3306`
- Usuario: `nuse_user`
- Contraseña: `nuse_pass`
- Database: `donation_db`

Luego copia el contenido de `src/main/resources/sql/schema.sql` y ejecútalo.

### 4. Compilar el Proyecto
```powershell
mvn clean install
```

### 5. Ejecutar la Aplicación
```powershell
mvn exec:java -Dexec.mainClass="org.unsa.Main"
```

La aplicación primero probará la conexión a la BD y luego lanzará la GUI.

## Estructura del Proyecto

```
src/main/java/org/unsa/
├── Main.java                 # Punto de entrada
├── GlobalConstants.java       # Constantes de UI
├── models/                    # Entidades (User, Donor, Association, Donation, DonationCollection)
├── dao/                       # Data Access Objects (acceso a BD)
│   ├── UserDAO
│   ├── DonorDAO
│   ├── AssociationDAO
│   ├── DonationDAO
│   └── DonationCollectionDAO
├── services/                  # Lógica de negocio
│   ├── UserService
│   ├── DonorService
│   ├── AssociationService
│   ├── DonationService
│   └── DonationCollectionService
└── utils/
    ├── DBConnection          # Gestor de conexiones
    └── EnvLoader             # Lector de variables de entorno
```

## Funcionalidades

### Para Donantes
- Registrarse como donante
- Crear donaciones (tipo, descripción, cantidad)
- Editar/eliminar donaciones propias
- Ver historial de donaciones recolectadas

### Para Asociaciones
- Registrarse como asociación
- Ver donaciones disponibles
- Recolectar donaciones (con transacción atómica)
- Ver historial de donaciones recolectadas

### Autenticación
- Login con username y password
- Validación de credenciales
- Retorna el tipo de usuario (donor/association)

## Troubleshooting

**Error: "Cannot connect to database"**
- Verifica que Docker está corriendo: `docker ps`
- Verifica que el contenedor está activo: `docker compose ps`
- Reinicia: `docker compose restart`

**Error: "Table doesn't exist"**
- Ejecuta el schema SQL nuevamente
- Verifica que la BD `donation_db` fue creada: `docker exec nuse-mysql mysql -unuse_user -pnuse_pass -e "SHOW DATABASES;"`

**Error: "Access denied"**
- Verifica las credenciales en `.env`
- Verifica que coinciden con las variables en `compose.yml`

## Rama Actual
Esta rama (`dao+services`) contiene la lógica de acceso a datos y servicios. 
Para mergear a `main`:
```powershell
git checkout main
git merge dao+services
```

## Próximos Pasos
- [ ] Implementar GUI con Swing (MainFrame)
- [ ] Mejorar seguridad (hashear contraseñas con BCrypt)
- [ ] Añadir validaciones más robustas
- [ ] Crear tests unitarios
- [ ] Documentar APIs REST (si aplica)
