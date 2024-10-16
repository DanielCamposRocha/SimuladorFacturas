package com.example.simuladorfacturas.front.usuarios.gui;


import com.example.simuladorfacturas.AplicacionUsuarios;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaCambiarContraseña extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JLabel etiquetaNuevaContraseña;
	private JTextField textoNuevoContraseña;
	private JButton btnCambiarContraseña;
	private JButton btnCancelar;
	private AplicacionUsuarios app;
	private String nombreUsuario;

	public VentanaCambiarContraseña(AplicacionUsuarios app, String nombreUsuario) {
		this.app = app;
		this.nombreUsuario = nombreUsuario;
		setTitle("Aplicación usuarios");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 250, 188);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		etiquetaNuevaContraseña = new JLabel("Escribe tu nueva contraseña:");
		etiquetaNuevaContraseña.setFont(new Font("Tahoma", Font.BOLD, 12));
		etiquetaNuevaContraseña.setBounds(21, 22, 192, 14);
		contentPane.add(etiquetaNuevaContraseña);

		textoNuevoContraseña = new JTextField();
		textoNuevoContraseña.setBounds(21, 58, 179, 20);
		contentPane.add(textoNuevoContraseña);
		textoNuevoContraseña.setColumns(10);

		btnCambiarContraseña = new JButton("Cambiar");
		btnCambiarContraseña.setBounds(124, 111, 89, 23);
		btnCambiarContraseña.addActionListener(this);
		contentPane.add(btnCambiarContraseña);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(10, 111, 89, 23);
		btnCancelar.addActionListener(this);
		contentPane.add(btnCancelar);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnCambiarContraseña)){
			app.cambiarContraseña(textoNuevoContraseña.getText());
			this.dispose();
		}
		if(e.getSource().equals(btnCancelar)){
			app.mostrarVentanaUsuario();
			this.dispose();
		}
	}

}
