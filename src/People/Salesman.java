/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package People;

/**
 *
 * @author Robert
 */
public class Salesman extends Person{

    private String regon;
    private String phoneNo;
    private String bankName;
    private String bankAccNo;

    public Salesman(String n, String sn, String cn, String strName, String hn, String pc, String ct, String nip,
                String reg, String pn, String bn, String bAccN) {
        super(n, sn, cn, strName, hn, pc, ct, nip);
        regon=reg; phoneNo=pn; bankName=bn; bankAccNo=bAccN;
        }    
    
    @Override
    public String toString() {
        return super.toString()+","+regon+","+phoneNo+","+bankName+","+bankAccNo;
    }
}
