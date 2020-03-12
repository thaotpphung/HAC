package cs;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.io.IOException;

public class CSReceiver implements Runnable
{
	private Host hosts;
	private DatagramSocket socket;
	
	public CSReceiver(Host hosts)
	{
		this.hosts = hosts;
		socket = hosts.getSocket();
	}
	
	public void run()
	{
		try
		{
			byte[] incomingData;
			int receiptCount = 0;
			int targetIndex;
			
			String previousIP = "";
			
			while (true)
			{
				// receive host info
				incomingData = new byte[1024];
				DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
				socket.receive(incomingPacket);
				
				// update the host info
				String hostIP = new String(incomingPacket.getData());
				targetIndex = hosts.searchHostbyIP(hostIP.trim());
				hosts.getHostInfo(targetIndex).updateTimeStamp(System.currentTimeMillis());
				hosts.getHostInfo(targetIndex).updateStatus(true);
				
				// get the sender's IP address
				String packetIP = incomingPacket.getAddress().toString().substring(1);
				
				// it is the first receipt from the sender
				if (receiptCount == 0)
				{
					receiptCount++;
					previousIP = packetIP;
				}
				// the receiver got host info from the same sender
				else if (packetIP.equals(previousIP))
				{
					receiptCount++;
					
					if (receiptCount == hosts.getHostListSize())
					{
						// the receiver concludes that the sender is the server
						hosts.getHostInfo(hosts.searchHostbyIP(packetIP)).updateServerStatus(true);
					}
				}
				else
				{
					// the receiver concludes that the sender is not the server
					receiptCount = 0;
					hosts.getHostInfo(hosts.searchHostbyIP(packetIP)).updateServerStatus(false);
				}
			}
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
