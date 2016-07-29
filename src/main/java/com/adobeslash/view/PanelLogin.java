package com.adobeslash.view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class PanelLogin extends JPanel {
  private JPasswordField passwordField;

  public PanelLogin() {
    this.setBounds(0, 0, 408, 102);
    this.setLayout(null);

    JTextField loginField = new JTextField();
    loginField.setBounds(69, 11, 86, 20);
    this.add(loginField);
    loginField.setColumns(10);

    JLabel lblPassword = new JLabel("Password");
    lblPassword.setBounds(10, 48, 46, 14);
    this.add(lblPassword);

    JLabel lblLogin = new JLabel("Login");
    lblLogin.setBounds(10, 11, 46, 14);
    this.add(lblLogin);

    JButton btnConnection = new JButton("Connection");
    btnConnection.setBounds(69, 73, 86, 23);
    this.add(btnConnection);

    JButton btnBrowse = new JButton("Browse");
    btnBrowse.setBounds(311, 41, 89, 23);
    this.add(btnBrowse);

    JTextField txtPathItineraire = new JTextField();
    txtPathItineraire.setBounds(314, 12, 86, 20);
    this.add(txtPathItineraire);
    txtPathItineraire.setText("c:/");
    txtPathItineraire.setColumns(10);

    JRadioButton rdbtnMooveItineraire = new JRadioButton("Moove itineraire");
    rdbtnMooveItineraire.setBounds(189, 11, 103, 23);
    this.add(rdbtnMooveItineraire);

    JRadioButton rdbtnMooveLibre = new JRadioButton("Moove Libre");
    rdbtnMooveLibre.setBounds(190, 33, 83, 23);
    this.add(rdbtnMooveLibre);
    rdbtnMooveLibre.setSelected(true);

    passwordField = new JPasswordField();
    passwordField.setBounds(69, 42, 86, 20);
    add(passwordField);
  }
}
