package jogoudpchute;

import java.io.IOException;
import java.net.UnknownHostException;
import javafx.application.Platform;

public class Jogo {
    private Integer target, numOfTries, numOfPlayers, serverPort;
    private final FXMLDocumentController controller;
    private boolean acertou;
    
    public Jogo(FXMLDocumentController controller, int port, int maxNumber) {
        this.target = (int) (Math.random() * maxNumber);
        this.serverPort = port;
        
        this.numOfTries = 0;
        this.numOfPlayers = 0;
        
        this.controller = controller;
        this.acertou = false;
    }
    
    public Integer getNumber() {
        return target;
    }
    
    public Integer getNumOfTries() {
        return numOfTries;
    }
    
    public Integer getNumOfPlayers() {
        return numOfPlayers;
    }
    
    public void startGame() {
        System.out.println("AQUI");
        
        Thread thread = new Thread(new Runnable() {
            
            @Override
            public void run() {
                Runnable updater = new Runnable() {  
                    @Override
                    public void run() {
                        while(true) {
                            try {
                                UDPMessage guess = UDP.receiveMessage(serverPort);
                                checkGuess(guess);
                            }
                            catch(Exception e) {
                                e.printStackTrace();
                            }   
                        }
                    }
                    
                };
                // UI update is run on the Application thread
                Platform.runLater(updater);
                
            }
            
        });
        // don't let thread prevent JVM shutdown
        thread.setDaemon(true);
        thread.start();
        
        /*
        Platform.runLater(new Runnable() {
        @Override
        public void run() {
        
        }
        });*/
        
        
    }
    
    public void checkGuess(UDPMessage guess) throws UnknownHostException, IOException {
        
        String guessStr = guess.getMessage().replaceAll("[^\\d.]", "");
        System.out.println(guessStr);
        if(!guessStr.equals("")) {
            updateGUICounters(); //TODO fix it!
            int guessInt = Integer.parseInt(guessStr);
            String returnMessage = "";
            if(guessInt == this.target) {
                returnMessage = "O cliente de IP " + guess.getIpAddress() + " acertou o alvo: " + this.target;
                acertou = true;
            }
            else if(guessInt < this.target) {
                returnMessage = "O valor alvo é MAIOR que " + guessInt + "!";
            }
            else if(guessInt > this.target) {
                returnMessage = "O valor alvo é MENOR que " + guessInt + "!";
            }
            
            System.out.println(returnMessage);
            this.controller.updateMessages(returnMessage);
            UDP.sendMessage(returnMessage, guess.getIpAddress(), guess.getPort());
        }
    }
    
    public void updateGUICounters() {
        this.controller.updateGUICounters();
    }
    
}
