package com.example.simuladorfacturas.front.usuarios.gui;

import com.example.simuladorfacturas.AplicacionUsuarios;
import com.example.simuladorfacturas.controlador.Controlador;
import javafx.application.Platform;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaInicioSesion extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField textoUsuario;
	private JTextField textoContraseña;
	private JButton btnIniciarSesion;
	private JButton btnCrearNuevoUsuario;
	private JButton btnvolver;
	private AplicacionUsuarios app;

	public VentanaInicioSesion(AplicacionUsuarios app) {
		this.app = app;

		setTitle("Aplicación usuarios");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 507, 376);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		JLabel etiquetaInicioSesion = new JLabel("Inicio de sesión");
		etiquetaInicioSesion.setFont(new Font("Tahoma", Font.BOLD, 18));
		etiquetaInicioSesion.setHorizontalAlignment(SwingConstants.CENTER);
		etiquetaInicioSesion.setBounds(97, 26, 270, 44);
		contentPane.add(etiquetaInicioSesion);

		JLabel etiquetaUsuario = new JLabel("Usuario:");
		etiquetaUsuario.setFont(new Font("Tahoma", Font.PLAIN, 12));
		etiquetaUsuario.setBounds(160, 90, 46, 14);
		contentPane.add(etiquetaUsuario);

		textoUsuario = new JTextField();
		textoUsuario.setBounds(160, 115, 149, 20);
		contentPane.add(textoUsuario);
		textoUsuario.setColumns(10);

		JLabel etiquetaContraseña = new JLabel("Contraseña:");
		etiquetaContraseña.setFont(new Font("Tahoma", Font.PLAIN, 12));
		etiquetaContraseña.setBounds(160, 146, 149, 14);
		contentPane.add(etiquetaContraseña);

		textoContraseña = new JTextField();
		textoContraseña.setColumns(10);
		textoContraseña.setBounds(160, 171, 149, 20);
		contentPane.add(textoContraseña);

		btnIniciarSesion = new JButton("Iniciar sesión");
		btnIniciarSesion.setBounds(176, 215, 118, 23);
		btnIniciarSesion.addActionListener(this);
		contentPane.add(btnIniciarSesion);

		btnCrearNuevoUsuario = new JButton("Crear nuevo usuario");
		btnCrearNuevoUsuario.setBounds(70, 275, 149, 23);
		btnCrearNuevoUsuario.addActionListener(this);
		contentPane.add(btnCrearNuevoUsuario);

		btnvolver = new JButton("Volver");
		btnvolver.setBounds(270, 275, 149, 23);
		btnvolver.addActionListener(this);
		contentPane.add(btnvolver);

	}


	public void setTextoUsuario(String s) {
		 textoUsuario.setText(s);
	}

	public void setTextoContraseña(String s) {
		textoContraseña.setText(s);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnCrearNuevoUsuario)){
			app.mostrarVentanaCrearUsuario();

		}
		if(e.getSource().equals(btnIniciarSesion)){
			app.iniciarSesion(textoUsuario.getText(),textoContraseña.getText());

		}
		if(e.getSource().equals(btnvolver)){
			Platform.runLater(() -> {

				app.stage.setOpacity(1);  // Mostrar el Stage de JavaFX
				app.cerrarVentanaInicioSesion();

			});

		}
	}
}
