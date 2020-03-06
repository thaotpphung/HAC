package p2p;

import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class P2P {
	// private double version;
	private DatagramSocket socket = null;
	// Map that maps IP addresses to time stamps
	private Map<String, Long> IpStampMap;

	public P2P() {
		try {
			// create my socket
			this.socket = new DatagramSocket(9876);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.IpStampMap = new HashMap<String, Long>();
		// initializes the list of all peers' IP addresses
		// the time stamps are all initialized to 0

		// Annie's IP
		IpStampMap.put("150.243.198.135", new Long(0));
		// Robert's IP
		IpStampMap.put("150.243.213.204", new Long(0));
		// Josh's IP
//		IpStampMap.put("150.243.198.135", new Long(0));
		
	}

	// adds a time stamp to the map, will replace existing ones
	public synchronized void addToMap(String IP, Long val) {
		IpStampMap.put(IP, val);
	}

	// gets an iterator of all the active IP addresses
	public synchronized Iterator<String> getIterator() {
		return IpStampMap.keySet().iterator();
	}

	// gets the time stamp associated with a key
	public synchronized long getValue(String key) {
		return IpStampMap.get(key);
	}

	// removes a time stamp from the map
	public synchronized void removeFromMap(String IP) {
		IpStampMap.remove(IP);
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
