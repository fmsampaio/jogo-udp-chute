/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogoudpchute;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author felipe
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label numOfPlayersLabel;
    
    @FXML
    private Label numOfTriesLabel;
    
    @FXML
    private TextField ipAddressTextField;
            
    @FXML
    private TextField portTextField;
    
    @FXML
    private Button startButton;
    
    @FXML
    private TextArea messagesTextArea;
    
    Jogo jogo;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        this.ipAddressTextField.setText("192.168.0.101");
        this.portTextField.setText("5000");
        this.numOfPlayersLabel.setText("0");
        this.numOfTriesLabel.setText("0");
        
        this.jogo = new Jogo(this);
    }    
    
}
