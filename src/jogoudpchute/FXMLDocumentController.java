/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private Label numOfPlayersLabel;
    
    @FXML
    private Label numOfTriesLabel;
    
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
        this.ipAddressTextField.setText("10.202.4.0");
        this.portTextField.setText("5000");
        this.numOfPlayersLabel.setText("0");
        this.numOfTriesLabel.setText("0");
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
                        
                        Task tTries = new Task<Void>() {
                            @Override public Void call() {
                                updateMessage(String.valueOf(UDP.getNumOfMessages()));
                                return null;
                            }
                        };
                        numOfTriesLabel.textProperty().bind(tTries.messageProperty());
                        new Thread(tTries).start();
                        
                        /*Task tPlayers = new Task<Void>() {
                            @Override public Void call() {
                                updateMessage(String.valueOf(UDP.getNumOfDifferentIPs()));
                                return null;
                            }
                        };
                        numOfPlayersLabel.textProperty().bind(tPlayers.messageProperty());
                        new Thread(tPlayers).start();*/
                        
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
            //updateGUICounters(); //TODO fix it!
            int guessInt = Integer.parseInt(guessStr);
      
            if(guessInt == jogo.getTarget()) {
                returnMessage = "O cliente de IP " + guess.getIpAddress() + " acertou o alvo: " + this.jogo.getTarget();
                jogo.setAcertou(true);
            }
            else if(guessInt < jogo.getTarget()) {
                returnMessage = "O valor alvo é MAIOR que " + guessInt + "!";
            }
            else if(guessInt > jogo.getTarget()) {
                returnMessage = "O valor alvo é MENOR que " + guessInt + "!";
            }
            
            System.out.println(returnMessage);
            UDP.sendMessage(returnMessage, guess.getIpAddress(), guess.getPort());
        }
        return returnMessage;
    }

    public void updateGUICounters() {      
        this.numOfPlayersLabel.setText(String.valueOf(UDP.getNumOfDifferentIPs()));
        this.numOfTriesLabel.setText(String.valueOf(UDP.getNumOfMessages()));
    }

    public synchronized void updateMessages(String message) {
        String currentState = this.messagesTextArea.getText();
        this.messagesTextArea.setText(currentState + message + "\n");
    }
    
}
