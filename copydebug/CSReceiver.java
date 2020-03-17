package cs.copydebug;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.io.IOException;

public class CSReceiver implements Runnable {
	private HostList hosts;
	private DatagramSocket socket;

	public CSReceiver(HostList hosts) {
		this.hosts = hosts;
		socket = hosts.getSocket();
	}

	public void run() {
		try {
			byte[] incomingData;
			int targetIndex;

			while (true) {
				// receive host info
				incomingData = new byte[1024];
				DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
				socket.receive(incomingPacket);

				// update the host info
				String messageReceived = new String(incomingPacket.getData());

				String[] infoList = messageReceived.split(" ");
				
				// array of info of the host received, at index 0 is the ip, at index 1 is the
				// server status of that host
				
				System.out.println("hi from receiver");
				
				try {
					String receivedIP = infoList[0];
					String isServer = infoList[1];
					String isActive = infoList[2];
					
					System.out.println("receive: IP: \"" + receivedIP + "\" isServer: \"" + isServer + "\"" + " isActive: \"" 
				+ isActive);
					
					targetIndex = hosts.getHostbyIP(receivedIP);
					
					if (isServer.startsWith("true"))
					{
						hosts.getHost(targetIndex).updateTimeStamp(System.currentTimeMillis()); // update host's time stamp
						hosts.getHost(targetIndex).updateStatus(true); // update active status
						hosts.getHost(hosts.getHostbyIP(receivedIP)).updateServerStatus(true);
						System.out.println("update " + receivedIP + " to be server");
						
					} else {
						hosts.getHost(hosts.getHostbyIP(receivedIP)).updateServerStatus(false);
						System.out.println("update " + receivedIP + " to be client");
						System.out.println("isActive: isActive" + " but get " + (isActive.startsWith("true") ? true : false));
						hosts.getHost(targetIndex).updateStatus((isActive.startsWith("true") ? true : false)); // update active status	
					}
					
					System.out.println();
				} catch (IndexOutOfBoundsException e) {
					System.out.println("Data was corrupt, wait for sender to resend");
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
