package cs.copydebug;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Sender Thread to send information, works differently depending on the mode
 * @author Robert Masek, Sua "Joshua" Lee, Thao Phung
 * @version 16 March 2020
 */
public class SendThread implements Runnable
{
	private HostList hostList;
	private DatagramSocket socket;
	private Random timer;
	private InetAddress myIP;
	
	/**
	 * constructor for Sender
	 * @param hostList the list of hostList
	 * @param myIP the IP of this host
	 */
	public SendThread(HostList hostList, InetAddress myIP)
	{
		this.hostList = hostList;
		this.socket = hostList.getSocket();
		this.myIP = myIP;
		timer = new Random();
	}
	

	/**
	 * run method of the Thread
	 */
	public void run()
	{
		try
		{
			System.out.println("Hold on, sender is getting ready . . .");
			// wait to receive information like IP addresses and their server status for initial setup
			Thread.sleep(35000); 
			System.out.println("Sender is now ready");
			
			while (true)
			{
				// before trying to find the server IP, update the active status and time stamp  of this host
				int targetIndex = hostList.getHostbyIP(myIP.toString().substring(1));
				hostList.getHost(targetIndex).updateTimeStamp(System.currentTimeMillis()); 
				hostList.getHost(targetIndex).updateActiveStatus(true); 
				
				System.out.println("check b4 probing");
				hostList.displayList();
				System.out.println();
				// to start, get the server IP address
				String serverIP = hostList.getServerIP();
				// update the server status of the server
				hostList.getHost(hostList.getHostbyIP(serverIP)).updateServerStatus(true);
				System.out.println("Done probing, server found: " + serverIP + "\n");
				
				// when this host is a server
				while (serverIP.equals(myIP.toString().substring(1)))
				{
					for (int index1 = 0; index1 < hostList.getHostListSize(); index1++)
					{
						// check which clients are active and update their active status accordingly
						if (!hostList.getHost(index1).getIPAddress().equals(serverIP) && 
								hostList.getHost(index1).getTimeStamp() <=
								System.currentTimeMillis() - 30000)
						{
							hostList.getHost(index1).updateActiveStatus(false);	
						}
						System.out.println(hostList.getHostSummary(index1));
					}
					
					// send the list of IPs and their server status to all clients
					for (int index1 = 0; index1 < hostList.getHostListSize(); index1++)
					{
						if (!hostList.getHost(index1).getIPAddress().equals(serverIP))
						{
							String currentClient = hostList.getHost(index1).getIPAddress();
							InetAddress destIP = InetAddress.getByName(currentClient);
							// send list of packets containing IPs and their server status by sending each packet separately 
							for (int index2 = 0; index2 < hostList.getHostListSize(); index2++)
							{
//								// only active hostList' packet to a client
//								if(hostList.getHost(index2).getActiveStatus())
//								{
									String IP = hostList.getHost(index2).getIPAddress();
									String isServer = String.valueOf(hostList.getHost(index2).getServerStatus());
									String isActive = String.valueOf(hostList.getHost(index2).getActiveStatus());
									String message = IP + " " + isServer + " " + isActive;
									byte[] messageToByte = message.getBytes();
									
									DatagramPacket packet = new DatagramPacket(messageToByte, messageToByte.length,
											destIP, 9876);
									socket.send(packet);
//								}		
							}
						}
					}
					
					System.out.print("Finished sending list to clients at: ");
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
					LocalDateTime now = LocalDateTime.now();
					System.out.println(dtf.format(now));
					System.out.println();
					// sleeps a random amount of time from 0-30 seconds before send again
					Thread.sleep(timer.nextInt(30000));
				}
				
				Host server = hostList.getHost(hostList.getHostbyIP(serverIP));
				boolean serverDown = false;
				
				// the host is the client
				while (!serverDown)
				{
					InetAddress destIP = InetAddress.getByName(serverIP);
					
					String IP = myIP.toString().substring(1);
					String isServer = "false"; // because the host is a client
					String isActive = "true"; // this host is active
					String message = IP + " " + isServer + " " + isActive;

					byte[] messageToByte = message.getBytes();
					
					DatagramPacket IPPacket = new DatagramPacket(messageToByte, messageToByte.length,
							destIP, 9876);
					socket.send(IPPacket);
					
					hostList.displayList();
					System.out.print("Finished sending my info to server at: ");
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
					LocalDateTime now = LocalDateTime.now();
					System.out.println(dtf.format(now));
					System.out.println();
					//sleeps a random amount of time from 0-30 seconds before send again
					Thread.sleep(timer.nextInt(30000));
					serverDown = server.getTimeStamp() < System.currentTimeMillis() - 30000 ?
							true : false;
				}
				
				// server is down
				hostList.getHost(hostList.getHostbyIP(serverIP)).updateActiveStatus(false);
				hostList.getHost(hostList.getHostbyIP(serverIP)).updateServerStatus(false);
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
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}