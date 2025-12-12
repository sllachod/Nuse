# Sistema de Donaciones - GUI Moderna

## Características de la GUI

### Diseño
- **Colores**: Rojo (#DC143C) y Negro (#141414) con acentos en gris
- **Estilo**: Moderno y minimalista
- **Componentes**: Swing con estilos personalizados

### Funcionalidades

#### Panel de Login
- Inicio de sesión para Donantes y Asociaciones
- Validación de credenciales con la base de datos
- Redirección automática según el tipo de usuario

#### Panel de Registro
- Registro de nuevos Donantes
- Registro de nuevas Asociaciones
- Validación de campos obligatorios
- Manejo de duplicados (usuario/email)

#### Panel de Donante
- Visualización de todas las donaciones del donante
- Crear nuevas donaciones (tipo, descripción, cantidad)
- Editar donaciones existentes
- Eliminar donaciones
- Tabla interactiva con código de colores

#### Panel de Asociación
- Visualización de todas las donaciones disponibles
- Recolectar donaciones parcial o totalmente
- Actualización automática de disponibilidad
- Registro de fecha de recolección

## Integración con la Base de Datos

La GUI utiliza correctamente:
- `UserService` para autenticación
- `DonorService` para operaciones de donantes
- `AssociationService` para operaciones de asociaciones
- `DonationService` para gestión de donaciones
- `DonationCollectionService` para recolección de donaciones

## Ejecución

Para ejecutar la aplicación:

1. Asegúrate de que la base de datos MySQL esté corriendo (usando Docker Compose)
2. Ejecuta la clase `Main.java` desde VS Code o tu IDE
3. La ventana principal se abrirá automáticamente

## Navegación

1. **Inicio**: Pantalla de Login
2. **Registro**: Crear nuevo usuario (Donante o Asociación)
3. **Dashboard Donante**: Gestión completa de donaciones
4. **Dashboard Asociación**: Visualización y recolección de donaciones disponibles
