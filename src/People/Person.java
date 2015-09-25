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
public abstract class Person {
    private String name, surname;
    private String companyName;
    private String streetName;
    private String homeNo;
    private String postCode;
    private String city;
    private String nipNo;

        public Person(String n, String sn, String cn ,String strName, String hn, String pc, String ct, String nip) {
            name=n; surname=sn ;companyName=cn; streetName=strName; homeNo=hn; postCode=pc; city=ct; nipNo=nip;
        } 
    public void printInfo () {
        System.out.println(name + " " + surname);
        System.out.println(companyName);
        System.out.println("ul. " + streetName + " " + homeNo + ", " + city);
        System.out.println(postCode);
        System.out.println("NIP: " + nipNo);
    }
    
    @Override
    public String toString() {
        // used for easy CSV file creation
        return name+","+surname+","+companyName+","+streetName+","+homeNo+ ","+postCode+","+city+","+nipNo;
    }
    
    public String getName() {
        return this.name;
    }
    public String getSurname() {
        return this.surname;
    }
    public String getCompanyName() {
        return this.companyName;
    }
    public String getStreet() {
        return this.streetName;
    }
    public String getHouseNumber() {
        return this.homeNo;
    }
    public String getCity() {
        return this.city;
    }
    public String getPostCode() {
        return this.postCode;
    }
    public String getNIPnumber () {
        return this.nipNo;
    }
    
    
}
