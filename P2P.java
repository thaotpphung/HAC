package p2p;

import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class P2P {
//	private double version;
	private DatagramSocket socket = null;
	private Map<String, Long> test;
	
	public P2P() 
	{
		try
		{
			this.socket = new DatagramSocket(9876);
		}
		catch (SocketException e) 
        {
            e.printStackTrace();
        }
		this.test = new HashMap<String, Long>();
		//initial ip addresses
		test.put("localhost", new Long(0));
	}
	
	//adds a timestamp to the map, will replace existing ones
	public synchronized void addToMap(String IP, Long val)
	{
		test.put(IP, val);
	}
	
	//gets an iterator of all the active ip addresses
	public synchronized Iterator<String> getIterator()
	{
		return test.keySet().iterator();
	}
	
	//gets the timestamp associated with a key
	public synchronized long getValue(String key)
	{
		return test.get(key);
	}
	
	//clears the map of all timestamps
	public synchronized void clearMap()
	{
		test.clear();
	}
	
	//removes a timestamp from the map
	public synchronized void removeFromMap(String IP)
	{
		test.remove(IP);
	}
	
	public static void main(String args[]) 
	{  
		P2P peer = new P2P();
		Thread receiver = new Thread(new P2PReceiver(peer.socket, peer));
		Thread sender = new Thread(new P2PSender(peer.socket, peer));
		receiver.start();
		sender.start();
		try
		{
			receiver.join();
			sender.join();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
