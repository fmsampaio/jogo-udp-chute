package jogoudpchute;

public class UDPMessage {
          
    private String message;
    private String ipAddress;
    private int port;
    
    public UDPMessage(String message, String ipAddress, int port) {
        this.message = message;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getMessage() {
        return message;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
    
}
