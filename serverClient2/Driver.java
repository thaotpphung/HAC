package serverClient2;

import java.util.ArrayList;

import serverClient.HostInfo;

public class Driver {

	public static void main(String[] args) {
		
		Host me = new Host("192.168.1.112", System.currentTimeMillis()); // me
		Host host1 = new Host("192.168.1.109", System.currentTimeMillis()); 
		
		ArrayList<Host> aList = new ArrayList<Host>();	
		aList.add(me);
		aList.add(host1);
		
		ListOfHost list = new ListOfHost(aList);
		
		// listen for any IP address for 30s
		int received = me.intialListen(list);
		
		if (received == 0)  // server is dead
		{
			String smallest = list.getIp(0);
			for (int i = 1; i < list.getListSize(); i++)
			{
				if(list.getStatus(i))
				{
					if (list.getIp(i).compareTo(smallest) < 0) {
						smallest = list.getIp(i);
					}
				}
			}
			
			list.setServer(list.getHostByIp(smallest));
			
			if (me.isServer())
			{
				me.server(list);
			} else { // someone else claims to be the server
				// keep sending my Ip to server
				me.client();
			}
		} else { // server is not dead
			// keep sending my Ip to server
			me.client();
		}
	}

}
