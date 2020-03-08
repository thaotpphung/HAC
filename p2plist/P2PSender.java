package p2plist;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

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
			// get local IP address to send to other peers
			InetAddress myIP = InetAddress.getByName("localhost");
			
			
			// initial setup: first, send my IP address to other peers 
			
			// get the active IP list
			ArrayList<PeerInfo> ipList = peer.getList();
			
			// iterate through active IP addresses
			for (int i = 0; i < ipList.size(); i++)
			{
				PeerInfo currentIP = ipList.get(i);
				// send my IP address to the current peer
				InetAddress destIP = InetAddress.getByName(currentIP.getIp());
				String sentence = myIP.toString();
				byte[] data = sentence.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(data, data.length, destIP, 9876);
				socket.send(sendPacket);
				System.out.println("IP sent to " + currentIP.getIp()+ "\n");
			}
			
					
			// sleep a random amount of time from 0-30 seconds 
			Thread.sleep(timer.nextInt(30000));
			
			// now, send my IP address to any peer who replies
			while(true)
			{
				// get a new iterator though IP addresses
				ipList = peer.getList();
				
				for (PeerInfo aPeer : ipList)
				{
					if (aPeer.getStatus() == true)
					{
						if(aPeer.getlastReceived() <= System.currentTimeMillis() - 30000)//if peer has not been active in the last 30 seconds
						{
							System.out.print("time out: ");
							peer.turnOff(aPeer);
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
