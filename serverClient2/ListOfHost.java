package serverClient2;

import java.net.DatagramSocket;
import java.util.ArrayList;


public class ListOfHost {
	private ArrayList<Host> list;
	
	public ListOfHost (ArrayList<Host> list)
	{
		this.list = list;
	}
	
	public synchronized void addHost(Host host)
	{
		list.add(host);
	}
	
	public Host getHost(int index)
	{
		return list.get(index);
	}
	
	public void updateStatus(int index, boolean status)
	{
		list.get(index).updateStatus(status);
	}
	
	public void updateTimestamp(int index, long time)
	{
		list.get(index).updateTimeStamp(time);
	}
	
	public int getHostByIp(String IP)
	{
		int result = -1;
		
		for (int index = 0; index < list.size(); index++)
		{
			if (list.get(index).getIPAddress().equals(IP))
			{
				return index;
			}
		}
		
		return result;
	}
	
	public int getListSize()
	{
		return list.size();
	}
	
	public boolean getStatus(int index)
	{
		return list.get(index).getStatus();
	}
	
	public String getIp (int index)
	{
		return list.get(index).getIp();
	}

	public void setServer(int index) {
		list.get(index).setServer(true);
		
	}
	
	

}
