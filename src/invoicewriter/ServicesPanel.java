/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicewriter;

import People.PanelMethods;
import People.Service;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private boolean isEditButtonClicked = false;
    
    private List<Service> services;
    private JTextField nameField;
    private JTextField symbolField;
    private JTextField vatPercentageField;
    private JTextField nettoValueField;
    private JComboBox servicesBox;
    private JLabel messageLabel;
    private JButton editButton, deleteButton, addButton;
    
    public ServicesPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.fill=GridBagConstraints.BOTH;
        gbc.gridx=0; gbc.gridy=0;        
        gbc.insets.set(5, 15, 5, 15);
        
        add (new JLabel("Nazwa usuługi / torawru:  "), gbc);
        gbc.gridx++;
        nameField = new JTextField(InvoiceWriter.DEFAULT_COLUMN_SIZE);
        add(nameField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        add (new JLabel("Symbol:"), gbc);
        gbc.gridx++;
        symbolField = new JTextField(InvoiceWriter.DEFAULT_COLUMN_SIZE);
        add(symbolField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        add (new JLabel("Wartość VAT (%):"), gbc);
        gbc.gridx++;
        vatPercentageField = new JTextField(InvoiceWriter.DEFAULT_COLUMN_SIZE);
        add(vatPercentageField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        add (new JLabel("Kwota netto:"), gbc);
        gbc.gridx++;
        nettoValueField = new JTextField(InvoiceWriter.DEFAULT_COLUMN_SIZE);
        add(nettoValueField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        addButton = new JButton("Dodaj");
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
        gbc.fill=GridBagConstraints.NONE;
        add(servicesBox, gbc);
        
        loadServices();
        initServicesBox();
        
        gbc.gridx++;        
        editButton = new JButton("Edytuj");
        add (editButton, gbc);
        gbc.gridy++;
        deleteButton = new JButton("Usuń");
        add (deleteButton, gbc);
        
        editButton.addActionListener(new EditButtonHandler());
        deleteButton.addActionListener(new DeleteButtonHandler());
        
        gbc.gridx=0; gbc.gridy++;
        messageLabel = new JLabel(" ");        
        gbc.gridwidth=3;        
        add (messageLabel, gbc);
        
        gbc.gridwidth=DEFAULT_GRID_WIDTH;
        gbc.fill=GridBagConstraints.BOTH;
    }

    private void initTestCSVfile() {
        Service service = new Service("Usługa 1 test", "74401L0RE", "23", "115.20");
        Service service2 = new Service("Usługa 2 test", "dsada1L0RE", "8", "615.35");
        
        FileWriter fw = null;        
        try {
            fw = new FileWriter(new File(FILE_NAME));
            fw.append(service.toString());
            fw.append(InvoiceWriter.NEW_LINE_SEPARATOR);
            fw.append(service2.toString());
            fw.append(InvoiceWriter.NEW_LINE_SEPARATOR);
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
                String[] values = line.split(InvoiceWriter.COMMA_DELIMITER);
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
            String[] values = s.toString().split(InvoiceWriter.COMMA_DELIMITER);
            String name = values[0] + "; " + values[1]; // service name & symbol
            names.add(name);
        }        
        servicesBox.setModel(new DefaultComboBoxModel(names.toArray()));
    }

    private class EditButtonHandler implements ActionListener {
        int index;
        @Override
        public void actionPerformed(ActionEvent ae) {            
            isEditButtonClicked = !isEditButtonClicked;
            if (isEditButtonClicked) { // edit button was clicked for the 1st time
                index = servicesBox.getSelectedIndex();
                addButton.setEnabled(false);
                fillTextFields();
                editButton.setText("Zatwierdź");
                deleteButton.setText("Anuluj");
                updateMessage("Tryb edytowania", Color.black);
            } else { // button was clicked 2nd time
                changeServicesValues();
                editButton.setText("Edytuj");
                deleteButton.setText("Usuń");
                addButton.setEnabled(true);
                clearTextFields();
            }
        }

        private void fillTextFields() {
            Service s = services.get(index);
            nameField.setText(s.getName());
            symbolField.setText(s.getSymbol());
            nettoValueField.setText(s.getNetto());
            vatPercentageField.setText(s.getVatPercentage());
        }

        private void changeServicesValues() {
            String name = nameField.getText();
            String symbol = symbolField.getText();
            String vatPer = vatPercentageField.getText();
            String netto = nettoValueField.getText();    
            if (name==null || vatPer==null || netto==null) {
                updateMessage("Nieprawidłowa dane", Color.red);
                return;
            }            
            try {
            Service service = new Service(name, symbol, vatPer, netto);            
            services.set(index, service);
            updateCSVfile();
            initServicesBox();
            updateMessage("Edytowano towar / usługę", Color.green.darker().darker());
            } catch (Exception e) {
                //e.printStackTrace();
                updateMessage("Nieprawidłowe dane", Color.red);
                return;
            }
        }

    }

    private class DeleteButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (isEditButtonClicked) { // rollback editing
                clearTextFields();
                editButton.setText("Edytuj");
                deleteButton.setText("Usuń");
                addButton.setEnabled(true);
            } else { // normal delete mode
                int index = servicesBox.getSelectedIndex();
                services.remove(index);
                updateCSVfile();
                initServicesBox();
                updateMessage("Usunięto usługę / towar", Color.black);
            }
        }

    }

    private class AddServiceHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            String name = nameField.getText();
            String symbol = symbolField.getText();
            String vatPer = vatPercentageField.getText();
            String netto = nettoValueField.getText();    
            if (name==null || vatPer==null || netto==null) {
                updateMessage("Nieprawidłowa dane", Color.red);
                return;
            }            
            try {
            Service service = new Service(name, symbol, vatPer, netto);            
            services.add(service);
            updateCSVfile();
            initServicesBox();
            clearTextFields();
            updateMessage("Dodano nową usługę / towar", Color.green.darker().darker());
            } catch (Exception e) {
                //e.printStackTrace();
                updateMessage("Nieprawidłowa dane", Color.red);
                return;
            }
        }        
    }    
    
    private void updateMessage (String msg, Color c) {        
        messageLabel.setForeground(c);
        messageLabel.setText(msg);        
    }
    private void updateCSVfile() {
        FileWriter fw = null;
        try {
            fw = new FileWriter(FILE_NAME);
            for (Service s : services) {
                fw.append(s.toString());
                fw.append(InvoiceWriter.NEW_LINE_SEPARATOR);
            }
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
    private void clearTextFields() {
        nameField.setText("");
        symbolField.setText("");
        nettoValueField.setText("");
        vatPercentageField.setText("");
    }
}
