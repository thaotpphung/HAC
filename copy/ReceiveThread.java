package cs.copy;

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
	private HostList hosts;
	private DatagramSocket socket;

	/**
	 * constructor for Receive Thread
	 * @param hosts the list of hosts
	 */
	public ReceiveThread(HostList hosts) {
		this.hosts = hosts;
		socket = hosts.getSocket();
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
					// update the active status and time stamp of received host
					targetIndex = hosts.getHostbyIP(infoList[0]);
					hosts.getHost(targetIndex).updateTimeStamp(System.currentTimeMillis()); // update host's time stamp
					hosts.getHost(targetIndex).updateActiveStatus(true); // update active status
					// update the server status of the receiving packages
					if (infoList[1].startsWith("true")) {
						hosts.getHost(hosts.getHostbyIP(infoList[0])).updateServerStatus(true);
					} else {
						hosts.getHost(hosts.getHostbyIP(infoList[0])).updateServerStatus(false);
						
					}
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