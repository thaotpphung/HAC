package p2p;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class P2P {
//	private double version;
	DatagramSocket socket = null;
	
	public P2P() {
//		this.version = version;
	}
	
	public void createAndListenSocket() 
    {
        try 
        {
        	// get my IP address
        	InetAddress myIP = InetAddress.getByName("localhost");
        	
        	// Create a socket for each IP and send data
        	// List of IP addresses that I'm going to send to
        	ArrayList<String> initialIPList = new ArrayList<>();
        	initialIPList.add("150.243.200.173");
        	initialIPList.add("150.243.200.142");
        	// Create socket
        	socket = new DatagramSocket(9876);
        	// Send IP address to other machines
        	for (int i = 0; i < initialIPList.size(); i++)
        	{
        		InetAddress destIP = InetAddress.getByName(initialIPList.get(i));;
        		String sentence = myIP.toString();
	            byte[] data = sentence.getBytes();
	            DatagramPacket sendPacket = new DatagramPacket(data, data.length, destIP, 9876);
	            socket.send(sendPacket);
	            System.out.println("IP sent from " + myIP.toString());
        	}
           
        	// wait to receive IP addresses from other machines
        	
        	
            byte[] incomingData = new byte[1024];
            
            while (true) 
            {
            	// receive data from an IP
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, 
                		incomingData.length);
                socket.receive(incomingPacket);
                String message = new String(incomingPacket.getData());
                InetAddress otherIP = incomingPacket.getAddress();
                int port = incomingPacket.getPort();
                
                System.out.println("Received IP from peer: " + message);
                System.out.println("Other peer IP: "+ otherIP.getHostAddress());
                System.out.println("Other peer port:"+ port);
                
                String reply = "Thank you for the message";
                byte[] data = reply.getBytes();
                
                DatagramPacket replyPacket =
                        new DatagramPacket(data, data.length, otherIP, port);
//                socket.close();
//                socket.send(replyPacket);
                Thread.sleep(2000);
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
		P2P peer = new P2P();
        peer.createAndListenSocket();
	}
}
