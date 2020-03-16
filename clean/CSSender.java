package cs.clean;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class CSSender implements Runnable
{
	private HostList hosts;
	private DatagramSocket socket;
	private Random timer;
	private InetAddress myIP;
	
	public CSSender(HostList hosts, InetAddress myIP)
	{
		this.hosts = hosts;
		this.socket = hosts.getSocket();
		this.myIP = myIP;
		timer = new Random();
	}
	
	public void run()
	{
		try
		{
			System.out.println("Hold on, sender is getting ready . . .");
			Thread.sleep(35000); // wait to receive some ip from senders
			System.out.println("Sender is now ready");
			
			while (true)
			{
				int targetIndex = hosts.getHostbyIP(myIP.toString().substring(1));
//				System.out.println("myIp is at the index: " + targetIndex);
				hosts.getHost(targetIndex).updateTimeStamp(System.currentTimeMillis()); // update host's time stamp
				hosts.getHost(targetIndex).updateActiveStatus(true); // update active status
				
				System.out.println("check before probing");
				for (int i = 0; i < hosts.getHostListSize(); i++)
				{
					System.out.println("ip: " + hosts.getHost(i).getIPAddress() + ", active: "  
							+ hosts.getHost(i).getActiveStatus() + ", isServer: " + hosts.getHost(i).getServerStatus());
				}
				
				// to start, probe the list of IPs for server
				String serverIP = hosts.getServerIP();
				System.out.println("done probing: server found:" + serverIP);
				// update the server status
				hosts.getHost(hosts.getHostbyIP(serverIP)).updateServerStatus(true);
				System.out.println("set " +serverIP + " to be server");
				System.out.println();
				
				// the host is the server if my ip is server ip
				while (serverIP.equals(myIP.toString().substring(1)))
				{
					// update server and client info
					for (int index1 = 0; index1 < hosts.getHostListSize(); index1++)
					{
						// host not active
						if (!hosts.getHost(index1).getIPAddress().equals(serverIP) && 
								hosts.getHost(index1).getTimeStamp() <=
								System.currentTimeMillis() - 30000)
						{
							hosts.getHost(index1).updateActiveStatus(false);
							System.out.println("IP " + hosts.getHost(index1).getIPAddress() +" become inactive" );
							
						}
						
						System.out.println(hosts.getHostInfo(index1));
					}
					
					// send the list of active hosts info to other hosts
					for (int index1 = 0; index1 < hosts.getHostListSize(); index1++)
					{
						// send ip list to all clients
						if (!hosts.getHost(index1).getIPAddress().equals(serverIP))
						{
							String current = hosts.getHost(index1).getIPAddress();
							InetAddress destIP = InetAddress.getByName(current);
							// send IP list by sending IP address of all active IP separately
							for (int index2 = 0; index2 < hosts.getHostListSize(); index2++)
							{
								if(hosts.getHost(index2).getActiveStatus())
								{
									String IP = hosts.getHost(index2).getIPAddress();
									String isServer = String.valueOf(hosts.getHost(index2).getServerStatus());
									String message = IP + " " + isServer;
//									System.out.println("sender is server: send: " + message);
									byte[] messageToByte = message.getBytes();
									
									DatagramPacket IPPacket = new DatagramPacket(messageToByte, messageToByte.length,
											destIP, 9876);
									socket.send(IPPacket);
								}		
							}
						}
					}
					
					System.out.print("Finished sending list to clients at: ");
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
					LocalDateTime now = LocalDateTime.now();
					System.out.println(dtf.format(now));
					System.out.println();
					
					Thread.sleep(timer.nextInt(30000));
				}
				
				Host server = hosts.getHost(hosts.getHostbyIP(serverIP));
				boolean serverDown = false;
				
				// the host is the client
				while (!serverDown)
				{
					InetAddress destIP = InetAddress.getByName(serverIP);
					
					String IP = myIP.toString().substring(1);
					String isServer = "false"; // because the host is a client
					String message = IP + " " + isServer;
//					System.out.println("sender is client: send: " + message);
					byte[] messageToByte = message.getBytes();
					
					DatagramPacket IPPacket = new DatagramPacket(messageToByte, messageToByte.length,
							destIP, 9876);
					socket.send(IPPacket);
					
					System.out.print("Info sent to server at: ");
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
					LocalDateTime now = LocalDateTime.now();
					System.out.println(dtf.format(now));
					System.out.println();
					Thread.sleep(timer.nextInt(30000));
					
					serverDown = server.getTimeStamp() < System.currentTimeMillis() - 30000 ?
							true : false;
				}
				
				// server is down
				hosts.getHost(hosts.getHostbyIP(serverIP)).updateActiveStatus(false);
				hosts.getHost(hosts.getHostbyIP(serverIP)).updateServerStatus(false);
//				System.out.println("sender: server is down, update " + serverIP + "to be client");
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