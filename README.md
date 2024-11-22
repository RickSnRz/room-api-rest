# Sistema de Alquiler de Habitaciones - API REST

Este proyecto es una **API RESTful** diseñada para gestionar el alquiler de habitaciones. La API permite registrar inquilinos, habitaciones, generar recibos de alquiler, y subir/descargar documentos relacionados con los inquilinos, como copias del DNI, utilizando **Amazon S3** como almacenamiento.

## Características

- **Gestión de Inquilinos**: Registro, edición, consulta y eliminación de inquilinos.
- **Gestión de Habitaciones**: Manejo de habitaciones con su precio, estado y número.
- **Recibos de Alquiler**: Generación de recibos vinculando inquilinos y habitaciones, con datos como monto, concepto y observaciones.
- **Subida y Descarga de Archivos PDF**: Los archivos se suben a AWS S3 con el número de DNI del inquilino como nombre.
- **Configuración de CORS**: Soporte para solicitudes desde clientes externos como Postman y aplicaciones frontend.

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.x**
  - Spring Web
  - Spring Data JPA
- **MySQL** como base de datos
- **AWS S3** para almacenamiento de archivos
- **Docker** para contenedores (opcional para la base de datos)
- **Postman** para pruebas
- **Git** para control de versiones

## Prerrequisitos

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
   
2. onfigura el archivo `.env` en el directorio raíz:
   
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
## Endpoints de la API
### Inquilinos
 - **Registrar un inquilino**
     - **POST** `/api/inquilinos`
     - **Body:**

        ```JSON
        {
          "dni": "12345678",
          "nombre": "Juan Pérez",
          "telefono": "987654321"
       }
        ```

 - **Listar inquilinos**
    - **GET** `/api/inquilinos`

### Habitaciones
 - **Registrar una habitación**
     - **POST** `/api/habitaciones`
     - **Body:**

        ```JSON
        {
          "numero": 101,
          "precio": 600,
          "estado": "Disponible"
       }
        ```

 - **Listar habitaciones**
    - **GET** `/api/habitaciones`

### Recibos
 - **Crear un recibo**
     - **POST** `/api/recibos`
     - **Body:**

        ```JSON
        {
          "inquilinoId": 1,
          "habitacionId": 2,
          "monto": 600,
          "concepto": "Renta mensual",
          "observaciones": "Pago puntual"
       }
        ```

 - **Listar recibos**
    - **GET** `/api/recibos`

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

## Copyright

Copyright (c) [2024] [Rick Kevin Saman Ramirez]. Todos los derechos reservados.

Este proyecto está protegido por derechos de autor y no puede ser utilizado, copiado o distribuido sin el permiso expreso del titular de los derechos de autor.
