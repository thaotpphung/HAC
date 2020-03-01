package p2p;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class P2P {
	private double version;
	DatagramSocket socket = null;
	
	public P2P(double version) {
		this.version = version;
	}
	
	public void createAndListenSocket() 
    {
        try 
        {
        	// get my IP address
        	InetAddress myIP = InetAddress.getByName("localhost");
        	
        	
        	// send my IP to other machines
        	
        	// Create a socket for each IP and send data
        	// List of IP addresses that I'm going to send to
        	ArrayList<String> initialIPList = new ArrayList<>();
        	initialIPList.add("150.243.200.173");
        	initialIPList.add("150.243.200.142");
        	// Create 
        	socket = new DatagramSocket();
        	
        	for (int i = 0; i < initialIPList.size(); i++)
        	{
        		 byte[] incomingData = new byte[1024];
                 String sentence = myIP.toString();
                 byte[] data = sentence.getBytes();
                 DatagramPacket sendPacket = new DatagramPacket(data, data.length, destIP, 9876);
                 socket.send(sendPacket);
                 System.out.println("IP sent from " + myIP.toString());
        	}
           
    
            
            byte[] incomingData = new byte[1024];

            while (true) 
            {
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, 
                		incomingData.length);
                socket.receive(incomingPacket);
                String message = new String(incomingPacket.getData());
                InetAddress IPAddress1 = incomingPacket.getAddress();
                int port = incomingPacket.getPort();
                
                System.out.println("Received message from client: " + message);
                System.out.println("Client IP:"+IPAddress1.getHostAddress());
                System.out.println("Client port:"+port);
                
                String reply = "Thank you for the message";
                byte[] data = reply.getBytes();
                
                DatagramPacket replyPacket =
                        new DatagramPacket(data, data.length, IPAddress1, port);
                
                socket.send(replyPacket);
                Thread.sleep(2000);
                socket.close();
            }
        } 
        catch (SocketException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException i) 
        {
            i.printStackTrace();
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }
	
	public static void main(String args[]) {  
		
	}
}
