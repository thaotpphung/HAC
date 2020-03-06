package p2p;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class P2P {
	// private double version;
	private DatagramSocket socket = null;
	// Map that maps IP addresses to time stamps
	private ArrayList<PeerInfo> peerInfoList;

	public P2P() {
		try {
			// create my socket
			this.socket = new DatagramSocket(9876);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.peerInfoList = new ArrayList<PeerInfo>();
		// initializes the list of all peers' IP addresses
		// the time stamps are all initialized to 0

		// Annie's IP
		peerInfoList.add(new PeerInfo("150.243.198.135", new Long(0)));
		// Robert's IP
		peerInfoList.add(new PeerInfo("150.243.213.204", new Long(0)));
		// Josh's IP
		peerInfoList.add(new PeerInfo("150.243.211.102", new Long(0)));
	}

	// adds to active list
	public synchronized void addToList(String ip, Long val) {
		peerInfoList.add(new PeerInfo(ip, val));
	}

//		// removes from the active list
//		public synchronized void removeFromList(String ip) {
//			for (int i = 0; i< peerInfoList.size(); i++)
//			{
//				if (peerInfoList.get(i).getIp().equals(ip))
//				{
//					peerInfoList.remove(i);
//				}
//			}
//		}

	// removes from the active list
	public synchronized void removeFromList(PeerInfo aPeer) {
		peerInfoList.remove(aPeer);
	}

	public synchronized ArrayList<PeerInfo> getList() {
		return this.peerInfoList;
	}

	public static void main(String args[]) {
		P2P peer = new P2P();
		Thread receiver = new Thread(new P2PReceiver(peer.socket, peer));
		Thread sender = new Thread(new P2PSender(peer.socket, peer));
		receiver.start();
		sender.start();
		try {
			receiver.join();
			sender.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
