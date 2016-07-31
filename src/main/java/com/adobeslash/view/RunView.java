package com.adobeslash.view;

import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

public class RunView {

    public static void main(String[] args) throws LoginFailedException, RemoteServerException {
    // TODO Auto-generated method stub


    AppView view = new AppView();
    view.setBounds(0, 0, 690, 465);
        view.changePanel(new PanelConfigLogin(view));
    view.setVisible(true);

  }

}
