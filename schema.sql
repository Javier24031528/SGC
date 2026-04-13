CREATE DATABASE IF NOT EXISTS sgc_db;
USE sgc_db;

CREATE TABLE IF NOT EXISTS alumnos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS materias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS calificaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    alumno_id INT,
    materia_id INT,
    nota DOUBLE,
    periodo VARCHAR(50),
    FOREIGN KEY (alumno_id) REFERENCES alumnos(id) ON DELETE CASCADE,
    FOREIGN KEY (materia_id) REFERENCES materias(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    rol VARCHAR(20) DEFAULT 'DOCENTE'
);

-- Insertar usuario admin por defecto (pass: admin123 -> SHA1)
-- SHA1('admin123') = 7c4a8d09ca3762af61e59520943dc26494f8941b
INSERT INTO usuarios (username, password, rol) VALUES ('admin', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'ADMIN') ON DUPLICATE KEY UPDATE id=id;
