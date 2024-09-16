drop database if exists FacturaLuz;
create database FacturaLuz;
use FacturaLuz;
Create table precios(
fecha datetime,
precio double,
verano boolean,
primary key (fecha, verano)
);
create table Potencia(
ano int primary key,
periodo1 double,
periodo2 double
);
create table Usuarios(
nombre varchar(50) primary Key,
password_hash VARCHAR(255)
);
CREATE TABLE UsuarioTablas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50),
    CUPS VARCHAR(50),
    clave VARCHAR(50),
    FOREIGN KEY (nombre_usuario) REFERENCES Usuarios(nombre),
    UNIQUE(nombre_usuario, clave)
);
Create table precioMayorista(
fecha datetime,
precio double,
verano boolean,
primary key (fecha, verano)
);
INSERT INTO Potencia (ano, periodo1, periodo2) VALUES
(2021, 23.469833, 0.96113),
(2022, 22.988256, 0.93889),
(2023, 22.39314, 1.150425),
(2024, 22.401746, 0.776564);