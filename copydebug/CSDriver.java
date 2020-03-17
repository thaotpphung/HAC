package cs.copydebug;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/** 
 * driver class for testing client-server HAC protocol
 * @author Robert Masek, Sua "Joshua" Lee, Thao Phung
 * @version 16 March 2020
 */
public class CSDriver
{
	public static void main(String[] args)
	{
		try
		{
			// read IP and ID list from file
			File file = new File("/Users/annie/truman/network/HACproject/HAC/src/cs/copydebug/test.txt");
			Scanner s = new Scanner(file);
			String senderIP;
			String inputIP;
			int id;
			
			HostList hosts = new HostList();
			
			senderIP = s.nextLine();	
			while (s.hasNext())
			{
				inputIP = s.next();
				id = s.nextInt();
				
				hosts.addHost(new Host(inputIP, System.currentTimeMillis() + 35000, id));
			}
			
			System.out.println("Completed reading; will start C-S protocol shortly.");
			
			Thread sender = new Thread(new SendThread(hosts, InetAddress.getByName(senderIP)));
			Thread receiver = new Thread(new ReceiveThread(hosts));
			
			sender.start();
			receiver.start();
			
			sender.join();
			receiver.join();
			
			s.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}