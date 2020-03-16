package p2p;

import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.*;

public class P2PDriver {
	public static void main(String[] args)
	{	
		try
		{
			File file = new File("/Users/annie/truman/network/HACproject/src/cs/test.txt");
			Scanner s = new Scanner(file);
			String senderIP;
			String inputIP;
			
			senderIP = s.nextLine();

			P2P peers = new P2P();
			
			// create P2PSender object using the input IP address
			Thread sender = new Thread(new P2PSender(peers, InetAddress.getByName(senderIP)));
			
			while (s.hasNext())
			{
				inputIP = s.next();		
				peers.addPeer(new PeerInfo(inputIP, System.currentTimeMillis()));
			}
			
			System.out.println("Starting Peer To Peer");
			
			// create receiver object; this updates the active peers information to be used by the sender
			Thread receiver = new Thread(new P2PReceiver(peers));
			
			sender.start();
			receiver.start();
			
			sender.join();
			receiver.join();
			
			s.close();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
