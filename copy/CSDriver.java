package cs.copy;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CSDriver
{
	public static void main(String[] args)
	{
		try
		{
			File file = new File("/Users/annie/truman/network/HACproject/src/cs/test.txt");
			HostList hosts = new HostList();
			
			Scanner s = new Scanner(file);
			String senderIP;
			String inputIP;
			int id;
			
			senderIP = s.nextLine();
			
			while (s.hasNext())
			{
				inputIP = s.next();
				id = s.nextInt();
				
				hosts.addHost(new Host(inputIP, System.currentTimeMillis() + 35000, id));
			}
			
			System.out.println("Completed reading; will start C-S protocol shortly.");
			
			Thread sender = new Thread(new CSSender(hosts, InetAddress.getByName(senderIP)));
			Thread receiver = new Thread(new CSReceiver(hosts));
			
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
