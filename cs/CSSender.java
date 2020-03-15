package cs;

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
	private Host hosts;
	private DatagramSocket socket;
	private Random timer;
	private InetAddress myIP;
	
	public CSSender(Host hosts, InetAddress myIP)
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
			Thread.sleep(35000);
			System.out.println("Sender is now ready");
			System.out.println();
			
			while (true)
			{
				// to start, probe the list of IPs for server
				String serverIP = hosts.probeServerIP();
				hosts.getHostInfo(hosts.searchHostbyIP(serverIP)).updateServerStatus(true);
				
				// the host is the server
				while (serverIP.equals(myIP.toString().substring(1)))
				{
					// update server and client info
					for (int index1 = 0; index1 < hosts.getHostListSize(); index1++)
					{
						// host not active
						if (!hosts.getHostInfo(index1).getIPAddress().equals(serverIP) && 
								hosts.getHostInfo(index1).getTimeStamp() <=
								System.currentTimeMillis() - 30000)
						{
							hosts.getHostInfo(index1).updateStatus(false);
						}
						
						System.out.println(hosts.getHostInfoSummary(index1));
					}
					
					// send the list of hosts info to other hosts
					for (int index1 = 0; index1 < hosts.getHostListSize(); index1++)
					{
						if (!hosts.getHostInfo(index1).getIPAddress().equals(serverIP))
						{
							String current = hosts.getHostInfo(index1).getIPAddress();
							InetAddress destIP = InetAddress.getByName(current);
							
							for (int index2 = 0; index2 < hosts.getHostListSize(); index2++)
							{
								
								String IP = hosts.getHostInfo(index2).getIPAddress();
								
								byte[] IPData = IP.getBytes();
								
								DatagramPacket IPPacket = new DatagramPacket(IPData, IPData.length,
										destIP, 9876);
								socket.send(IPPacket);
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
				
				HostInfo server = hosts.getHostInfo(hosts.searchHostbyIP(serverIP));
				boolean serverDown = false;
				
				// the host is the client
				while (!serverDown)
				{
					InetAddress destIP = InetAddress.getByName(serverIP);
					
					String sentence = myIP.toString().substring(1);
					byte[] data = sentence.getBytes();
					
					DatagramPacket sendPacket = new DatagramPacket(data, data.length, destIP, 9876);
					socket.send(sendPacket);
					
					System.out.print("Info sent to server at: ");
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
					 LocalDateTime now = LocalDateTime.now();  
					 System.out.println(dtf.format(now));  
					System.out.println();
					
					Thread.sleep(timer.nextInt(29001) + 1000);
					
					serverDown = server.getTimeStamp() < System.currentTimeMillis() - 30000 ?
							true : false;
				}
				
				// server is down
				hosts.getHostInfo(hosts.searchHostbyIP(serverIP)).updateStatus(false);
				hosts.getHostInfo(hosts.searchHostbyIP(serverIP)).updateServerStatus(false);
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
