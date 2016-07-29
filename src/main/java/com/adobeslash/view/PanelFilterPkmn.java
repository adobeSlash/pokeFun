package com.adobeslash.view;

import java.awt.Choice;
import java.awt.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

public class PanelFilterPkmn extends JPanel {
  public PanelFilterPkmn() {
    setLayout(null);

    JLabel lblThrowPcBelow = new JLabel("Transfer tout en dessous de ");
    lblThrowPcBelow.setBounds(10, 3, 147, 30);
    add(lblThrowPcBelow);

    JSpinner spinner = new JSpinner();
    spinner.setBounds(156, 8, 55, 20);
    add(spinner);

    JLabel lblPc = new JLabel("PC");
    lblPc.setBounds(221, 11, 26, 14);
    add(lblPc);

    Choice choice = new Choice();
    choice.setBounds(87, 81, 136, 23);
    add(choice);

    JLabel lblPkmnGarder = new JLabel("Pkmn à garder");
    lblPkmnGarder.setBounds(10, 82, 74, 19);
    add(lblPkmnGarder);

    JButton btnKeep = new JButton("keep");
    btnKeep.setBounds(248, 78, 55, 23);
    add(btnKeep);

    JLabel lblKeep = new JLabel("Keep Exception");
    lblKeep.setBounds(309, 11, 96, 14);
    add(lblKeep);

    List list = new List();
    list.setBounds(309, 31, 110, 99);
    add(list);

    List list_1 = new List();
    list_1.setBounds(309, 191, 110, 99);
    add(list_1);

    JLabel lblThrowException = new JLabel("throw Exception");
    lblThrowException.setBounds(309, 171, 96, 14);
    add(lblThrowException);

    JButton btnThrow = new JButton("throw");
    btnThrow.setBounds(242, 221, 61, 23);
    add(btnThrow);

    Choice choice_1 = new Choice();
    choice_1.setBounds(87, 224, 136, 20);
    add(choice_1);

    JLabel lblPkmnJeter = new JLabel("Pkmn à jeter");
    lblPkmnJeter.setBounds(10, 223, 61, 19);
    add(lblPkmnJeter);

    JButton btnRemoveThisThrow = new JButton("Remove this throw");
    btnRemoveThisThrow.setBounds(296, 296, 123, 23);
    add(btnRemoveThisThrow);

    JButton btnRemoveThisKeep = new JButton("Remove this keep");
    btnRemoveThisKeep.setBounds(296, 137, 123, 23);
    add(btnRemoveThisKeep);

    JLabel lblToEvolve = new JLabel("To evolve");
    lblToEvolve.setBounds(309, 330, 96, 14);
    add(lblToEvolve);

    List list_2 = new List();
    list_2.setBounds(309, 349, 110, 99);
    add(list_2);

    JButton btnRemoveThisEvolve = new JButton("Remove this evolve");
    btnRemoveThisEvolve.setBounds(296, 454, 123, 23);
    add(btnRemoveThisEvolve);

    JLabel lblPkmnEvoluer = new JLabel("Pkmn à evoluer");
    lblPkmnEvoluer.setBounds(10, 388, 74, 19);
    add(lblPkmnEvoluer);

    Choice choice_2 = new Choice();
    choice_2.setBounds(87, 388, 136, 20);
    add(choice_2);

    JButton btnEvolve = new JButton("evolve");
    btnEvolve.setBounds(229, 386, 74, 23);
    add(btnEvolve);
  }
}
