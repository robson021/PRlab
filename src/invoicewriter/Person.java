/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicewriter;

/**
 *
 * @author Robert
 */
public abstract class Person {
    protected String name, surname;
    protected String companyName;
    protected String streetName;
    protected int homeNo;
    protected String postCode;
    protected String city;
    protected int nipNo;

        public Person(String n, String sn, String cn ,String strName, int hn, String pc, String ct, int nip) {
            name=n; surname=sn ;companyName=cn; streetName=strName; homeNo=hn; postCode=pc; city=ct; nipNo=nip;
        } 
    public void printInfo () {
        System.out.println(name + " " + surname);
        System.out.println(companyName);
        System.out.println("ul. "  + streetName + " " + homeNo + ", " + city);
        System.out.println(postCode);
        System.out.println("NIP: " + nipNo);
    }
}
