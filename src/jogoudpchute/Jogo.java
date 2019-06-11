package jogoudpchute;

public class Jogo {
    private Integer number, numOfTries, numOfPlayers;
    private final FXMLDocumentController controller;
    
    public Jogo(FXMLDocumentController controller) {
        this.number = (int) (Math.random() * 100000);
        this.numOfTries = 0;
        this.numOfPlayers = 0;
        this.controller = controller;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getNumOfTries() {
        return numOfTries;
    }

    public Integer getNumOfPlayers() {
        return numOfPlayers;
    }
    
}
