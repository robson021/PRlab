/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicewriter;

import com.alee.laf.WebLookAndFeel;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;

/**
 *
 * @author Administrator
 */
public class MenuBar_ extends JMenuBar {
  
  private JMenu fileMenu;  
  private JMenu optionMenu;
  private JMenu viewMenu;  
    
    public MenuBar_() {
        
        fileMenu = new JMenu("Plik");
        JMenuItem newItem = new JMenuItem("Nowy");
        JMenuItem openItem = new JMenuItem("Otwórz");
        
        newItem.addActionListener((ActionEvent e) -> {
            System.out.println("nowy projekt");
        });
        
        newItem.addActionListener((ActionEvent ae) -> {
            // TODO
            
        });
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        add (fileMenu);   
        
        optionMenu = new JMenu("Opcje");
        
        add (optionMenu);
        
        viewMenu = new JMenu("Widok");
        ButtonGroup bgrp = new ButtonGroup();        
        JRadioButtonMenuItem webLaf = new JRadioButtonMenuItem("Web style", true);
        viewMenu.add(webLaf);
        JRadioButtonMenuItem classicLaf = new JRadioButtonMenuItem("Classic style");
        viewMenu.add(classicLaf);
        bgrp.add(webLaf);
        bgrp.add(classicLaf);        
        
        webLaf.addActionListener((ActionEvent e) -> {    // TODO 
            if (!webLaf.isSelected()) {
                WebLookAndFeel.install();
                repaint();
            }
        });
        
        classicLaf.addActionListener((ActionEvent e) -> {
            if (!classicLaf.isSelected()) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    repaint();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        
        add(viewMenu);
    }
    
}
