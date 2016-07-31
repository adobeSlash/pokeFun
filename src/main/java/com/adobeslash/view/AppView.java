package com.adobeslash.view;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.adobeslash.webservice.PokeFunController;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

public class AppView extends JFrame {
    PokeFunController control;

    public AppView() throws LoginFailedException, RemoteServerException {
        control = new PokeFunController();
    setTitle("PokeFunView");
    getContentPane().setLayout(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

    public void changePanel(JPanel panel) {
        this.setBounds(0, 0, 690, 465);
        this.getContentPane().removeAll();
        panel.setBounds(0, 0, 690, 465);
        this.getContentPane().add(panel);
        panel.setLayout(null);
        this.repaint();
    }

}
