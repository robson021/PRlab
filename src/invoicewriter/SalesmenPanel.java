/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicewriter;

import People.Salesman;
import java.awt.Color;
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
public class SalesmenPanel extends JPanel {
    public static final int DEFAULT_COLUMN_SIZE = 15;
    public static final String COMMA_DELIMITER = ",";
    //static final String FILE_HEADER = "id,firstName,lastName,gender,age";
    public static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_NAME = "salesmenList.csv";
    
    private boolean isEditButtonClicked = false;

    private JButton addButton;
    private JButton backButton;
    private JButton editButton;
    private JButton deleteButton;
    private JPanel thisPanel;
    private JLabel messageLabel;
    
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField companyField;
    private JTextField streetField;
    private JTextField cityField;
    private JTextField codeField;
    private JTextField nipField;
    private JTextField regonField;
    private JTextField bankAccNoField;
    private JTextField bankNameField;
    private JTextField phoneNoField;
    //private JTextField regonField;
    private JComboBox salesmenBox;
    private List<Salesman> salesmen;

    public SalesmenPanel() {
        thisPanel = this;
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();        
        gbc.fill=GridBagConstraints.BOTH;
        gbc.gridx=0; gbc.gridy=0;        
        gbc.insets.set(5, 15, 5, 15);
        
        // adding text fields with labels
        add (new JLabel("Imię:"), gbc);        
        nameField = new JTextField(DEFAULT_COLUMN_SIZE);        
        gbc.gridx++;        
        add (nameField, gbc);
        
        surnameField = new JTextField(DEFAULT_COLUMN_SIZE);
        gbc.gridx=0; gbc.gridy++;
        add (new JLabel("Nazwisko:"), gbc);
        gbc.gridx++;
        add (surnameField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        companyField = new JTextField(DEFAULT_COLUMN_SIZE);
        add (new JLabel("Nazwa firmy:"), gbc);
        gbc.gridx++;
        add (companyField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        streetField = new JTextField(DEFAULT_COLUMN_SIZE);
        add (new JLabel("Ulica, numer domu:"), gbc);
        gbc.gridx++;
        add (streetField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        cityField = new JTextField(DEFAULT_COLUMN_SIZE);
        add (new JLabel("Miejscowość:"), gbc);
        gbc.gridx++;
        add (cityField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        codeField = new JTextField(DEFAULT_COLUMN_SIZE);
        add (new JLabel("Kod pocztowy:"), gbc);
        gbc.gridx++;
        add (codeField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        nipField = new JTextField(DEFAULT_COLUMN_SIZE);
        add (new JLabel("NIP:"), gbc);
        gbc.gridx++;
        add (nipField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        regonField = new JTextField(DEFAULT_COLUMN_SIZE);
        add (new JLabel("Regon:"), gbc);
        gbc.gridx++;
        add (regonField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        phoneNoField = new JTextField(DEFAULT_COLUMN_SIZE);
        add (new JLabel("Nr telefonu:"), gbc);
        gbc.gridx++;
        add (phoneNoField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        bankNameField = new JTextField(DEFAULT_COLUMN_SIZE);
        add (new JLabel("Nazwa baku:"), gbc);
        gbc.gridx++;
        add (bankNameField, gbc);
        
        gbc.gridx=0; gbc.gridy++;
        bankAccNoField = new JTextField(DEFAULT_COLUMN_SIZE);
        add (new JLabel("Numer konta bankowego:"), gbc);
        gbc.gridx++;
        add (bankAccNoField, gbc);
        
        // buttons
        gbc.gridx=0; gbc.gridy++;
        //gbc.insets.set(5, 5, 5, 5);
        addButton = new JButton("Dodaj");
        backButton = new JButton("Powrót");
        add (addButton, gbc);
        gbc.gridx++;
        add (backButton, gbc);
        
        backButton.addActionListener((ActionEvent e) -> {
            InvoiceWriter.swapPanel("mainPanel");
            
        });        
        salesmen = new ArrayList<>();
        File f = new File(FILE_NAME);
        if(!f.exists() || f.isDirectory())
            initTestSalesmenList();
        
        gbc.gridx=0; gbc.gridy++;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        final int DEFAULT_WIDTH = gbc.gridwidth;
        gbc.gridwidth=3;
        add (new JSeparator(), gbc);
        //gbc.gridx++;
        //add (new JSeparator(), gbc);
        gbc.gridx=0;
        gbc.gridwidth=DEFAULT_WIDTH;
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridy++;
        salesmenBox = new JComboBox();
        salesmenBox.setPreferredSize(new Dimension(150, 30));
        add (salesmenBox, gbc);
        
        loadSalesmen();                   
        initSalesmenBox();
        
        editButton = new JButton("Edytuj");
        deleteButton = new JButton("Usuń");        
        editButton.addActionListener(new EditButtonHandler());
        deleteButton.addActionListener(new DeleteButtonHandler());
        
        gbc.gridx++;        
        add (editButton, gbc);
        gbc.gridy++;
        add (deleteButton, gbc);
        
        addButton.addActionListener(new AddSalesmanHandler());
        
        gbc.gridx=0; gbc.gridy++;
        messageLabel = new JLabel(" ");        
        gbc.gridwidth=3;
        add (messageLabel, gbc);
        gbc.gridwidth=DEFAULT_WIDTH;
    }   

    private void initTestSalesmenList() {
        Salesman salesman = new Salesman("Jacek", "Kaganek", "Kraków", "Mickiewicza", "23",
                FILE_NAME, FILE_NAME, FILE_NAME, FILE_NAME, FILE_NAME, FILE_NAME, FILE_NAME);
        Salesman salesman2 = new Salesman("Marcin", "Iwański", "Poznań", "Słowackiego", "56",
                FILE_NAME, FILE_NAME, FILE_NAME, FILE_NAME, FILE_NAME, FILE_NAME, FILE_NAME);
        
        FileWriter fileWriter = null;        
        try {
            fileWriter = new FileWriter(FILE_NAME);
            
            fileWriter.append(salesman.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(salesman2.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);
            
            System.out.println("Sample salesmen CSV file was created.");
        } catch (IOException ex) {
            Logger.getLogger(ContractorsPanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException ex) {
                System.out.println("Error while closing File Writer.");
                ex.printStackTrace();
            }
        }
        
    }

    // TODO
    private void loadSalesmen() {
        BufferedReader fileReader = null;
        salesmen = new ArrayList<>();
        String line = "";        
        try {
            fileReader = new BufferedReader(new FileReader(FILE_NAME));
            while ((line = fileReader.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);                
                if (values.length > 0) { // add new element to list
                    salesmen.add(new Salesman(values[0], values[1], values[2], values[3], values[4],
                            values[5], values[6], values[7], values[8], values[9], values[10], values[11]));     
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SalesmenPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SalesmenPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void initSalesmenBox() {
        List<String> names = new ArrayList<>();
        for (Salesman salesman : salesmen) {
            String[] s = salesman.toString().split(COMMA_DELIMITER);
                names.add((s[0]+" "+" "+s[1]+"; "+s[6])); // name, surname, city  
        }
        salesmenBox.setModel(new DefaultComboBoxModel(names.toArray()));
    }

    private class EditButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            
        }
    }

    private class DeleteButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
        }

    }

    private class AddSalesmanHandler implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent ae) {                        
            String name = nameField.getText();
            String surname = surnameField.getText();
            String company = companyField.getText();
            String[] streetAndHouseNO = streetField.getText().split(", ");            
            String city = cityField.getText();
            String postCode = codeField.getText();
            String NIP = nipField.getText();
            String regon = regonField.getText();
            String phoneNo = phoneNoField.getText();
            String bankAccNo = bankAccNoField.getText();
            String bankName = bankNameField.getText();
            
            if (!streetField.getText().contains(", ") || name==null ||
                    surname==null || company==null || streetAndHouseNO[0]==null || streetAndHouseNO[1]==null ||
                    city==null || postCode==null || NIP==null || regon==null ||
                    phoneNo==null || bankName==null || bankAccNo==null) {
                updateMessage("Nieprawidłowe wartości", Color.red);
                return;
            }            
            
            Salesman salesman = new Salesman(name, surname, company, streetAndHouseNO[0], streetAndHouseNO[1],
                    postCode, city, NIP, regon, phoneNo, bankName, bankAccNo);
            
            System.out.println("Adding:\n" + salesman.toString());            
            salesmen.add(salesman);
            updateCSVfile();
            initSalesmenBox();
            clearTextFields();    
            updateMessage("Dodano nowego sprzedawce", Color.green.darker().darker());                      
        }        
    }
    
    private void updateCSVfile () {
        FileWriter fileWriter = null;           
        try {
            fileWriter = new FileWriter(FILE_NAME);
            for (Salesman s : salesmen) {
                fileWriter.append(s.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);
            } 
        } catch (IOException ex) {
            Logger.getLogger(ContractorsPanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {fileWriter.flush();
                fileWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(ContractorsPanel.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
    }
    
    private void updateMessage (String msg, Color c) {        
        messageLabel.setForeground(c);
        messageLabel.setText(msg);        
    }
    
    private void clearTextFields() {
        nameField.setText("");
        surnameField.setText("");
        companyField.setText("");
        streetField.setText("");
        cityField.setText("");
        codeField.setText("");
        nipField.setText("");
        regonField.setText("");
        bankAccNoField.setText("");
        bankNameField.setText("");
        phoneNoField.setText("");
    }
    
    
    
}
