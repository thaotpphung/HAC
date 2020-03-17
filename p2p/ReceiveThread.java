package p2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ReceiveThread implements Runnable
{
	private PeerList peer;
	private DatagramSocket socket;
	
	public ReceiveThread(PeerList peer)
	{
		this.peer = peer;
		this.socket = peer.getSocket();
	}
	
	public void run()
	{
		try
		{
			byte[] incomingData = new byte[1024];
			int targetIndex;

        	while(true) 
        	{
        		// receive data from another peer
            	DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            	socket.receive(incomingPacket);
            
            	//get peer's IP address
            	InetAddress otherIP = incomingPacket.getAddress();
			                
            	//update the map with the correct timestamp and status
            	targetIndex = peer.searchPeerWithIP(otherIP.toString().substring(1));
            	peer.updatePeerTimestamp(targetIndex, System.currentTimeMillis());
            	peer.updatePeerStatus(targetIndex, true);
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
	}
}