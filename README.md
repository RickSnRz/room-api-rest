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
