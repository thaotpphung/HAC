package p2p;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.*;

public class P2PDriver {
	public static void main(String[] args) {
		try {
			// read IP and ID list from file
			File file = new File("/Users/annie/truman/network/HACproject/HAC/src/p2p/test.txt");
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