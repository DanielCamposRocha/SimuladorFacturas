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
INSERT INTO Potencia (ano, periodo1, periodo2) VALUES
(2021, 23.469833, 0.96113),
(2022, 22.988256, 0.93889),
(2023, 22.39314, 1.150425),
(2024, 22.401746, 0.776564);