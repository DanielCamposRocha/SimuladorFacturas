package com.example.simuladorfacturas.front.usuarios.gui;

import com.example.simuladorfacturas.AplicacionUsuarios;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaMenuUsuario extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JLabel etiquetaMenuUsuario;
	private JTextPane textoNombreUsuario;
	private JButton btnCambiarContraseña;
	private JButton btnBorrarUsuario;
	private JButton btnCerrarSesion;
	private AplicacionUsuarios app;
	private String nombreUsuario;

	public VentanaMenuUsuario(AplicacionUsuarios app, String nombreUsuario) {
		this.app = app;
		this.nombreUsuario = nombreUsuario;

		setTitle("Aplicación usuarios");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 325, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		etiquetaMenuUsuario = new JLabel("Menú de usuario:");
		etiquetaMenuUsuario.setFont(new Font("Tahoma", Font.BOLD, 16));
		etiquetaMenuUsuario.setBounds(10, 24, 147, 14);
		contentPane.add(etiquetaMenuUsuario);

		btnCambiarContraseña = new JButton("Cambiar contraseña");
		btnCambiarContraseña.setBounds(71, 98, 153, 23);
		btnCambiarContraseña.addActionListener(this);
		contentPane.add(btnCambiarContraseña);

		btnBorrarUsuario = new JButton("Borrar usuario");
		btnBorrarUsuario.setBounds(71, 132, 153, 23);
		btnBorrarUsuario.addActionListener(this);
		contentPane.add(btnBorrarUsuario);

		btnCerrarSesion = new JButton("Cerrar sesión");
		btnCerrarSesion.setBounds(183, 227, 116, 23);
		btnCerrarSesion.addActionListener(this);
		contentPane.add(btnCerrarSesion);

		textoNombreUsuario = new JTextPane();
		textoNombreUsuario.setEditable(false);
		textoNombreUsuario.setBounds(167, 24, 132, 20);
		textoNombreUsuario.setText(nombreUsuario);
		contentPane.add(textoNombreUsuario);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource().equals(btnCambiarContraseña)){
			app.mostrarVentanaCambiarContraseña(nombreUsuario);
		}
		if(e.getSource().equals(btnCerrarSesion)){
			app.cerrarSesion();
		}
		if(e.getSource().equals(btnBorrarUsuario)){
			app.mostrarVentanaBorrarUsuario(nombreUsuario);
		}
	}

}
