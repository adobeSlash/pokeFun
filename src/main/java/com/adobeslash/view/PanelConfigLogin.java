package com.adobeslash.view;

import java.awt.Choice;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;

public class PanelConfigLogin extends JPanel {
    public PanelConfigLogin(final AppView view) {
        PokemonId[] AllPkmn = POGOProtos.Enums.PokemonIdOuterClass.PokemonId.values();
        setLayout(null);

        final JTextField loginField = new JTextField();
        loginField.setBounds(69, 11, 86, 20);
        this.add(loginField);
        loginField.setColumns(10);

        final JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(69, 42, 86, 20);
        add(passwordField);

        JButton btnConnection = new JButton("Connection");
        btnConnection.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    view.control.startFarmerBot(loginField.getText(), String.valueOf(passwordField.getPassword()));
                    view.changePanel(new MapView(view));
                } catch (LoginFailedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (RemoteServerException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        btnConnection.setBounds(69, 73, 110, 23);
        this.add(btnConnection);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(10, 48, 46, 14);
        this.add(lblPassword);

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setBounds(10, 11, 46, 14);
        this.add(lblLogin);

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

        JLabel lblThrowPcBelow = new JLabel("Transfer tout en dessous de ");
        lblThrowPcBelow.setBounds(14, 107, 147, 30);
        add(lblThrowPcBelow);

        JSpinner spinner = new JSpinner();
        spinner.setBounds(160, 112, 55, 20);
        add(spinner);

        JLabel lblPc = new JLabel("PC");
        lblPc.setBounds(225, 115, 26, 14);
        add(lblPc);

        final Choice choice = new Choice();
        choice.setBounds(95, 174, 136, 23);
        for (PokemonId p : AllPkmn)
            choice.addItem(p.name());
        add(choice);

        JLabel lblKeep = new JLabel("Keep Exception");
        lblKeep.setBounds(24, 244, 96, 14);
        add(lblKeep);

        JLabel lblPkmnGarder = new JLabel("Pkmn");
        lblPkmnGarder.setBounds(14, 174, 74, 19);
        add(lblPkmnGarder);

        final List list = new List();
        list.setBounds(24, 264, 110, 99);
        add(list);

        JButton btnKeep = new JButton("keep");
        btnKeep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        btnKeep.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                list.add(choice.getSelectedItem());
                choice.remove(choice.getSelectedIndex());
            }
        });
        btnKeep.setBounds(252, 140, 74, 23);
        add(btnKeep);

        final List list_1 = new List();
        list_1.setBounds(197, 264, 110, 99);
        add(list_1);

        JLabel lblThrowException = new JLabel("throw Exception");
        lblThrowException.setBounds(180, 244, 96, 14);
        add(lblThrowException);

        JButton btnThrow = new JButton("throw");
        btnThrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                list_1.add(choice.getSelectedItem());
                choice.remove(choice.getSelectedIndex());
            }
        });
        btnThrow.setBounds(252, 174, 74, 23);
        add(btnThrow);

        JButton btnRemoveThisThrow = new JButton("Remove this throw");
        btnRemoveThisThrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                choice.add(list_1.getSelectedItem() != null ? list_1.getSelectedItem() : list_1.getItem(0));
                list_1.remove(list_1.getSelectedItem() != null ? list_1.getSelectedIndex() : 0);
            }
        });
        btnRemoveThisThrow.setBounds(180, 369, 150, 23);
        add(btnRemoveThisThrow);

        JButton btnRemoveThisKeep = new JButton("Remove this keep");
        btnRemoveThisKeep.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                choice.add(list.getSelectedItem() != null ? list.getSelectedItem() : list.getItem(0));
                list.remove(list.getSelectedItem() != null ? list.getSelectedIndex() : 0);
            }
        });
        btnRemoveThisKeep.setBounds(10, 369, 137, 23);
        add(btnRemoveThisKeep);

        JLabel lblToEvolve = new JLabel("To evolve");
        lblToEvolve.setBounds(349, 244, 96, 14);
        add(lblToEvolve);

        final List list_2 = new List();
        list_2.setBounds(349, 263, 110, 99);
        add(list_2);

        JButton btnRemoveThisEvolve = new JButton("Remove this evolve");
        btnRemoveThisEvolve.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                choice.add(list_2.getSelectedItem() != null ? list_2.getSelectedItem() : list_2.getItem(0));
                list_2.remove(list_2.getSelectedItem() != null ? list_2.getSelectedIndex() : 0);
            }
        });
        btnRemoveThisEvolve.setBounds(336, 368, 147, 23);
        add(btnRemoveThisEvolve);

        JButton btnEvolve = new JButton("evolve");
        btnEvolve.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                list_2.add(choice.getSelectedItem());
                choice.remove(choice.getSelectedIndex());
            }
        });
        btnEvolve.setBounds(252, 208, 74, 23);
        add(btnEvolve);

    }
}
