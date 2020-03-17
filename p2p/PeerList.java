package p2p;

import java.net.*;
import java.util.ArrayList;

/**
 * A class representing the list of peers, with relevant operations
 * @version 3/16/2020
 */
public class PeerList {
	private DatagramSocket socket = null;
	private ArrayList<Peer> peerList;
	
	/**
	 * constructor for PeerList, create socket and initialize the peer list
	 */
	public PeerList() 
	{
		try
		{
			this.socket = new DatagramSocket(9876);
		}
		catch (SocketException e) 
        {
            e.printStackTrace();
        }
		this.peerList = new ArrayList<Peer>();
	}
	
	/**
	 * add a new peer to the list of peer
	 * @param peer the new peer
	 */
	public void addPeer(Peer peer)
	{
		peerList.add(peer);
	}
	
	/**
	 * get the Peer object by its index
	 * @param index the index of the desired peer
	 * @return the desired Peer object 
	 */
	public synchronized Peer getPeer(int index)
	{
		return peerList.get(index);
	}
	
	/**
	 * updae th
	 * @param index
	 * @param status
	 */
	public synchronized void updatePeerStatus(int index, boolean status)
	{
		peerList.get(index).updateStatus(status);
	}
	
	public synchronized void updatePeerTimestamp(int index, long time)
	{
		peerList.get(index).updateTimeStamp(time);
	}
	
	public synchronized int searchPeerWithIP(String IP)
	{
		int result = -1;
		
		for (int index = 0; index < peerList.size(); index++)
		{
			if (peerList.get(index).getIPAddress().equals(IP))
			{
				return index;
			}
		}
		
		return result;
	}
	
	public int getListSize()
	{
		return peerList.size();
	}
	
	public DatagramSocket getSocket()
	{
		return socket;
	}
	
	public synchronized String getPeerSummary(int index)
	{
		return peerList.get(index).toString();
	}
}