package com.adobeslash.view;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AppView extends JFrame {

  public AppView() {
    setTitle("PokeFunView");
    getContentPane().setLayout(null);

    JPanel panel = new PanelLogin();
    panel.setBounds(0, 0, 690, 465);
    getContentPane().add(panel);
    panel.setLayout(null);
  }

}
