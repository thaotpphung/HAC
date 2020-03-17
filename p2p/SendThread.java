package p2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
				//iterates through IP addresses
				for (int index = 0; index < peer.getListSize(); index++)
				{
					// check if peer is still active, update peer status accordingly
					if (peer.getPeer(index).getTimeStamp() <= System.currentTimeMillis() - 30000)
					{
						peer.updatePeerStatus(index, false);
					}
					
					// print out the summary of the selective peer info (IP address and status)
	            	System.out.println(peer.getPeerSummary(index));
					
					String currentPeer = peer.getPeer(index).getIPAddress();
					InetAddress destIP = InetAddress.getByName(currentPeer);
				
					byte[] data = myIP.toString().getBytes();
					DatagramPacket sendPacket = new DatagramPacket(data, data.length, destIP, 9876);
					socket.send(sendPacket);
				}
				
				System.out.print("Finished sending my IP to other peers at: ");
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				System.out.println(dtf.format(now));
				System.out.println();
				
				// sleeps a random amount of time from 0-30 seconds before send again
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