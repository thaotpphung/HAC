package p2p;

import java.util.Scanner;
import java.net.*;

public class P2PDriver {
	public static void main(String[] args)
	{
		Scanner s = new Scanner(System.in);
		String input;
		
		try
		{
			// enter 'your' IP address first
			System.out.println("Input your IP address:");
			input = s.next();
			System.out.println();
			P2P peer = new P2P();
			
			// create P2PSender object using the input IP address
			Thread sender = new Thread(new P2PSender(peer, InetAddress.getByName(input)));
			
			// enter recipients' IP addresses
			System.out.println("Input the recipient peer's IP addresses.");
			System.out.println("Enter 'done' without quotes when done.");
			
			input = s.next();
			System.out.println();
			
			while (!input.equals("done"))
			{
				// add each peer's information into the P2P object
				peer.addPeer(new PeerInfo(input, System.currentTimeMillis()));
				
				input = s.next();
				System.out.println();
			}
			
			s.close();
			
			System.out.println("Starting P2P");
			
			// create receiver object; this updates the active peers information to be used by the sender
			Thread receiver = new Thread(new P2PReceiver(peer));
			
			sender.start();
			receiver.start();
			
			sender.join();
			receiver.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}
}
