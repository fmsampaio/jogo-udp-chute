package jogoudpchute;

import java.io.IOException;
import java.net.UnknownHostException;
import javafx.concurrent.Task;

public class Jogo {
    private Integer target, numOfTries, numOfPlayers, serverPort;

    private boolean acertou;

    public void setAcertou(boolean acertou) {
        this.acertou = acertou;
    }
    
    public Jogo(FXMLDocumentController controller, int port, int maxNumber) {
        this.target = (int) (Math.random() * maxNumber);
        this.serverPort = port;
        
        this.numOfTries = 0;
        this.numOfPlayers = 0;
        
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
    
    public Integer getTarget() {
        return target;
    }

    public boolean isAcertou() {
        return acertou;
    }
    public Integer getServerPort() {
        return serverPort;
    }
    
}
