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
periodo2 double,
margenp1 double,
margenp2 double,
costefijo double
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
create table festivos(
fecha date primary key
);
INSERT INTO festivos (fecha) values
('2022-01-01'),
('2022-01-06'),
('2022-04-15'),
('2022-08-15'),
('2022-10-12'),
('2022-11-01'),
('2022-12-06'),
('2022-12-08'),
('2023-04-07'),
('2023-05-01'),
('2023-08-15'),
('2023-10-12'),
('2023-11-01'),
('2023-12-06'),
('2023-12-08'),
('2023-12-25'),
('2024-01-01'),
('2024-03-29'),
('2024-05-01'),
('2024-08-15'),
('2024-10-12'),
('2024-11-01'),
('2024-12-06'),
('2024-12-25');
INSERT INTO Potencia (ano, periodo1, periodo2,margenp1,margenp2,costefijo) VALUES
(2021, 23.469833, 0.96113,7.202827,	0.463229,3.113000),
(2022, 22.988256, 0.93889,4.970533,	0.319666,3.113000),
(2023, 22.39314, 1.150425,2.989915,	0.192288,3.113000),
(2024, 22.401746, 0.776564,2.989915,0.192288,3.113000);