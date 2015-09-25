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
    private JPanel salesmenPanel;
    private static Map <String, JPanel> panelMap; 
   
    // constructor
    public InvoiceWriter () {     
        
        this.setLayout(new CardLayout());
        this.setTitle("Invoice Writer by Robert Nowak");
        panelMap = new HashMap<>(6);
        
        mainPanel = new JPanel(new FlowLayout());
        contractorsPanel = new ContractorsPanel();
        salesmenPanel = new SalesmenPanel();
        
        panelMap.put("mainPanel", mainPanel);
        panelMap.put("contractorsPanel", contractorsPanel);
        panelMap.put("salesmenPanel", salesmenPanel);
        
        initMainPanel();
        
        this.setJMenuBar(new MenuBar_());
        this.getContentPane().add (mainPanel);   
        this.getContentPane().add (contractorsPanel); 
        this.getContentPane().add(salesmenPanel);
        pack();
        repaint();
        revalidate();
                        
        
        //this.pack();
    }
    
    public static void swapPanel (String panelName) {
        System.out.println("Swaping panel to: " + panelName);
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
        
        JButton salesmenButton = new JButton("Sprzedawcy");
        salesmenButton.addActionListener((ActionEvent ae) -> {
            swapPanel("salesmenPanel");
        });
        mainPanel.add(salesmenButton);        
    }
        
    
    public static void main(String[] args) 
    {     
        // Install WebLaF as application L&F
        WebLookAndFeel.install ();        
        SwingUtilities.invokeLater ( new Runnable ()
        {
            public void run () 
            {
                mainFrame = new InvoiceWriter();
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setLocationByPlatform(true);
                mainFrame.setResizable(false);
                mainFrame.setVisible(true);
            }
        });                       
    }    
}
