/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package People;

import java.awt.Color;

/**
 *
 * @author Robert
 */
public interface PanelMethods {  
    void updateCSVfile();
    void initTestCSVfile();
    void loadDataFromFile();
    void initComboBox();    
    void updateMessage (String msg, Color c);
    void clearTextFields();
}
