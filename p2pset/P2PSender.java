package p2pset;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class P2PSender implements Runnable
{
	private P2P peer;
	private DatagramSocket socket;
	private Random timer;
	
	public P2PSender(DatagramSocket socket, P2P peer)
	{
		this.peer = peer;
		this.socket = socket;
		this.timer = new Random();
	}
	
	public void run()
	{
		try
		{
			// first, send my IP address to other peers, initial setup
			// get local IP address to send to other peers
			InetAddress myIP = InetAddress.getByName("localhost");
			
			// get the active IP list
			Set<PeerInfo> active = peer.getList();
			
			// iterate through active IP addresses
			for (PeerInfo aPeer : active)
			{
				// send my IP address to the current peer
				InetAddress destIP = InetAddress.getByName(aPeer.getIp());
				String sentence = myIP.toString();
				byte[] data = sentence.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(data, data.length, destIP, 9876);
				socket.send(sendPacket);
				System.out.println("IP sent to " + aPeer.getIp()+ "\n");
			}
			
			active.clear();
					
			// sleep a random amount of time from 0-30 seconds
			Thread.sleep(timer.nextInt(30000));
			
			// now, send my IP address to any peer who replies
			while(true)
			{
				// get a new iterator though IP addresses
				active = peer.getList();
				
				for (PeerInfo aPeer : active)
				{
					if(aPeer.getlastReceived() <= System.currentTimeMillis() - 30000)//if peer has not been active in the last 30 seconds
					{
						peer.removeFromList(aPeer);
					}
					else//if peer is active
					{
						InetAddress destIP = InetAddress.getByName(aPeer.getIp());
						String sentence = myIP.toString();
						byte[] data = sentence.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(data, data.length, destIP, 9876);
						socket.send(sendPacket);
						System.out.println("IP sent to " + aPeer.getIp() + "\n");
					}
				}
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