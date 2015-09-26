/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicewriter;

import People.Service;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

/**
 *
 * @author Robert
 */
public class ServicesPanel extends JPanel {
    private static final String FILE_NAME = "servicesList.csv";
    
    private List<Service> services;
    private JTextField nameField;
    private JTextField symbolField;
    private JTextField vatPercentageField;
    private JTextField nettoValueField;
    private JComboBox servicesBox;
    
    public ServicesPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.fill=GridBagConstraints.BOTH;
        gbc.gridx=0; gbc.gridy=0;        
        gbc.insets.set(5, 15, 5, 15);
        
        add (new JLabel("Nazwa usuługi / torawru:  "), gbc);
        gbc.gridx++;
        nameField = new JTextField(ContractorsPanel.DEFAULT_COLUMN_SIZE);
        add(nameField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        add (new JLabel("Symbol:"), gbc);
        gbc.gridx++;
        symbolField = new JTextField(ContractorsPanel.DEFAULT_COLUMN_SIZE);
        add(symbolField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        add (new JLabel("Wartość VAT (%):"), gbc);
        gbc.gridx++;
        vatPercentageField = new JTextField(ContractorsPanel.DEFAULT_COLUMN_SIZE);
        add(vatPercentageField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        add (new JLabel("Kwota netto:"), gbc);
        gbc.gridx++;
        nettoValueField = new JTextField(ContractorsPanel.DEFAULT_COLUMN_SIZE);
        add(nettoValueField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        JButton addButton = new JButton("Dodaj");
        addButton.addActionListener(new AddServiceHandler());
        add (addButton, gbc);
        JButton backBatton = new JButton("Powrót");
        gbc.gridx++;
        add (backBatton, gbc);
        backBatton.addActionListener((ActionEvent ae) -> {
            InvoiceWriter.swapPanel("mainPanel");
        });
        
        gbc.gridx=0; gbc.gridy++;
        final int DEFAULT_GRID_WIDTH = gbc.gridwidth;
        gbc.gridwidth=3;
        add (new JSeparator(), gbc);
        gbc.gridwidth=DEFAULT_GRID_WIDTH;
        
        services = new ArrayList<>();
        File f = new File(FILE_NAME);
        if (!f.exists() || f.isDirectory())
            initTestCSVfile();
        
        servicesBox = new JComboBox();
        servicesBox.setPreferredSize(new Dimension(150, 30));
        
        gbc.gridx=0; gbc.gridy++;
        add(servicesBox, gbc);
        
        loadServices();
        initServicesBox();
    }

    private void initTestCSVfile() {
        Service service = new Service("Usługa 1 test", "74401L0RE", "23", "115.20");
        Service service2 = new Service("Usługa 2 test", "dsada1L0RE", "8", "615.35");
        
        FileWriter fw = null;        
        try {
            fw = new FileWriter(new File(FILE_NAME));
            fw.append(service.toString());
            fw.append(SalesmenPanel.NEW_LINE_SEPARATOR);
            fw.append(service2.toString());
            fw.append(SalesmenPanel.NEW_LINE_SEPARATOR);
        } catch (IOException ex) {
            Logger.getLogger(ServicesPanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(ServicesPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void loadServices() {
        BufferedReader fr = null;
        try {
            fr = new BufferedReader(new FileReader(FILE_NAME));
            String line ="";
            while ((line = fr.readLine()) != null) {
                String[] values = line.split(ContractorsPanel.COMMA_DELIMITER);
                if (values.length > 0) {
                    services.add(new Service(values[0], values[1], values[2], values[3]));
                }
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServicesPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServicesPanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(ServicesPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void initServicesBox() {
        List<String> names = new ArrayList<>();
        for (Service s : services) {
            String[] values = s.toString().split(ContractorsPanel.COMMA_DELIMITER);
            String name = values[0] + "; " + values[1]; // service name & symbol
            names.add(name);
        }        
        servicesBox.setModel(new DefaultComboBoxModel(names.toArray()));
    }

    private class AddServiceHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            
        }
        
    }
}
