/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicewriter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Administrator
 */
public class ContractorsPanel extends JPanel {
    public static final int DEFAULT_COLUMN_SIZE = 15;
    public static final String COMMA_DELIMITER = ",";
    //static final String FILE_HEADER = "id,firstName,lastName,gender,age";
    public static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_NAME = "contractorsList.csv";

    private JButton addButton;
    private JButton backButton;
    private JPanel thisPanel;    
    
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField companyField;
    private JTextField streetField;
    private JTextField cityField;
    private JTextField codeField;
    private JTextField nipField;
    private JTextField regonField;
    private JList contractorsList;
    private List<Contractor> contractors;   
    
    
    // constructor
    public ContractorsPanel() {
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
        add (new JLabel("Ulica i numer domu:"), gbc);
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
        contractors = new ArrayList<>();
        File f = new File(FILE_NAME);
        if(!f.exists() || f.isDirectory())
            initTestContractorsList();
        
        
        // TODO contractors jlist               
        
    }   
    
    private class Contractor extends Person{
        public Contractor(String n, String sn, String cn, String strName, String hn, String pc, String ct, String nip) {
            super(n, sn, cn, strName, hn, pc, ct, nip);
        }        
    }
    
    private void loadContractors () {        
        // TODO load form csv files
        
    }
    
    private void initTestContractorsList() {
        Contractor columnsName = new Contractor("Name", "Surname", "CompanyName", "Street", "HomeNo", "PostCode", "City", "NIP");
        Contractor sampleContractor = new Contractor("Jan", "Kowalski", "Kowalski Spółka Z.O.O",
                "Mickiewicza", "102", "33-100", "Tarnów", "1234567890");
        
        contractors.add(columnsName);
        contractors.add(sampleContractor);
        FileWriter fileWriter = null;        
        try {
            fileWriter = new FileWriter(FILE_NAME);
                        
            fileWriter.append(columnsName.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(sampleContractor.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);
            
            System.out.println("Sample CSV file was created.");
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
    
    
    
    
    // test gui show
/*    public void createAndShowGUI () {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame testFrame = new JFrame("test frame - contractors panel");
                testFrame.getContentPane().add(thisPanel);
                testFrame.setLocationByPlatform(true);
                testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                testFrame.pack();
                testFrame.setVisible(true);
            }
        });
    } */
}
