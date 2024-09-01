package com.example.simuladorfacturas.front.usuarios.gui;

import com.example.simuladorfacturas.AplicacionUsuarios;
import com.example.simuladorfacturas.validadores.Validaciones;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaCrearUsuario extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JLabel etiquetaCrearUsuario;
	private JLabel etiquetaNombre;
	private JLabel etiquetaContraseña;
	private JTextField textoNombre;
	private JTextField textoContraseña;
	private JButton btnCrear;
	private JButton btnCancelar;
	private AplicacionUsuarios app;

	public VentanaCrearUsuario(AplicacionUsuarios app) {
		this.app = app;
		setTitle("Aplicación usuarios");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 322, 385);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		etiquetaNombre = new JLabel("Nombre:");
		etiquetaNombre.setFont(new Font("Tahoma", Font.PLAIN, 12));
		etiquetaNombre.setBounds(65, 56, 68, 14);
		contentPane.add(etiquetaNombre);

		etiquetaCrearUsuario = new JLabel("CREAR USUARIO");
		etiquetaCrearUsuario.setBounds(83, 11, 154, 20);
		etiquetaCrearUsuario.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPane.add(etiquetaCrearUsuario);

		textoNombre = new JTextField();
		textoNombre.setBounds(65, 81, 214, 20);
		contentPane.add(textoNombre);
		textoNombre.setColumns(10);

		etiquetaContraseña = new JLabel("Contraseña:");
		etiquetaContraseña.setFont(new Font("Tahoma", Font.PLAIN, 12));
		etiquetaContraseña.setBounds(65, 112, 68, 14);
		contentPane.add(etiquetaContraseña);

		textoContraseña = new JTextField();
		textoContraseña.setColumns(10);
		textoContraseña.setBounds(65, 137, 214, 20);
		contentPane.add(textoContraseña);

		btnCrear = new JButton("Crear");
		btnCrear.setBounds(172, 299, 89, 23);
		btnCrear.addActionListener(this);
		contentPane.add(btnCrear);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(39, 299, 89, 23);
		btnCancelar.addActionListener(this);
		contentPane.add(btnCancelar);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnCrear)){
			String nombre= Validaciones.validarNombre(textoNombre.getText());
			String contra=textoContraseña.getText();
			if(nombre.length()>0 && Validaciones.contraIsValid(contra)){
				app.crearUsuario(nombre,contra);
				this.dispose();
			}
		}
		if(e.getSource().equals(btnCancelar)){
			this.dispose();
		}
	}
}
