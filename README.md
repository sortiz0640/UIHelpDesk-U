# 🖥️ UIHelpDesk-U: Interfaz Gráfica (JavaFX)

Este repositorio contiene la capa de presentación (Frontend) para el sistema **HelpDeskU**. Es una aplicación de escritorio desarrollada en **Java** utilizando **JavaFX** y FXML, diseñada para interactuar con la lógica de negocio del sistema de gestión de tickets.

## 📋 Descripción

La interfaz permite a los usuarios (Administradores, Funcionarios y Estudiantes) interactuar con el sistema de manera visual.

Sus funciones principales incluyen:

* **Inicio de Sesión:** Autenticación segura validando credenciales contra la base de datos.
* **Gestión de Tickets:** Creación de nuevos tickets, visualización de "Inbox" y detalles de tickets existentes.
* **Panel de Administración:** Gestión exclusiva para administradores de Usuarios y Departamentos (CRUD).
* **Visualización de Análisis:** Ventanas emergentes para ver el análisis de texto (Bag of Words) realizado por el backend sobre los tickets.

## 🛠️ Requisitos del Proyecto

Para ejecutar este proyecto necesitas:

* **Java JDK 24** (o superior compatible).
* **JavaFX SDK** (versión compatible con tu JDK).
* **Módulo de Lógica (HelpDeskU):** Este proyecto **depende** del `.jar` o del módulo compilado del backend (el otro repositorio).
* **Librerías Adicionales:**
    * `BCrypt` (para compatibilidad con la encriptación de usuarios).
    * `MySQL Connector/J` (para que la lógica pueda conectarse a la BD).

## 🚀 Configuración e Instalación

### 1. Vincular la Lógica de Negocio
Este proyecto **no funciona por sí solo**. Necesita tener acceso a las clases del paquete `cr.ac.ucenfotec.sortiz0640.bl` (Lógica y Entidades).

1.  Abre el proyecto en tu IDE (IntelliJ IDEA recomendado).
2.  Ve a **Project Structure** > **Modules** > **Dependencies**.
3.  Agrega el módulo o el archivo `.jar` del proyecto **HelpDeskU** (Backend).

### 2. Configurar JavaFX
Asegúrate de tener configurado el SDK de JavaFX en tu IDE y añade las opciones de VM si es necesario:
```bash
--module-path /ruta/a/tu/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
