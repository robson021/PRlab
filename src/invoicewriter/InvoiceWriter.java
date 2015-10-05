/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicewriter;

import com.alee.laf.WebLookAndFeel;
import java.awt.BorderLayout;
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
    
    public static final int DEFAULT_COLUMN_SIZE = 15;
    public static final String COMMA_DELIMITER = ",";
    public static final String NEW_LINE_SEPARATOR = "\n";
    
    private static InvoiceWriter mainFrame;
    //private static JFrame loadingFrame;
    private JPanel mainPanel;
    private JPanel contractorsPanel;  
    private JPanel salesmenPanel;
    private JPanel servicesPanel;
    private static Map <String, JPanel> panelMap; 
   
    // constructor
    public InvoiceWriter () {     
        
        this.setLayout(new CardLayout());
        this.setTitle("Invoice Writer by Robert Nowak");
        panelMap = new HashMap<>(8);
        
        mainPanel = new JPanel(new BorderLayout());
        contractorsPanel = new ContractorsPanel();
        salesmenPanel = new SalesmenPanel();
        servicesPanel = new ServicesPanel();
        
        panelMap.put("mainPanel", mainPanel);
        panelMap.put("contractorsPanel", contractorsPanel);
        panelMap.put("salesmenPanel", salesmenPanel);
        panelMap.put("servicesPanel", servicesPanel);
        
        initMainPanel();
        
        this.setJMenuBar(new MenuBar_());
        this.getContentPane().add (mainPanel);   
        this.getContentPane().add (contractorsPanel); 
        this.getContentPane().add(salesmenPanel);
        this.getContentPane().add(servicesPanel);
        pack();
        repaint();
        revalidate();
    }
    
    public static void swapPanel (String panelName) {
        //System.out.println("Swapping panel to: " + panelName);
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
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton contractorsButton = new JButton("Kontrahenci");
        contractorsButton.addActionListener((ActionEvent ae) -> {
            swapPanel("contractorsPanel");          
        });
        buttonPanel.add (contractorsButton);
        
        JButton salesmenButton = new JButton("Sprzedawcy");
        salesmenButton.addActionListener((ActionEvent ae) -> {
            swapPanel("salesmenPanel");
        });
        buttonPanel.add(salesmenButton);
        
        JButton servicesButton = new JButton("Usługi / Towary");
        servicesButton.addActionListener((ActionEvent ae) -> {
            swapPanel("servicesPanel");
        });        
        buttonPanel.add(servicesButton);
        
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
    }
        
    
    public static void main(String[] args) 
    {           
//        SwingUtilities.invokeLater(() -> {
//            loadingFrame = new JFrame("  ");
//            loadingFrame.add(new JLabel("Wczytywanie programiu..."));
//            loadingFrame.pack();
//            //loadingFrame.setLocationRelativeTo(mainFrame);
//            loadingFrame.setVisible(true);
//        });
        
        System.out.println("Installing WebLaF as application L&F...");
        WebLookAndFeel.install ();   
        System.out.println("...done");
        SwingUtilities.invokeLater (new Runnable() {
            @Override
            public void run () {
                mainFrame = new InvoiceWriter();
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setLocationByPlatform(true);
                mainFrame.setResizable(false);     
                //loadingFrame.dispose();
                mainFrame.setVisible(true);
            }
        });                       
    }    
}
