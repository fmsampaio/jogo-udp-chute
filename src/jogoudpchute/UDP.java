package jogoudpchute;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.TreeSet;

public class UDP {
    
    private static int numOfMessages = 0;
    private static TreeSet<String> numOfDifferentIPs = new TreeSet<>();
    
    public static void sendMessage(String message, String ipAddress, int port) throws SocketException, UnknownHostException, IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] dado = (message + "\n").getBytes();
        DatagramPacket datagrama = new DatagramPacket(dado, dado.length, InetAddress.getByName(ipAddress), port);
        
        socket.send(datagrama);
        
        socket.close();
    }
    
    public static UDPMessage receiveMessage(int port) throws IOException {
        DatagramSocket socket = new DatagramSocket(port);
        byte[] dado = new byte[100];
        DatagramPacket datagrama = new DatagramPacket(dado, dado.length);
        
        System.out.println("Esperando aposta... (" + port + ")");
        socket.receive(datagrama);
        
        String returnable = new String(datagrama.getData());
        socket.close();
        
        numOfMessages ++;
        numOfDifferentIPs.add(datagrama.getAddress().toString());      
        
        return new UDPMessage(returnable, datagrama.getAddress().toString().substring(1), datagrama.getPort());
    }
    
    public static int getNumOfMessages() {
        return numOfMessages;
    }
    
    public static int getNumOfDifferentIPs() {
        return numOfDifferentIPs.size();
    }
}
