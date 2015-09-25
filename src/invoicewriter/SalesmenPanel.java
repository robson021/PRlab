/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicewriter;

import People.Contractor;
import People.Salesman;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    //private JTextField regonField;
    private JComboBox salesmenBox;
    private List<Contractor> salesmen;

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
                FILE_NAME, FILE_NAME, FILE_NAME, FILE_NAME, FILE_NAME, FILE_NAME);
        Salesman salesman2 = new Salesman("Marcin", "Iwański", "Poznań", "Słowackiego", "56",
                FILE_NAME, FILE_NAME, FILE_NAME, FILE_NAME, FILE_NAME, FILE_NAME);
        
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

    private void loadSalesmen() {
    }

    private void initSalesmenBox() {
    }

    private static class EditButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
        }
    }

    private static class DeleteButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
        }

    }

    private static class AddSalesmanHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
        }

    }
    
    
    
}
