package com.adobeslash.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.IOUtils;

import com.adobeslash.webservice.PokeFunController;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;

public class MapView extends JPanel {
    private JTable table;
    private String mode;
    private JLabel map;

    public MapView(final AppView view) throws LoginFailedException, RemoteServerException {
        this.setBounds(0, 0, 695, 415);
        this.setLayout(null);

        map = new JLabel();
        map.setIcon(getMap(view.control));
        view.repaint();
        Timer timer = new Timer(128000, new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                System.out.println("refresh");
                map.setIcon(getMap(view.control));
                view.repaint();
            }
        });
        timer.start();
        map.setBounds(0, 0, 400, 400);
        add(map);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(429, 87, 235, 313);
        add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);
        table.setBorder(new LineBorder(Color.DARK_GRAY, 1, true));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setModel(getModelBag(view.control.getLiveStats().getMyBag()));
        mode = "bag";
        setTable();

        JButton btnBag = new JButton("bag");
        btnBag.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    table.setModel(getModelBag(view.control.getLiveStats().getMyBag()));
                    mode = "bag";
                } catch (LoginFailedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (RemoteServerException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                setTable();
            }
        });
        btnBag.setBounds(450, 10, 86, 23);
        this.add(btnBag);
        JButton btnPkmn = new JButton("Pkmn");
        btnPkmn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                try {
                    table.setModel(getModelPkmn(view.control.getLiveStats().getMyPokemon()));
                    mode = "pkmn";
                } catch (LoginFailedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (RemoteServerException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                setTable();
            }
        });
        btnPkmn.setBounds(450, 53, 86, 23);
        this.add(btnPkmn);

        JButton btnRemove = new JButton("Remove");
        btnRemove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    remove(view.control, 0);
                    if ("pkmn".equals(mode)) {
                        table.setModel(getModelPkmn(view.control.getLiveStats().getMyPokemon()));
                    } else {
                        table.setModel(getModelBag(view.control.getLiveStats().getMyBag()));
                    }
                } catch (RemoteServerException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (LoginFailedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                setTable();
            }
        });
        btnRemove.setBounds(558, 53, 89, 23);
        add(btnRemove);

        JSpinner spinner = new JSpinner();
        spinner.setBounds(558, 0, 55, 20);
        add(spinner);

    }

    public DefaultTableModel getModelPkmn(List<Pokemon> lesPkmn) {
        String[] headerPkmn = new String[] { "Nom", "CP" };
        DefaultTableModel a = new DefaultTableModel(headerPkmn, 0) {

            boolean[] columnEditables = new boolean[] { false, false };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        };
        for (Pokemon p : lesPkmn) {
            Vector<Object> data = new Vector<Object>();
            data.add(p.getPokemonId());
            data.add(p.getCp());
            a.addRow(data);
        }
        return a;

    }

    public DefaultTableModel getModelBag(Collection<Item> lesObjets) {
        String[] headerBag = new String[] { "Nom", "NB" };
        DefaultTableModel a = new DefaultTableModel(headerBag, 0) {

            boolean[] columnEditables = new boolean[] { false, false };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        };
        for (Item p : lesObjets) {
            Vector<Object> data = new Vector<Object>();
            data.add(p.getItemId());
            data.add(p.getCount());
            a.addRow(data);
        }
        return a;

    }

    public void remove(PokeFunController control, int nb) throws RemoteServerException, LoginFailedException {
        if (table.getSelectedRow() != -1)
            if ("pkmn".equals(mode)) {
                control.transferPkmn(control.getLiveStats().getMyPokemon().get(table.getSelectedRow()));
            } else {

                control.removeItem((ItemId) table.getValueAt(table.getSelectedRow(), 0), nb);
            }
    }

    public ImageIcon getMap(PokeFunController control) {
        double lat = control.position().get("lat");
        double lon = control.position().get("lon");
        String imageUrl = "http://staticmap.openstreetmap.de/staticmap.php?maptype=mapnik&zoom=15&size=400x400&markers=" + lat + "," + lon + ",red&center=" + lat + "," + lon;
        JLabel map = new JLabel("Erreur chargement de la map");
        byte[] bytes = null;
        try {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            bytes = IOUtils.toByteArray(is);
            is.close();

            return new ImageIcon((new ImageIcon(bytes)).getImage().getScaledInstance(400, 400, java.awt.Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setTable() {
        table.getColumnModel().getColumn(0).setResizable(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(121);
        table.getColumnModel().getColumn(1).setResizable(false);
        table.getColumnModel().getColumn(1).setPreferredWidth(42);
    }

}
