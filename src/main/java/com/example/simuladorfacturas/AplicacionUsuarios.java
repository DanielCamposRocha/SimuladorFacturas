package com.example.simuladorfacturas;

import com.example.simuladorfacturas.controlador.Controlador;
import com.example.simuladorfacturas.front.HelloController;
import com.example.simuladorfacturas.front.usuarios.gui.*;
import com.example.simuladorfacturas.objetos.Usuario;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.Connection;

public class AplicacionUsuarios {
	public HelloController helloController;
	public Stage stage;
	private VentanaInicioSesion ventanaInicioSesion;
	private VentanaCrearUsuario ventanaCrearUsuario;
	private VentanaMenuUsuario ventanaMenuUsuario;
	private VentanaCambiarContraseña ventanaCambiarContraseña;
	private VentanaBorrarUsuario ventanaBorrarUsuario;
	private VentanaVerListado ventanaVerListado;
	private static Usuario usuarioLogueado;

	public AplicacionUsuarios(Stage stage, HelloController helloController) {
		this.stage = stage;
		this.helloController=helloController;

	}

	public static Usuario getUsuarioLogueado() {
		return usuarioLogueado;
	}


	public void ejecutar() {
		ventanaInicioSesion=new VentanaInicioSesion(this);
		ventanaInicioSesion.setVisible(true);
	}

	public void iniciarSesion(String nombreUsuario, String contrasenhaUsuario) {
		Usuario usuario=new Usuario(nombreUsuario,contrasenhaUsuario);

		if (Controlador.comprobarContrasenha(usuario)) {
			ventanaInicioSesion.setTextoUsuario("");
			ventanaInicioSesion.setTextoContraseña("");
			ventanaMenuUsuario = new VentanaMenuUsuario(this, nombreUsuario,stage,helloController);
			ventanaMenuUsuario.setVisible(true);
			ventanaInicioSesion.setVisible(false);
			usuarioLogueado=Controlador.obtenerUsuario(nombreUsuario);

		} else {
			JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrecta");
		}

	}

	public void cerrarSesion() {
		if(ventanaMenuUsuario!=null)ventanaMenuUsuario.dispose();
		if(ventanaBorrarUsuario!=null)ventanaBorrarUsuario.dispose();
		if(ventanaCambiarContraseña!=null)ventanaCambiarContraseña.dispose();
		usuarioLogueado=new Usuario();

	}

	public void crearUsuario(String nombre, String contraseña) {

		if(Controlador.obtenerUsuario(nombre)==null){
			Usuario nuevo= new Usuario(nombre,contraseña);
			Controlador.crearUsuario(nuevo);
		}else {
			JOptionPane.showMessageDialog(null, "Ya existe un usuario con ese nombre");
		}

	}

	public void cambiarContraseña(String nuevaContrasenha) {

		usuarioLogueado.setContrasenha(nuevaContrasenha);
		if(Controlador.cambiarContrasenha(usuarioLogueado))	cerrarSesion();
		else{System.out.println("No se ha podido modificar la contraseña");	}

	}

	public void borrarUsuario(String nombreUsuario) {
		// TODO: 02/09/2024 borrado de usuarios teniendo en cuenta la tabla con los cups
		cerrarSesion();
	}

	public void mostrarVentanaCrearUsuario() {
		ventanaCrearUsuario=new VentanaCrearUsuario(this);
		ventanaCrearUsuario.setVisible(true);
	}


	public void mostrarVentanaCambiarContraseña(String nombreUsuario) {
		ventanaCambiarContraseña=new VentanaCambiarContraseña(this,nombreUsuario);
		ventanaMenuUsuario.setVisible(false);
		ventanaCambiarContraseña.setVisible(true);
	}

	public void mostrarVentanaBorrarUsuario(String nombreUsuario) {
		ventanaBorrarUsuario=new VentanaBorrarUsuario(this,nombreUsuario);
		ventanaMenuUsuario.setVisible(false);
		ventanaBorrarUsuario.setVisible(true);
	}


	public void mostrarVentanaVerListado(VentanaMenuUsuario ventanaMenuUsuario, String nombreUsuario) {
		ventanaVerListado= new VentanaVerListado(this,nombreUsuario);
		ventanaMenuUsuario.setVisible(false);
		ventanaVerListado.setVisible(true);
	}

	public void mostrarVentanaUsuario() {
		ventanaMenuUsuario.setVisible(true);
	}
	public void cerrarVentanaUsuario(){ventanaVerListado.setVisible(false);}

	public void cerrarVentanaInicioSesion() {ventanaInicioSesion.setVisible(false);	}
}
