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
public class Service {
    private String name;
    private String symbol;
    private int vatPercentage;
    private double nettoValue;
    private double bruttoValue;
    private double vatValue;
    
    public Service(String n, String s, String vat, String netto) {
        name=n; symbol=s; vatPercentage=Integer.parseInt(vat); nettoValue=Double.parseDouble(netto);
        vatValue = nettoValue * vatPercentage/100.;
        bruttoValue = nettoValue + vatValue;        
    }
    
    @Override
    public String toString() {
        return name+","+symbol+","+String.valueOf(vatPercentage)+","+String.valueOf(nettoValue);
    }
    public String getName() {
        return name;
    }
    public String getSymbol() {
        return symbol;
    }
    public String getVatPercentage() {
        return String.valueOf(vatPercentage);
    }
    public String getNetto() {
        return String.valueOf(nettoValue);
    }
}
