package serverClient2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class Host {
	String ip;
	private DatagramSocket socket = null;
	private long timeStamp;
	private boolean isServer;
	private boolean isActive;

	public Host(String ip, long timeStamp) {
		this.ip = ip;
		this.isActive = false;
		this.timeStamp = timeStamp;
		try {
			// create my socket
			this.socket = new DatagramSocket(9876);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public int intialListen(ListOfHost list) {
		int received = 0;
		try {
			byte[] incomingData = new byte[1024];

			long a = System.currentTimeMillis();

			int targetIndex;

			while (true) {
				long b = System.currentTimeMillis();
				if (b - a >= 30000) // listen for 30 seconds
					break;

				DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
				socket.receive(incomingPacket);
				String otherIp = new String(incomingPacket.getData());
				InetAddress IPAddress = incomingPacket.getAddress();
				int port = incomingPacket.getPort();

				// update the map with the correct time stamp and status
				targetIndex = list.getHostByIp(otherIp);
				list.updateTimestamp(targetIndex, System.currentTimeMillis());
				list.updateStatus(targetIndex, true);

				received++;

			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException i) {
			i.printStackTrace();
		}
		return received;
	}

	public void updateStatus(boolean status) {
		isActive = status;
	}

	public void updateTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getIPAddress() {
		return ip;
	}

	public boolean getStatus() {
		return this.isActive;
	}

	public String getIp() {
		return this.ip;
	}

	public void setServer(boolean isServer) {
		this.isServer = isServer;
	}

	public boolean isServer() {
		return this.isServer;
	}

	private class SendThread implements Runnable
	{
		private String anIp;
		private String destIp;

		public SendThread(String anIp, String destIp) {
			this.anIp = anIp;
			this.destIp = destIp;
		}

		public void run() {
			
		}

	}

	private class ReceiveThread implements Runnable {
		public ReceiveThread()
		{
			
		}

		public void run() {
			
		}
	}

	public void server(ListOfHost list) {
		while (true) {
			Thread receive = new Thread(new ReceiveThread());
			receive.start();
		}
	}

	public void client() {

	}

	public String findServer() {

		return null;
	}

}
