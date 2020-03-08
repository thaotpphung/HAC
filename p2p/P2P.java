package p2p;

import java.net.*;
import java.util.ArrayList;

public class P2P {
	private DatagramSocket socket = null;
	// Map that maps IP addresses to time stamps
	private ArrayList<PeerInfo> peerList;
	
	public P2P() 
	{
		try
		{
			// create my socket
			this.socket = new DatagramSocket(9876);
		}
		catch (SocketException e) 
        {
            e.printStackTrace();
        }
		this.peerList = new ArrayList<PeerInfo>();
		// initialize the list of all peers' IP addresses
		
	}
	
	public synchronized void addPeer(PeerInfo peer)
	{
		peerList.add(peer);
	}
	
	public PeerInfo getPeerInfo(int index)
	{
		return peerList.get(index);
	}
	
	public void updatePeerStatus(int index, boolean status)
	{
		peerList.get(index).updateStatus(status);
	}
	
	public void updatePeerTimestamp(int index, long time)
	{
		peerList.get(index).updateTimeStamp(time);
	}
	
	public int searchPeerWithIP(String IP)
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
	
	public String getPeerStatus(int index)
	{
		return peerList.get(index).toString();
	}
}
