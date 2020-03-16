package cs.copy;

import java.net.*;
import java.util.ArrayList;

public class Host
{
	private ArrayList<HostInfo> hostList;
	private DatagramSocket socket;
	
	public Host()
	{
		try
		{
			socket = new DatagramSocket(9876);
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		hostList = new ArrayList<HostInfo>();
	}
	
	public void addHost(HostInfo newInfo)
	{
		hostList.add(newInfo);
	}
	
	public int searchHostbyIP(String IP)
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
	
	public HostInfo getHostInfo(int index)
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
				return hostList.get(index).getIPAddress();
			}
		}
		
		// if no one is the server, return the smallest active host
		return getIPAddressByID(getMinID());
	}
	
	public int getMinID()
	{
		int minValue = hostList.size();
		
		for (int index = 0; index < hostList.size(); index++)
		{
			if (hostList.get(index).getStatus())
			{
				if (hostList.get(index).getID() < minValue)
				{
					minValue = hostList.get(index).getID();
				}
			}
		}
		
		return minValue;
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
	
	public String getHostInfoSummary(int index)
	{
		return new String(hostList.get(index).getIPAddress() + " " + getHostServerStatus(index) + " "
				+ getHostStatus(index));
	}
	
	public String getHostServerStatus(int index)
	{
		String result;
		
		if (hostList.get(index).getServerStatus())
		{
			result = "Server";
		}
		else
		{
			result = "Client";
		}
		
		return result;
	}
	
	public String getHostStatus(int index)
	{
		String result;
		
		if (hostList.get(index).getStatus())
		{
			result = "Active";
		}
		else
		{
			result = "Inactive";
		}
		
		return result;
	}
}
