package p2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Random;

public class P2PSender implements Runnable
{
	private P2P peer;
	private DatagramSocket socket;
	private Random timer;
	P2PSender(DatagramSocket socket, P2P peer)
	{
		this.peer = peer;
		this.socket = socket;
		this.timer = new Random();
	}
	
	public void run()
	{
		try
		{
			//gets local ip address
			InetAddress myIP = InetAddress.getByName("localhost");
			
			//gets an iterator through the active IP adresses
			Iterator<String> IPs = peer.getIterator();
			//iterates through active ip adresses
			while(IPs.hasNext())
			{
				String current = IPs.next();
				
				//removes peer from map
				peer.removeFromMap(current);
				
				InetAddress destIP = InetAddress.getByName(current);
				String sentence = myIP.toString();
				byte[] data = sentence.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(data, data.length, destIP, 9876);
				socket.send(sendPacket);
				System.out.println("IP sent to " + current);
			}
			//sleeps a random amount of time from 0-30 seconds
			Thread.sleep(timer.nextInt(30000));
			while(true)
			{
				//gets a new iterator though ip addresses
				IPs = peer.getIterator();
				while(IPs.hasNext())
				{
					String current = IPs.next();
					if(peer.getValue(current) >= 30000 + System.currentTimeMillis())//if peer has timed out
					{
						peer.removeFromMap(current);
					}
					else//if peer is active
					{
						InetAddress destIP = InetAddress.getByName(current);
						String sentence = myIP.toString();
						byte[] data = sentence.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(data, data.length, destIP, 9876);
						socket.send(sendPacket);
						System.out.println("IP sent to " + current);
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
