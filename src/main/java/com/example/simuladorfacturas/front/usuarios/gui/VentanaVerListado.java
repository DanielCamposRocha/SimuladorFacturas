package com.example.simuladorfacturas.front.usuarios.gui;

import com.example.simuladorfacturas.AplicacionUsuarios;
import com.example.simuladorfacturas.controlador.Controlador;
import com.example.simuladorfacturas.front.HelloController;
import javafx.application.Platform;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaVerListado extends JFrame implements ActionListener {
    private AplicacionUsuarios app;
    private HelloController helloController;
    private String nombreUsuario;
    private JPanel contentPane;
    private JScrollPane jscrollPane;
    private JLabel etiquetaMenuUsuario;
    private JTextPane textoNombreUsuario;
    private JButton btnVolver;
    public VentanaVerListado(AplicacionUsuarios app, String nombreUsuario) {
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

        textoNombreUsuario = new JTextPane();
        textoNombreUsuario.setEditable(false);
        textoNombreUsuario.setBounds(167, 24, 132, 20);
        textoNombreUsuario.setText(nombreUsuario);
        contentPane.add(textoNombreUsuario);

        btnVolver= new JButton("Volver");
        btnVolver.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnVolver.setBounds(10, 50, 147, 14);
        btnVolver.addActionListener(this);
        contentPane.add(btnVolver);

        JList<String> list =Controlador.listaPuntos(nombreUsuario);
        jscrollPane = new JScrollPane(list);
        jscrollPane.setBounds(71, 98, 153, 23);
        add(jscrollPane);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {  // Detecta un solo clic
                    // Obtener el valor seleccionado de la lista
                    JList<String> source = (JList<String>) e.getSource();
                    String selectedItem = source.getSelectedValue();

                    // Cambiar el texto del Label y del Button en JavaFX
                    Platform.runLater(() -> {
                        app.helloController.usuarioLogueado(nombreUsuario,selectedItem,source);  // Cambiar texto basado en el valor seleccionado
                        app.stage.setOpacity(1);  // Mostrar el Stage de JavaFX
                        app.cerrarVentanaUsuario();

                    });

                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(btnVolver)){
            app.mostrarVentanaUsuario();
            this.dispose();

        }

    }
}
