package jogoudpchute;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
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
    private TextField ipAddressTextField;
            
    @FXML
    private TextField portTextField;
    
    @FXML
    private TextField maxNumberTextField;
    
    @FXML
    private Button startButton;
    
    @FXML
    private TextArea messagesTextArea;
    
    private Jogo jogo;
    private String messages;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        this.ipAddressTextField.setText("10.202.3.204");
        this.portTextField.setText("12345");
        this.maxNumberTextField.setText("500");
        this.messages = "";
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        int port = Integer.parseInt(this.portTextField.getText());
        int maxNumber = Integer.parseInt(this.maxNumberTextField.getText());
        this.jogo = new Jogo(this, port, maxNumber);
        
        Task task = new Task<Void>() {
            @Override public Void call() {
                while(!jogo.isAcertou()) {
                    try {
                        UDPMessage guess = UDP.receiveMessage(jogo.getServerPort());
                        messages += checkGuess(guess) + "\n";
                        updateMessage(messages);
                                              
                        Thread.sleep(100);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
        
        messagesTextArea.textProperty().bind(task.messageProperty());
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        
    }
    
    public synchronized String checkGuess(UDPMessage guess) throws UnknownHostException, IOException {
        
        String guessStr = guess.getMessage().replaceAll("[^\\d.]", "");
        System.out.println(guessStr);
        String returnMessage = "";
        
        if(!guessStr.equals("")) {
            int guessInt = Integer.parseInt(guessStr);
      
            if(guessInt == jogo.getTarget()) {
                returnMessage = "O cliente de IP " + guess.getIpAddress() + " acertou o alvo: " + this.jogo.getTarget();
                jogo.setAcertou(true);
            }
            else if(guessInt < jogo.getTarget()) {
                returnMessage = guess.getIpAddress() + ": O valor alvo eh MAIOR que " + guessInt + "!";
            }
            else if(guessInt > jogo.getTarget()) {
                returnMessage = guess.getIpAddress() + ": O valor alvo eh MENOR que " + guessInt + "!";
            }
            
            System.out.println(returnMessage);
            UDP.sendMessage("O valor alvo eh MAIOR que " + guessInt + "!", guess.getIpAddress(), guess.getPort());
        }
        return returnMessage;
    }

    public synchronized void updateMessages(String message) {
        String currentState = this.messagesTextArea.getText();
        this.messagesTextArea.setText(currentState + message + "\n");
    }
    
}
