package p2pt;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class P2P {
	private DatagramSocket socket = null;
	private Set<PeerInfo> active;

	public P2P() {
		try {
			// create my socket
			this.socket = new DatagramSocket(9876);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.active = new HashSet<PeerInfo>();
		// initializes the list of all peers' IP addresses
		// the time stamps are all initialized to 0

		// add the others' IP addresses to the list of active IP address here

		// Annie's IP
//		active.add(new PeerInfo("150.243.198.135", new Long(0)));
		// Robert's IP
//		active.add(new PeerInfo("150.243.213.204", new Long(0)));
		// Josh's IP
//		active.add(new PeerInfo("150.243.211.102", new Long(0)));
//		active.add(new PeerInfo("192.168.1.112", new Long(0)));
		active.add(new PeerInfo("150.243.103.40", new Long(0)));
		active.add(new PeerInfo("150.243.144.126", new Long(0)));

	}

	// adds to active list
	public synchronized void addToList(String ip, Long val) {
		active.add(new PeerInfo(ip, val));
	}

	// removes from the active list by PeerInfo
	public synchronized void removeFromList(PeerInfo aPeer) {
		active.remove(aPeer);
	}

	// removes from the active list by IP address
	public synchronized void removeFromList(String ip) {
		for (PeerInfo peer : active) {
			if (peer.getIp().equals(ip)) {
				active.remove(peer);
			}
		}
	}

	// return the list of active IP addresses
	public synchronized Set<PeerInfo> getList() {
		return this.active;
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
