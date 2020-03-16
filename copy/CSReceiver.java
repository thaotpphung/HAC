package cs.copy;

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
			int targetIndex;
			
			while (true)
			{
				// receive host info
				incomingData = new byte[1024];
				DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
				socket.receive(incomingPacket);
				
				// update the host info
				String messageReceived = new String(incomingPacket.getData());
				
				String[] infoList = messageReceived.split(" ");
				
				// array of info of the host received, at index 0 is the ip, at index 1 is the server status of that host
				
				targetIndex = hosts.searchHostbyIP(infoList[0]);
				hosts.getHostInfo(targetIndex).updateTimeStamp(System.currentTimeMillis()); // update host's time stamp
				hosts.getHostInfo(targetIndex).updateStatus(true); // update active status
				
				// update the server status of the receiving packages
				if(infoList[1].startsWith("true"))
				{
					hosts.getHostInfo(hosts.searchHostbyIP(infoList[0])).updateServerStatus(true);
				}
				else
				{
					hosts.getHostInfo(hosts.searchHostbyIP(infoList[0])).updateServerStatus(false);
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
