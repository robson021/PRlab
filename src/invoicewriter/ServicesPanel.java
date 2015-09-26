/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicewriter;

import People.Service;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

/**
 *
 * @author Robert
 */
public class ServicesPanel extends JPanel {
    private List<Service> services;
    private JTextField nameField;
    private JTextField symbolField;
    private JTextField vatPercentageField;
    private JTextField nettoValueField;
    
    public ServicesPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.fill=GridBagConstraints.BOTH;
        gbc.gridx=0; gbc.gridy=0;        
        gbc.insets.set(5, 15, 5, 15);
        
        add (new JLabel("Nazwa usuługi/torawru:  "), gbc);
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
    }

    private static class AddServiceHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
}
