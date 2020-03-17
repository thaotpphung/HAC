package p2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

public class SendThread implements Runnable
{
	private PeerList peer;
	private DatagramSocket socket;
	private Random timer;
	private InetAddress myIP;
	
	public SendThread(PeerList peer, InetAddress myIP)
	{
		this.peer = peer;
		this.socket = peer.getSocket();
		this.timer = new Random();
		this.myIP = myIP;
	}
	
	public void run()
	{
		try
		{	
			while (true)
			{
				//iterates through ip addresses
				for (int index1 = 0; index1 < peer.getListSize(); index1++)
				{
					// peer not active
					if (peer.getPeerInfo(index1).getTimeStamp() <= System.currentTimeMillis() - 30000)
					{
						peer.updatePeerStatus(index1, false);
					}
					
					// print out the summary of the selective peer info (ip address and status)
	            	System.out.println(peer.getPeerStatus(index1));
					
	            	// send my peer info if the selected peer is active
					// if (peer.getPeerInfo(index1).getStatus())
					// {
						String current = peer.getPeerInfo(index1).getIPAddress();
						InetAddress destIP = InetAddress.getByName(current);
						
						String sentence = myIP.toString();
						byte[] data = sentence.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(data, data.length, destIP, 9876);
						socket.send(sendPacket);
					// }
				}
				
				System.out.println();
				//sleeps a random amount of time from 0-30 seconds
				Thread.sleep(timer.nextInt(30000));
				
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
}