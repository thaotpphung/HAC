package cs.copy;

public class Host
{
	private String IP;
	private long timeStamp;
	private boolean isActive;
	private int idNumber;
	private boolean isServer;
	private int flag;
	private double version;
	private String reserved;
	
	public Host(String IP, long timeStamp, int id)
	{
		this.IP = IP;
		this.timeStamp = timeStamp;
		this.idNumber = id;
		isActive = false;
		isServer = false;
		
		this.flag = 1; // 1 is server-client mode
		this.version = 1.0;
		this.reserved = "";
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
	
	public boolean getActiveStatus()
	{
		return isActive;
	}
	
	public void updateActiveStatus(boolean status)
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
