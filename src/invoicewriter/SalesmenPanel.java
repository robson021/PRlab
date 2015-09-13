/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicewriter;

import javax.swing.JPanel;

/**
 *
 * @author Robert
 */
public class SalesmenPanel extends JPanel {

    public SalesmenPanel() {
    }
    
    
    
    private class Salesman extends Person {
        private String phoneNo;
        private String bankName;
        private String bankAccNo;

        public Salesman(String n, String sn, String cn, String strName, String hn, String pc, String ct, String nip,
                String pn, String bn, String bAccN) {
            super(n, sn, cn, strName, hn, pc, ct, nip);
            phoneNo=pn; bankName=bn; bankAccNo=bAccN;
        }
        
        
    }
}
