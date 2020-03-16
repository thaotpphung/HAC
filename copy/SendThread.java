package cs.copy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Sender Thread to send information, works differently depending on the mode
 * @version 3/16/2020
 */
public class SendThread implements Runnable {
	private HostList hostList;
	private DatagramSocket socket;
	private Random timer;
	private InetAddress myIP;

	/**
	 * constructor for Sender
	 * @param hostList the list of hosts
	 * @param myIP the IP of this host
	 */
	public SendThread (HostList hostList, InetAddress myIP) {
		this.hostList = hostList;
		this.socket = hostList.getSocket();
		this.myIP = myIP;
		timer = new Random();
	}
	
	/**
	 * run method of the Thread
	 */
	public void run() {
		try {
			System.out.println("Hold on, sender is getting ready . . .");
			// wait to receive information like IP addresses and their server status for
			// initial setup
			Thread.sleep(35000);
			System.out.println("Sender is now ready");

			while (true) {
				// before trying to find the server IP, update the active status and time stamp
				// of this host
				int targetIndex = hostList.getHostbyIP(myIP.toString().substring(1));
				hostList.getHost(targetIndex).updateTimeStamp(System.currentTimeMillis());
				hostList.getHost(targetIndex).updateActiveStatus(true);

				// to start, get the server IP address
				String serverIP = hostList.getServerIP();
				// update the server status of the server
				hostList.getHost(hostList.getHostbyIP(serverIP)).updateServerStatus(true);
				System.out.println("Done searching, server found: " + serverIP);

				// check if this host is a server
				if (serverIP.equals(myIP.toString().substring(1))) {
					serverMode(serverIP, hostList);
				} else {
					// the host is a client
					clientMode(serverIP, hostList);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * send information by server mode
	 * send active hosts' IP addresses and their server status to all clients
	 * @param serverIP
	 * @param hostList
	 */
	private void serverMode(String serverIP, HostList hostList) {
		try {
			while (true) {
				for (int index = 0; index < hostList.getHostListSize(); index++) {
					// check which clients are active and update their active status accordingly
					if (!hostList.getHost(index).getIPAddress().equals(serverIP)
							&& hostList.getHost(index).getTimeStamp() <= System.currentTimeMillis() - 30000) {
						hostList.getHost(index).updateActiveStatus(false);
					}
					// print the summary of that host
					System.out.println(hostList.getHostInfo(index));
				}

				// send the list of IPs and their server status to all clients
				for (int index1 = 0; index1 < hostList.getHostListSize(); index1++) {
					if (!hostList.getHost(index1).getIPAddress().equals(serverIP)) {
						String currentClient = hostList.getHost(index1).getIPAddress(); // the client to be sent the
																						// list to
						InetAddress destIP;
						destIP = InetAddress.getByName(currentClient);

						// only send IP and server status list of those who are active to a client
						for (int index2 = 0; index2 < hostList.getHostListSize(); index2++) {
							if (hostList.getHost(index2).getActiveStatus()) {

								String IP = hostList.getHost(index2).getIPAddress();
								String isServer = String.valueOf(hostList.getHost(index2).getServerStatus());
								String message = IP + " " + isServer;
								
								byte[] messageToByte = message.getBytes();
								DatagramPacket IPPacket = new DatagramPacket(messageToByte, messageToByte.length,
										destIP, 9876);
								socket.send(IPPacket);
							}
						}
					}
				}
				System.out.print("Finished sending list to clients at: ");
				printTime();
				Thread.sleep(timer.nextInt(30000));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * send information by client mode
	 * send this host's IP address and server status to the server
	 * @param serverIP
	 * @param hostList2
	 */
	private void clientMode(String serverIP, HostList hostList2) {
		try {
			Host server = hostList.getHost(hostList.getHostbyIP(serverIP));
			boolean serverDown = false;
			while (!serverDown) {
				// destination IP address is the server IP address
				InetAddress destIP = InetAddress.getByName(serverIP);

				// send the host's IP address and server status to server
				String IP = myIP.toString().substring(1);
				String isServer = "false"; // because the host is a client
				String message = IP + " " + isServer;
				
				byte[] messageToByte = message.getBytes();
				DatagramPacket IPPacket = new DatagramPacket(messageToByte, messageToByte.length, destIP, 9876);
				socket.send(IPPacket);

				System.out.print("Info sent to server at: ");
				printTime();
				// wait for random amount of time less than 30s to send again
				Thread.sleep(timer.nextInt(30000));
				// check if the server is down
				serverDown = server.getTimeStamp() < System.currentTimeMillis() - 35000 ? true : false;
			}

			// server is down
			hostList.getHost(hostList.getHostbyIP(serverIP)).updateActiveStatus(false);
			hostList.getHost(hostList.getHostbyIP(serverIP)).updateServerStatus(false);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * print our the current time
	 */
	private void printTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		System.out.println(dtf.format(now));
		System.out.println();
	}
}