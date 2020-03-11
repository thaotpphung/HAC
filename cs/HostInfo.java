package cs;

public class HostInfo
{
	private String IP;
	private long timeStamp;
	private boolean isActive;
	private int idNumber;
	private boolean isServer;
	
	public HostInfo(String IP, long timeStamp, int id)
	{
		this.IP = IP;
		this.timeStamp = timeStamp;
		this.idNumber = id;
		isActive = true;
		isServer = false;
	}
	
	public String getIPAddress()
	{
		return IP;
	}
	
	public long getTimeStamp()
	{
		return timeStamp;
	}
	
	public void updateTimeStamp(long timeStamp)
	{
		this.timeStamp = timeStamp;
	}
	
	public boolean getStatus()
	{
		return isActive;
	}
	
	public void updateStatus(boolean status)
	{
		isActive = status;
	}
	
	public int getID()
	{
		return idNumber;
	}
	
	public boolean getServerStatus()
	{
		return isServer;
	}
	
	public void updateServerStatus(boolean status)
	{
		isServer = status;
	}
}
