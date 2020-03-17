package cs.copydebug;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.io.IOException;

/**
 * A Receiver Thread to receive IP address and server status
 * @author Robert Masek, Sua "Joshua" Lee, Thao Phung 
 * @version 16 March 2020
 */
public class ReceiveThread implements Runnable {
	private HostList hostList;
	private DatagramSocket socket;

	/**
	 * constructor for Receive Thread
	 * @param hostList the list of hostList
	 */
	public ReceiveThread(HostList hostList) {
		this.hostList = hostList;
		socket = hostList.getSocket();
	}
	
	/**
	 * run method of the Thread
	 */
	public void run() {
		try {
			byte[] incomingData;
			int targetIndex;

			while (true) {
				// receive host info
				incomingData = new byte[1024];
				DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
				socket.receive(incomingPacket);
				String messageReceived = new String(incomingPacket.getData());
				
				// array of info of the host received, at index 0 is the ip, at index 1 is the  server status of that host
				String[] infoList = messageReceived.split(" ");
				

				try {
					String receivedIP = infoList[0];
					String isServer = infoList[1];
					String isActive = infoList[2];
					
					System.out.println("receive: IP: \"" + receivedIP + "\" isServer: \"" + isServer + "\"" + " isActive: \"" 
							+ isActive);
					
					String senderIP = incomingPacket.getAddress().toString().substring(1);
					
					// received index
					
					Host receivedHost = hostList.getHost(hostList.getHostbyIP(infoList[0]));
					Host sendHost = hostList.getHost(hostList.getHostbyIP(senderIP));
					
					// update the server status of the receiving packages
					if (infoList[1].startsWith("true")) {
						receivedHost.updateServerStatus(true);
					} else {
						receivedHost.updateServerStatus(false);
					}
					
					// if the sender is the server
					if (sendHost.getServerStatus())
					{
						// if the receiving package is not the server 
						// this means that the sender is a server and this host is a client, the receiving package is another client
						if (!receivedHost.getServerStatus())
						{
//							receivedHost.updateTimeStamp(System.currentTimeMillis()); 
							receivedHost.updateActiveStatus(  (isActive.startsWith("true")) ? true : false  ); 
						}
						// else, the receiving package is the server
						else { 
							// check if sender is receiver, in that case, update like normal
							if (sendHost.getIPAddress().equals(receivedHost.getIPAddress()))
							{
								receivedHost.updateTimeStamp(System.currentTimeMillis()); 
								receivedHost.updateActiveStatus(true); 
							} 
							// else, there are 2 server, this is not valid, remove 1 server
							else { 
								receivedHost.updateServerStatus(false);
							}
						}
					}
					// if the sender is the client, update like normal
					else {
						receivedHost.updateTimeStamp(System.currentTimeMillis()); 
						receivedHost.updateActiveStatus(true); 
					}
					
					
//					targetIndex = hostList.getHostbyIP(infoList[0]);
//					if (infoList[1].startsWith("true")) {
//						hostList.getHost(hostList.getHostbyIP(infoList[0])).updateServerStatus(true);
//					} else {
//						hostList.getHost(hostList.getHostbyIP(infoList[0])).updateServerStatus(false);
//					}
//					
//					hostList.getHost(targetIndex).updateTimeStamp(System.currentTimeMillis()); 
//					hostList.getHost(targetIndex).updateActiveStatus(true); 
					
					
					
					
				} catch (IndexOutOfBoundsException e) {
					System.out.println("Data was corrupt, wait for sender to resend..");
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}