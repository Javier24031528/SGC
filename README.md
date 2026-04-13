# Sistema de Gestión de Calificaciones (SGC)

## Descripción
Aplicación para el registro, consulta y administración de calificaciones de alumnos en distintas asignaturas, desarrollada con JavaFX y MySQL.

## Características Principales
- **Autenticación:** Login con validación de contraseña mediante el algoritmo **SHA-1**.
- **Gestión Integral:** Registro de alumnos, materias y calificaciones.
- **Cálculo de Promedios:** Determinación automática del estado (Aprobado/Reprobado).
- **Reportes:** Exportación de datos en formatos **PDF** (iText) y **CSV** (OpenCSV).
- **Arquitectura MVC:** Separación clara entre modelos, vistas y controladores.

## Patrones de Diseño Aplicados
1.  **Singleton:** En `DatabaseConnection` para asegurar una única conexión a la base de datos.
2.  **DAO (Data Access Object):** Con implementación de **Genéricos (`GenericDAO<T>`)** para abstraer el acceso a datos.
3.  **Observer:** La interfaz principal observa los cambios en las calificaciones para actualizarse automáticamente.
4.  **Strategy:** Implementado para permitir diferentes métodos de cálculo de promedios (Simple y Ponderado).

## Requisitos
- **Java 23** (o compatible con JDK 17+).
- **MySQL Server** (XAMPP/WAMP recomendado).
- **Maven** para la gestión de dependencias.

## Instalación y Ejecución
1.  Clonar el repositorio.
2.  Importar la base de datos: Ejecutar el script `schema.sql` en MySQL.
3.  Configurar credenciales en `src/main/java/org/example/sgc/util/DatabaseConnection.java`.
4.  Ejecutar el programa:
    ```bash
    mvn javafx:run
    ```
    O ejecutar directamente la clase `org.example.sgc.AppLauncher`.

## Credenciales por defecto
- **Usuario:** `admin`
- **Contraseña:** `admin123`

## Autores
- [Tu Nombre/Javier]
