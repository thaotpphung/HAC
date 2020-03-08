package p2p;

public class PeerInfo {
	private String IP;
	private long timeStamp;
	private boolean isActive;
	
	public PeerInfo(String IP, long timeStamp)
	{
		this.IP = IP;
		this.timeStamp = timeStamp;
		isActive = true;
	}

	public String getIPAddress()
	{
		return IP;
	}
	
	public void updateTimeStamp(long timeStamp)
	{
		this.timeStamp = timeStamp;
	}
	
	public long getTimeStamp()
	{
		return timeStamp;
	}
	
	public boolean getStatus()
	{
		return isActive;
	}
	
	public void updateStatus(boolean status)
	{
		isActive = status;
	}
	
	public String toString()
	{
		return new String(IP + " " + isActive);
	}
}
