/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicewriter;

import People.Contractor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
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
    private JButton editButton;
    private JButton deleteButton;
    private JPanel thisPanel;    
    
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField companyField;
    private JTextField streetField;
    private JTextField cityField;
    private JTextField codeField;
    private JTextField nipField;
    private JTextField regonField;
    private JComboBox contractorsBox;
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
        
        loadContractors();
        
        // TODO contractors box               
        initContractorsBox();
        
        gbc.gridx=0; gbc.gridy++;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        int default_width = gbc.gridwidth;
        gbc.gridwidth=3;
        add (new JSeparator(), gbc);
        //gbc.gridx++;
        //add (new JSeparator(), gbc);
        gbc.gridx=0;
        gbc.gridwidth=default_width;
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridy++;
        add (contractorsBox, gbc);
        
        editButton = new JButton("Edytuj");
        deleteButton = new JButton("Usuń");
        
        gbc.gridx++;        
        add (editButton, gbc);
        gbc.gridy++;
        add (deleteButton, gbc);
    }   

    private void initContractorsBox() {
        ArrayList<String> names = new ArrayList<>();
        int i=0;
        for (Contractor c : contractors) {
            if (i != 0) {
                String[] s = c.toString().split(COMMA_DELIMITER);
                names.add((s[0]+" "+" "+s[1]+"; "+s[6])); // name, surname, city
            }
            i++;
        }
        contractorsBox = new JComboBox(names.toArray());
    }
    
    
    private void loadContractors () {        
        BufferedReader fileReader = null;
        contractors = new ArrayList<>();
        
        String line = "";        
        try {
            fileReader = new BufferedReader(new FileReader(FILE_NAME));
            while ((line = fileReader.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);                
                if (values.length > 0) { // add new element to list
                    contractors.add(new Contractor(values[0], values[1 ], values[2], values[3], values[4],
                            values[5], values[6], values[7]));     
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ContractorsPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ContractorsPanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileReader.close();
            } catch (IOException ex) {
                System.out.println("error while closing file reader");
                ex.printStackTrace();
            }
        }
        System.out.println("Loaded contractors from " + FILE_NAME);    
        for (Contractor c : contractors) {
            System.out.println(c.toString());
        }
    }
    
    private void initTestContractorsList() {
        Contractor columnsName = new Contractor("Name", "Surname", "CompanyName", "Street", "HomeNo", "PostCode", "City", "NIP");
        Contractor sampleContractor = new Contractor("Jan", "Kowalski", "Kowalski Spółka Z.O.O",
                "Mickiewicza", "102", "33-100", "Tarnów", "1234567890");
        Contractor sampleContractor2 = new Contractor("Adam", "Kawa", "Kawa INC.",
                "Krakowska", "156", "33-534", "Wrocław", "098343242");
        
        contractors.add(columnsName);
        contractors.add(sampleContractor);
        FileWriter fileWriter = null;        
        try {
            fileWriter = new FileWriter(FILE_NAME);
                        
            fileWriter.append(columnsName.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(sampleContractor.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(sampleContractor2.toString());
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
