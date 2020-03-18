package p2p;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.*;

public class P2PDriver {
	public static void main(String[] args) {
		try {
			// read IP list from file
			Scanner input = new Scanner(System.in);
			System.out.println("Enter the directory to the text file to be read:");
			String text = input.nextLine();
			
			File file = new File(text);
			Scanner s = new Scanner(file);
			String myIP;
			String inputIP;

			PeerList peerList = new PeerList();

			myIP = s.nextLine();
			while (s.hasNext()) {
				inputIP = s.next();
				peerList.addPeer(new Peer(inputIP, System.currentTimeMillis()));
			}
			
			s.close();
			input.close();
			System.out.println("Completed reading; will start Peer To Peer shortly. \n");

			Thread sender = new Thread(new SendThread(peerList, InetAddress.getByName(myIP)));
			Thread receiver = new Thread(new ReceiveThread(peerList));

			sender.start();
			receiver.start();

			sender.join();
			receiver.join();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}