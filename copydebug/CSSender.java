package cs.copydebug;

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
	private HostList hostList;
	private DatagramSocket socket;
	private Random timer;
	private InetAddress myIP;
	
	public CSSender(HostList hostList, InetAddress myIP)
	{
		this.hostList = hostList;
		this.socket = hostList.getSocket();
		this.myIP = myIP;
		timer = new Random();
	}
	
	public void run()
	{
		try
		{
			System.out.println("Hold on, sender is getting ready . . . \n");
			Thread.sleep(35000); // wait to receive some ip from senders
			System.out.println("\nSender is now ready");
			
			while (true)
			{
				int targetIndex = hostList.getHostbyIP(myIP.toString().substring(1));
//				System.out.println("myIp is at the index: " + targetIndex);
				hostList.getHost(targetIndex).updateTimeStamp(System.currentTimeMillis()); // update host's time stamp
				hostList.getHost(targetIndex).updateStatus(true); // update active status
				
				System.out.println("\ncheck before probing: ");
				for (int i = 0; i < hostList.getHostListSize(); i++)
				{
					System.out.println("ip: " + hostList.getHost(i).getIPAddress() + ", active: "  
							+ hostList.getHost(i).getStatus() + ", isServer: " + hostList.getHost(i).getServerStatus());
				}
				System.out.println("\n");
				
				// to start, probe the list of IPs for server
				String serverIP = hostList.probeServerIP();
				System.out.println("\ndone probing, server found: " + serverIP );
				// update the server status
				hostList.getHost(hostList.getHostbyIP(serverIP)).updateServerStatus(true);
				System.out.println("set " +serverIP + " to be server \n");
				
				// the host is the server if my ip is server ip
				while (serverIP.equals(myIP.toString().substring(1)))
				{
					// update server and client info
					for (int index1 = 0; index1 < hostList.getHostListSize(); index1++)
					{
						// host not active
						if (!hostList.getHost(index1).getIPAddress().equals(serverIP) && 
								hostList.getHost(index1).getTimeStamp() <=
								System.currentTimeMillis() - 30000)
						{
							hostList.getHost(index1).updateStatus(false);
						}
						
						System.out.println(hostList.getHostSummary(index1));
					}
					
					// send the list of active hosts info to other hosts
					for (int index1 = 0; index1 < hostList.getHostListSize(); index1++)
					{
						// send ip list to all clients
						if (!hostList.getHost(index1).getIPAddress().equals(serverIP))
						{
							String current = hostList.getHost(index1).getIPAddress();
							InetAddress destIP = InetAddress.getByName(current);
							// send IP list by sending IP address of all active IP separately
							for (int index2 = 0; index2 < hostList.getHostListSize(); index2++)
							{
								if(hostList.getHost(index2).getStatus())
								{
									String IP = hostList.getHost(index2).getIPAddress();
									String isServer = String.valueOf(hostList.getHost(index2).getServerStatus());
									String isActive = String.valueOf(hostList.getHost(index2).getStatus());
									String message = IP + " " + isServer + " " + isActive;
									
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
//					System.out.println("sender is client: send: " + message);
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
					Thread.sleep(timer.nextInt(30000));
					
					serverDown = server.getTimeStamp() < System.currentTimeMillis() - 30000 ?
							true : false;
				}
				
				// server is down
				hostList.getHost(hostList.getHostbyIP(serverIP)).updateStatus(false);
				hostList.getHost(hostList.getHostbyIP(serverIP)).updateServerStatus(false);
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