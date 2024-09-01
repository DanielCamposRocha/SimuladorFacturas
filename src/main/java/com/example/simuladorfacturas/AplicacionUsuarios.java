package com.example.simuladorfacturas;

import com.example.simuladorfacturas.controlador.Controlador;
import com.example.simuladorfacturas.front.usuarios.gui.*;
import com.example.simuladorfacturas.objetos.Usuario;

import javax.swing.*;

public class AplicacionUsuarios {

	private VentanaInicioSesion ventanaInicioSesion;
	private VentanaCrearUsuario ventanaCrearUsuario;
	private VentanaMenuUsuario ventanaMenuUsuario;
	private VentanaCambiarContraseña ventanaCambiarContraseña;
	private VentanaBorrarUsuario ventanaBorrarUsuario;
	private static Usuario usuarioLogueado;

	public void ejecutar() {
		ventanaInicioSesion=new VentanaInicioSesion(this);
		ventanaInicioSesion.setVisible(true);
	}

	public void iniciarSesion(String nombreUsuario, String contrasenhaUsuario) {
		Usuario usuario=new Usuario(nombreUsuario,contrasenhaUsuario);
		Controlador.openConexion();
		if (Controlador.comprobarContrasenha(usuario)) {
			ventanaInicioSesion.setTextoUsuario("");
			ventanaInicioSesion.setTextoContraseña("");
			ventanaMenuUsuario = new VentanaMenuUsuario(this, nombreUsuario);
			ventanaMenuUsuario.setVisible(true);
			ventanaInicioSesion.setVisible(false);
			usuarioLogueado=Controlador.obtenerUsuario(nombreUsuario);
		} else {
			JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrecta");
		}
		Controlador.closeConexion();
	}

	public void cerrarSesion() {
		if(ventanaMenuUsuario!=null)ventanaMenuUsuario.dispose();
		if(ventanaBorrarUsuario!=null)ventanaBorrarUsuario.dispose();
		if(ventanaCambiarContraseña!=null)ventanaCambiarContraseña.dispose();
		usuarioLogueado=new Usuario();
	}

	public void crearUsuario(String nombre, String contraseña) {
		Controlador.openConexion();
		if(Controlador.obtenerUsuario(nombre)==null){
			Usuario nuevo= new Usuario(nombre,contraseña);
			Controlador.crearUsuario(nuevo);
		}else {
			JOptionPane.showMessageDialog(null, "Ya existe un usuario con ese nombre");
		}
		Controlador.closeConexion();
	}

	public void cambiarContraseña(String nuevaContrasenha) {
		Controlador.openConexion();
		usuarioLogueado.setContrasenha(nuevaContrasenha);
		if(Controlador.cambiarContrasenha(usuarioLogueado))	cerrarSesion();
		else{System.out.println("No se ha podido modificar la contraseña");	}
		Controlador.closeConexion();
	}

	public void borrarUsuario(String nombreUsuario) {
		Controlador.openConexion();
		// TODO: 02/09/2024 borrado de usuarios teniendo en cuenta la tabla con los cups
		cerrarSesion();
		Controlador.closeConexion();
	}

	public void mostrarVentanaCrearUsuario() {
		ventanaCrearUsuario=new VentanaCrearUsuario(this);
		ventanaCrearUsuario.setVisible(true);
	}


	public void mostrarVentanaCambiarContraseña(String nombreUsuario) {
		ventanaCambiarContraseña=new VentanaCambiarContraseña(this,nombreUsuario);
		ventanaCambiarContraseña.setVisible(true);
	}

	public void mostrarVentanaBorrarUsuario(String nombreUsuario) {
		ventanaBorrarUsuario=new VentanaBorrarUsuario(this,nombreUsuario);
		ventanaBorrarUsuario.setVisible(true);
	}


}
