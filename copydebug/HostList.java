package cs.copydebug;

import java.net.*;
import java.util.ArrayList;

public class HostList
{
	private ArrayList<Host> hostList;
	private DatagramSocket socket;
	
	public HostList()
	{
		try
		{
			socket = new DatagramSocket(9876);
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		hostList = new ArrayList<Host>();
	}
	
	public void addHost(Host newInfo)
	{
		hostList.add(newInfo);
	}
	
	public int getHostbyIP(String IP)
	{
		int notFound = -1;
		
		for (int index = 0; index < hostList.size(); index++)
		{
			if (hostList.get(index).getIPAddress().equals(IP))
			{
				return index;
			}
		}
		
		return notFound;
	}
	
	public Host getHost(int index)
	{
		return hostList.get(index);
	}
	
	/**
	 * get the IP address of the server 
	 * go through the list of 
	 * else,  
	 * @return
	 */
	public String probeServerIP()
	{
		for (int index = 0; index < hostList.size(); index++)
		{
			if (hostList.get(index).getStatus() && hostList.get(index).getServerStatus())  
			{
				System.out.println("probeServer: found an existing active server");
				return hostList.get(index).getIPAddress();
			}
		}
		
		System.out.println("probeServer: could't find existing active server, set the first active in the list to be server");
		// if no one is the server, return the smallest active host
		return getIPAddressByID(getMinID());
	}
	
	public int getMinID()
	{	
		int result = hostList.size();
//		System.out.println("get min id:");
		for (int index = 0; index < hostList.size(); index++)
		{
//			System.out.println(hostList.get(index).getIPAddress() + " active:" + hostList.get(index).getStatus());
			if (hostList.get(index).getStatus())
			{
				return hostList.get(index).getID();
			}
		}
		
//		System.out.println("getminid fail, retun hostlist size");
		return result;
	}
	
	public String getIPAddressByID(int id)
	{
		String result = "";
		boolean found = false;
		
		for (int index = 0; !found && index < hostList.size(); index++)
		{
			if (hostList.get(index).getID() == id)
			{
				result = hostList.get(index).getIPAddress();
				found = true;
			}
		}
//		System.out.println("getipbyid: IP found: " + result);
		return result;
	}
	
	public DatagramSocket getSocket()
	{
		return socket;
	}
	
	public int getHostListSize()
	{
		return hostList.size();
	}
	
	public synchronized String getHostSummary(int index)
	{
		return new String(hostList.get(index).getIPAddress() + " " + getHostServerStatus(index) + " "
				+ getHostStatus(index));
	}
	
	/** 
	 * get the server status of the host
	 * @param index
	 * @return the String "server" if the host is the server, "client" otherwise
	 */
	public synchronized String getHostServerStatus(int index)
	{
		return hostList.get(index).getServerStatus() ? "Server" : "Client";
	}
	
	/**
	 * get the host active status 
	 * @param index
	 * @return the String "Active" if the host is active, "Inactive" otherwise
	 */
	public synchronized String getHostStatus(int index)
	{
		return hostList.get(index).getStatus() ? "Active" : "Inactive";
	}
	
	public synchronized void displayList() {
		for (int index = 0; index < hostList.size(); index++)
		{
			System.out.println(getHostSummary(index));
		}
	}
	
}
