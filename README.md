# Sistema de Alquiler de Habitaciones - API REST

Este proyecto es una **API RESTful** diseñada para gestionar el alquiler de habitaciones. La API permite registrar inquilinos, habitaciones, generar recibos de alquiler, y subir/descargar documentos relacionados con los inquilinos, como copias del DNI, utilizando **Amazon S3** como almacenamiento.

## Características

- **Gestión de Inquilinos**: Registro, edición, consulta y eliminación de inquilinos.
- **Gestión de Habitaciones**: Manejo de habitaciones con su precio, estado y número.
- **Recibos de Alquiler**: Generación de recibos vinculando inquilinos y habitaciones, con datos como monto, concepto y observaciones.
- **Subida y Descarga de Archivos PDF**: Los archivos se suben a AWS S3 con el número de DNI del inquilino como nombre.
- **Configuración de CORS**: Soporte para solicitudes desde clientes externos como Postman y aplicaciones frontend.
- **Seguridad con JWT (JSON Web Tokens)**:
  - Autenticación basada en tokens.
  - Rutas protegidas para operaciones que requieren autorización.
  - Funciones de login y registro con generación de tokens.

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.x** 
  - Spring Web
  - Spring Data JPA
  - Spring Security con JWT
- **MySQL** como base de datos
- **AWS S3** para almacenamiento de archivos
- **Docker** para contenedores (opcional para la base de datos)
- **Postman** para pruebas
- **Git** para control de versiones

## Pre requisitos

Asegúrate de tener instalados:

- **Java 17** o superior 
- **Maven**
- **Docker** (opcional, para la base de datos)
- **Cuenta de AWS** con un bucket S3 configurado
- **Archivo `.env`** con las siguientes variables de entorno:

```properties
BUCKET_NAME=nombre-del-bucket
REGION=us-east-1
AWS_KEY_ID=tu-access-key
AWS_SECRET_KEY=tu-secret-key
```
## Instalación

1. Clona el repositorio:
   
   ```git
   git clone https://github.com/tuusuario/sistema-alquiler-habitaciones.git
   ```
   
2. Configura el archivo `.env` en el directorio raíz:
   
    ```env
    BUCKET_NAME=nombre-del-bucket
    REGION=us-east-1
    AWS_KEY_ID=tu-access-key
    AWS_SECRET_KEY=tu-secret-key
    ```
    
3. Compila el proyecto:
   
   ```bash
   mvn clean install
   ```
   
4. Inicia el servidor:
   
    ```bash
   mvn spring-boot:run
   ```
    
5. (Opcional) Inicia la base de datos con Docker:
    
   ```bash
   docker-compose up -d
   ```
## Endpoints principales del API
### Autenticación y Usuarios

| Método   | Endpoint      | Descripción     |  Autenticación     |
|:------------|:-----------|:------------|:------------|
| `POST`     | `/auth/register`     | Registro de un nuevo usuario     |No     |
| `POST`   | `/auth/login`   | Autenticación y generación de un JWT Token   |No   |
| `GET`   | `/api/all-users`   | Ver todos los usuarios registrados	   |Sí (JWT)   |

### Habitaciones e inquilinos

| Método   | Endpoint      | Descripción     |  Autenticación     |
|:------------|:-----------|:------------|:------------|
| `GET`     | `/api/habitaciones`     | Listar todas las habitaciones     |Sí (JWT)     |
| `POST`   | `/api/habitaciones	`   | Registrar una nueva habitación   |Sí (JWT)   |
| `GET`   | `/api/inquilinos`   | Listar todos los inquilinos	   |Sí (JWT)   |
| `POST`   | `/api/inquilinos`   | Registrar un nuevo inquilino	   |Sí (JWT)   |

## Subida y Descarga de Archivos
 - **Subir archivo PDF (DNI del inquilino)**
     - **POST** `/api/files/upload`
     - **Params:** `dni=12345678`
     - **Body:** Archivo PDF como  `multipart/form-data`
 - **Descargar archivo PDF**
     - **GET** `/api/files/download`
     - **Params:** `dni=12345678`

## Ejemplo de Peticiones en Postman
**Subir archivo:**
  - **Método:** `POST`
  - **URL:** `http://localhost:8080/api/files/upload?dni=12345678`
  - **Headers:**
    
     ```bash
        Content-Type: multipart/form-data
      ```

 - **Body:**
   - **Tipo:** `form-data`
   - **Clave:** `file` (Archivo PDF)
     
**Descargar archivo:**
  - **Método:** `GET`
  - **URL:** `http://localhost:8080/api/files/upload?dni=12345678`

## Seguridad implementada

La seguridad del sistema se basa en JWT (JSON Web Tokens) para proteger los endpoints. A continuación, se describen los pasos principales para interactuar con el sistema:

 1. **Registro de usuario:**
     - **POST** `/auth/register`
     - **Ejemplo en Postman:**

       ```JSON
            {
             "username": "usuario1",
             "password": "contraseña123"
            }
       ```
            
 2. **Login:**
     - **POST** `/auth/login`
     - **Respuesta exitosa:**
       
       ```JSON
            {
             "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            }
       ```
 3. **Acceso a endpoints protegidos:**
     - **Agrega el token como un header `Authorization` con el prefijo `Bearer`:**
       
       ```makefile
            Authorization: Bearer <tu_token_generado>
       ```
       

## Ejemplo de Peticiones en Postman
**Subir archivo:**
  - **Método:** `POST`
  - **URL:** `http://localhost:8080/api/files/upload?dni=12345678`
  - **Headers:**
    
     ```bash
        Content-Type: multipart/form-data
      ```

 - **Body:**
   - **Tipo:** `form-data`
   - **Clave:** `file` (Archivo PDF)
     
**Descargar archivo:**
  - **Método:** `GET`
  - **URL:** `http://localhost:8080/api/files/upload?dni=12345678`

## Configuración de CORS
El sistema permite conexiones desde cualquier origen para desarrollo. La configuración se encuentra en `CorsConfig.java`:

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080") // Cambiar según el dominio
                .allowedMethods("*");
    }
}
```

## Base de Datos
El proyecto usa MySQL como base de datos. Si usas Docker, puedes inicializarla con el archivo `docker-compose.yml`. Credenciales de conexión configuradas en `application.properties`:

 ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/alquiler_habitaciones
        spring.datasource.username=root
        spring.datasource.password=tu_contraseña
 ```

## Autor
  - **Rick Kevin Saman Ramirez**
  - **Contacto:** rickx213@gmail.com

## Licencia
 Este proyecto está licenciado bajo la **MIT License** - consulta el archivo [LICENSE](LICENSE) para más detalles.
