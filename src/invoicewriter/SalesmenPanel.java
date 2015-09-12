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
        private int phoneNo;
        private String bankName;
        private int bankAccNo;

        public Salesman(String n, String sn, String cn, String strName, int hn, String pc, String ct, int nip,
                int pn, String bn, int bAccN) {
            super(n, sn, cn, strName, hn, pc, ct, nip);
            phoneNo=pn; bankName=bn; bankAccNo=bAccN;
        }
        
        
    }
}
