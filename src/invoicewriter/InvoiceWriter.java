/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicewriter;

import com.alee.laf.WebLookAndFeel;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
 *
 * @author Administrator
 */
public class InvoiceWriter extends JFrame {

    /**
     * @param args the command line arguments - no needed
     */
    
    private static InvoiceWriter mainFrame;
    private JPanel mainPanel;
    private JPanel contractorsPanel;    
    private static Map <String, JPanel> panelMap; 
   
    // constructor
    public InvoiceWriter () {     
        
        this.setLayout(new CardLayout());
        this.setTitle("Invoice Writer by Robert Nowak");
        panelMap = new HashMap<>(6);
        
        mainPanel = new JPanel(new FlowLayout());
        contractorsPanel = new ContractorsPanel();
        
        panelMap.put("mainPanel", mainPanel);
        panelMap.put("contractorsPanel", contractorsPanel);
        
        this.initMainPanel();
        
        this.setJMenuBar(new MenuBar_());
        this.getContentPane().add (mainPanel);    
        pack();
        repaint();
        revalidate();
        this.getContentPane().add (contractorsPanel);                 
        
        //this.pack();
    }
    
    public static void swapPanel (String panelName) {
        for (JPanel p : panelMap.values()) {
            p.setVisible(false);
        }
        JPanel p = panelMap.get(panelName);
        p.setVisible(true);        
        mainFrame.repaint();
        mainFrame.revalidate();
        mainFrame.pack();
    }
    
    public static InvoiceWriter getInstance() {
        return mainFrame;
    }    
    
    private void initMainPanel() {
        JButton contractorsButton = new JButton("Kontrahenci");
        contractorsButton.addActionListener((ActionEvent ae) -> {
            swapPanel("contractorsPanel");          
        });
        mainPanel.add (contractorsButton);
        
    }
        
    
    public static void main(String[] args) 
    {     
        // Install WebLaF as application L&F
        WebLookAndFeel.install ();
        
        SwingUtilities.invokeLater ( new Runnable ()
        {
            public void run () {
                mainFrame = new InvoiceWriter();
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setLocationByPlatform(true);
                mainFrame.setVisible(true); 
                
                // tests
                //new ContractorsPanel(new InvoiceWriter()).createAndShowGUI();
            }
        });                       
    }    
}
