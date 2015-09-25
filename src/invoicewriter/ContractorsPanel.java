/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicewriter;

import People.Contractor;
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
import javax.swing.SwingUtilities;

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
    //private JTextField regonField;
    private JComboBox contractorsBox;
    private List<Contractor> contractors;       
    
    //private int index;
    
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
        
//        gbc.gridx=0; gbc.gridy++;
//        regonField = new JTextField(DEFAULT_COLUMN_SIZE);
//        add (new JLabel("Regon:"), gbc);
//        gbc.gridx++;
//        add (regonField, gbc);
        
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
        if(!f.exists() || f.isDirectory() )
            initTestContractorsList();
                
        //index = contractors.size();
        
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
        contractorsBox = new JComboBox();
        contractorsBox.setPreferredSize(new Dimension(150, 30));
        add (contractorsBox, gbc);
        
        loadContractors();                   
        initContractorsBox();
        
        editButton = new JButton("Edytuj");
        deleteButton = new JButton("Usuń");        
        editButton.addActionListener(new EditButtonHandler());
        deleteButton.addActionListener(new DeleteButtonHandler());
        
        gbc.gridx++;        
        add (editButton, gbc);
        gbc.gridy++;
        add (deleteButton, gbc);
        
        addButton.addActionListener(new AddContractorHandler());
        
        gbc.gridx=0; gbc.gridy++;
        messageLabel = new JLabel(" ");        
        gbc.gridwidth=3;
        add (messageLabel, gbc);
        gbc.gridwidth=DEFAULT_WIDTH;
    }   

    private void initContractorsBox() {
        List<String> names = new ArrayList<>();
        //int i=0;
        for (Contractor c : contractors) {            
                String[] s = c.toString().split(COMMA_DELIMITER);
                names.add((s[0]+" "+" "+s[1]+"; "+s[6])); // name, surname, city           
        }     
        contractorsBox.setModel(new DefaultComboBoxModel(names.toArray()));
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
                    contractors.add(new Contractor(values[0], values[1], values[2], values[3], values[4],
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
       // Contractor columnsName = new Contractor("Name", "Surname", "CompanyName", "Street", "HomeNo", "PostCode", "City", "NIP");
        Contractor sampleContractor = new Contractor("Jan", "Kowalski", "Kowalski Spółka Z.O.O",
                "Mickiewicza", "102", "33-100", "Tarnów", "1234567890");
        Contractor sampleContractor2 = new Contractor("Adam", "Kawa", "Kawa INC.",
                "Krakowska", "156", "33-534", "Wrocław", "098343242");
        
        //contractors.add(columnsName);
        contractors.add(sampleContractor);
        FileWriter fileWriter = null;        
        try {
            fileWriter = new FileWriter(FILE_NAME);
                        
            //fileWriter.append(columnsName.toString());
            //fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(sampleContractor.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(sampleContractor2.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);
            
            System.out.println("Sample contractors CSV file was created.");
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
    
    private class AddContractorHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {                        
            String name = nameField.getText();
            String surname = surnameField.getText();
            String company = companyField.getText();
            String[] streetAndHouseNO = streetField.getText().split(" ");            
            String city = cityField.getText();
            String postCode = codeField.getText();
            String NIP = nipField.getText();
            //String regon = regonField.getText();
            
            if (!streetField.getText().contains(" ") || name==null ||
                    surname==null || company==null || streetAndHouseNO[0]==null || streetAndHouseNO[1]==null ||
                    city==null || postCode==null || NIP==null) {
                updateMessage("Nieprawidłowe wartości", Color.red);
                return;
            }            
            //ArrayList people = (ArrayList) contractors;            
            Contractor contractor = new Contractor(name, surname, city, streetAndHouseNO[0],
                    streetAndHouseNO[1], postCode, city, NIP);
            System.out.println("Adding:\n" + contractor.toString());
            //people.add(index, contractor);
            //index = people.size();
            contractors.add(contractor);
            updateCSVfile();
            initContractorsBox();
            clearTextFields();           
            SwingUtilities.invokeLater(() -> {
                updateMessage("Zaktualizowano kontrahentów", Color.green.darker().darker());
            });            
        }
        
    }
    
    private void updateCSVfile () {
        FileWriter fileWriter = null;           
        try {
            fileWriter = new FileWriter(FILE_NAME);
            for (Contractor c : contractors) {
                fileWriter.append(c.toString());
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
    
    private void clearTextFields() {
        nameField.setText("");
        surnameField.setText("");
        companyField.setText("");
        streetField.setText("");
        cityField.setText("");
        codeField.setText("");
        nipField.setText("");
        //regonField.setText("");
    }
    
    private void updateMessage (String msg, Color c) {        
        messageLabel.setForeground(c);
        messageLabel.setText(msg);        
    }
    
    private class EditButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            isEditButtonClicked = !isEditButtonClicked;            
            if (isEditButtonClicked) { // edit button has been just clicked.
                editButton.setText("Zatwierdź"); // change text to "commit changes"
                deleteButton.setText("Anuluj");
                fillTextFields();      
                updateMessage("Tryb edytowania", Color.black);                
            } else { // button was clicked 2nd time
                editButton.setText("Edytuj");
                deleteButton.setText("Usuń");
                changeContractorValues(); 
            }
        }

        private void fillTextFields() {     
            int index = contractorsBox.getSelectedIndex();
            Contractor c = contractors.get(index);
            nameField.setText(c.getName());
            surnameField.setText(c.getSurname());
            cityField.setText(c.getCity());
            nipField.setText(c.getNIPnumber());
            streetField.setText(c.getStreet()+" "+c.getHouseNumber()); 
            codeField.setText(c.getPostCode());
            companyField.setText(c.getCompanyName());
        }

        private void changeContractorValues() { 
            String name = nameField.getText();
            String surname = surnameField.getText();
            String company = companyField.getText();
            String[] streetAndHouseNO = streetField.getText().split(" ");            
            String city = cityField.getText();
            String postCode = codeField.getText();
            String NIP = nipField.getText();
            
            if (!streetField.getText().contains(" ") || name==null ||
                    surname==null || company==null || streetAndHouseNO[0]==null || streetAndHouseNO[1]==null ||
                    city==null || postCode==null || NIP==null) {
                updateMessage("Nieprawidłowe wartości", Color.red);
                return;
            }         
                     
            Contractor contractor = new Contractor(name, surname, city, streetAndHouseNO[0],
                    streetAndHouseNO[1], postCode, city, NIP);
            //System.out.println("Adding:\n" + contractor.toString());
            int index = contractorsBox.getSelectedIndex();
            contractors.set(index, contractor);
            updateCSVfile();
            initContractorsBox();
            clearTextFields();   
            updateMessage("Zmieniono dane kontrahenta", Color.green.darker().darker());
        }
        
    }
    
    private class DeleteButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) { // TODO
            if (!isEditButtonClicked) { // normal delete mode
                deleteContractor();
                updateCSVfile();
                initContractorsBox();
                updateMessage("Usunięto kontrahenta", Color.black);
            } else { // rollback edit changes
                clearTextFields();
                isEditButtonClicked = !isEditButtonClicked;
                editButton.setText("Edytuj");
                deleteButton.setText("Usuń");
                updateMessage("Anulowano", Color.black);
            }         
        }

        private void deleteContractor() { 
            int index = contractorsBox.getSelectedIndex();
            contractors.remove(index);            
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
