# 📦 Gestor de Inventarios - Aplicación de Escritorio

**Gestor de Inventarios** es una aplicación de escritorio desarrollada en **Java con Swing** y **Spring Boot**, diseñada para pequeñas empresas que desean administrar sus productos, ventas y reportes de manera eficiente. Utiliza **SQLite** como base de datos para un almacenamiento ligero y local.

---

## 🚀 Tecnologías Utilizadas

- **Java (Swing)** → Interfaz gráfica de usuario.
- **Spring Boot** → Backend para la gestión de datos.
- **SQLite** → Base de datos local.
- **iText** → Generación de reportes en PDF.

---

## 📂 Estructura del Proyecto

```
gestor-inventarios/
│── src/
│   ├── main/
│   │   ├── database/        # Configuración de SQLite
│   │   ├── java/com/gestorinventarios/
│   │   │   ├── backend/     # Lógica de negocio
│   │   │   │   ├── controller/  # Controladores
│   │   │   │   ├── model/       # Modelos de datos
│   │   │   │   ├── repository/  # Repositorios JPA
│   │   │   │   ├── service/     # Servicios de negocio
│   │   │   ├── frontend/
│   │   │   │   ├── ui/       # Interfaz de usuario (Java Swing)
│   │   ├── resources/
│   │   │   ├── static/
│   │   │   ├── templates/
│── README.md                 # Documentación
```

---

## 🎨 Módulos y Funcionalidades

### 1️⃣ **Gestión de Productos**
- Agregar, editar y eliminar productos.
- Buscar productos por nombre o categoría.
- Mostrar inventario en una tabla con filtros dinámicos.

### 2️⃣ **Gestión de Ventas**
- Registrar nuevas ventas.
- Asignar productos a una venta.
- Calcular totales automáticamente.

### 3️⃣ **Reportes en PDF**
- Generar reportes de inventario.
- Resumen de ventas por fecha.

### 4️⃣ **Autenticación de Usuarios**
- Registro y login con SQLite.
- Permisos diferenciados para administradores y empleados.

---

## 🖥️ Interfaz de Usuario

La aplicación usa **Java Swing** para la UI, con diseño optimizado para facilidad de uso:
- **Menús** para navegar entre secciones.
- **Botones y listas desplegables** para seleccionar opciones.
- **Gráficos estadísticos** con resumen de ventas e inventario.

```java
JButton btnAgregar = new JButton("Agregar Producto");
btnAgregar.addActionListener(e -> agregarProducto());
```

---

## 📊 Base de Datos (SQLite)

La base de datos almacena productos, ventas y usuarios. **Ejemplo de tabla `productos`**:
```sql
CREATE TABLE productos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    categoria TEXT NOT NULL,
    cantidad INTEGER NOT NULL,
    precio REAL NOT NULL
);
```

---

## 🔧 Instalación y Ejecución

1️⃣ Clona el repositorio:
```sh
git clone https://github.com/Pabloob/gestor-inventarios.git
```

2️⃣ Instala las dependencias con Maven:
```sh
mvn clean install
```

3️⃣ Ejecuta la aplicación:
```sh
mvn spring-boot:run
```

4️⃣ Inicia la interfaz gráfica:
```sh
java -jar target/gestor-inventarios.jar
```

---

## 📢 Despliegue y Distribución

Para generar un instalador ejecutable:
1. Compilar el proyecto: `mvn package`
2. Usar **Launch4j** o **Inno Setup** para crear un `.exe` instalable en Windows.

---

## 📌 Autor

**Pablo Orbea Benitez** – [GitHub](https://github.com/Pabloob) | [LinkedIn](https://www.linkedin.com/in/pabloob5)

🛒 ¡Optimiza la gestión de tu inventario con esta aplicación! 🚀
