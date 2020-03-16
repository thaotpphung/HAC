package cs.copydebug;

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
				
				System.out.println("receiver: IP: " + infoList[0] + " isServer: \"" + infoList[1] + "\"");
				// array of info of the host received, at index 0 is the ip, at index 1 is the server status of that host
				
				targetIndex = hosts.searchHostbyIP(infoList[0]);
				hosts.getHostInfo(targetIndex).updateTimeStamp(System.currentTimeMillis()); // update host's time stamp
				hosts.getHostInfo(targetIndex).updateStatus(true); // update active status
				
				// get the sender's IP address
//				String senderIP = incomingPacket.getAddress().toString().substring(1);
				
				// update the server status of the receiving packages
				if(infoList[1].startsWith("true"))
				{
					hosts.getHostInfo(hosts.searchHostbyIP(infoList[0])).updateServerStatus(true);
					System.out.println("receiver: update " + infoList[0] + " to be server");
				}
				else
				{
					hosts.getHostInfo(hosts.searchHostbyIP(infoList[0])).updateServerStatus(false);
					System.out.println("receiver: update " + infoList[0] + " to be client");
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
