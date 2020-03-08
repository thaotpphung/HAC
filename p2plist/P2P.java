package p2plist;

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
	private ArrayList<PeerInfo> activeList; // list of active peers

	public P2P() {
		try {
			// create my socket
			this.socket = new DatagramSocket(9876);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.activeList = new ArrayList<PeerInfo>();

		// initializes the list of all peers' IP addresses
		// the time stamps are all initialized to 0
		// add the others' IP addresses to the list of active IP address here

		activeList.add(new PeerInfo("192.168.1.109", new Long(0), false));
		activeList.add(new PeerInfo("150.243.16.36", new Long(0), false));

	}

	// a peer is active, change status to on, update timestamp
	public synchronized void turnOn(String ip, Long val) {
		for (int i = 0; i < activeList.size(); i++)
		{
			if(activeList.get(i).getIp().equals(ip))
			{
				activeList.get(i).setStatus(true);
				activeList.get(i).setlastReceived(val);
			}
		}
	}

	// a peer is offline, change status to off
	public synchronized void turnOff(PeerInfo aPeer) {
		aPeer.setStatus(false);
	}

	// return the list of active IP addresses
	public synchronized ArrayList<PeerInfo> getList() {
		return this.activeList;
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
