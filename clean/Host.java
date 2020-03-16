package cs.clean;

/**
* This class represents a Host, including its relevant information and operations
* @version 3/16/2020
*/

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
	
	/**
	 * constructor for HostInfo
	 * @param IP IP address of the host
	 * @param timeStamp the time when the host information was received or when it first become active
	 * @param id a number unique to each host
	 */
	public Host(String IP, long timeStamp, int id)
	{
		this.IP = IP;
		this.timeStamp = timeStamp;
		this.idNumber = id;
		isActive = false;
		isServer = false;
		
		this.flag = 1; 	// 1 is server-client mode
		this.version = 1.0; // the first version
		this.reserved = ""; // for future development
	}
	
	/**
	 * get the IP address of the host
	 * @return the IP address of the host
	 */
	public String getIPAddress()
	{
		return IP;
	}
	
	/**
	 * get the time stamp of the host
	 * @return the time stamp of the host
	 */
	public long getTimeStamp()
	{
		return timeStamp;
	}
	
	/**
	 * update the time stamp of the host
	 * @param timeStamp the new time stamp for the host
	 */
	public void updateTimeStamp(long timeStamp)
	{
		this.timeStamp = timeStamp;
	}
	
	/**
	 * get the status of the host, active or inactive
	 * @return true if host is active, false otherwise
	 */
	public boolean getActiveStatus()
	{
		return isActive;
	}
	
	/**
	 * update the status of the host
	 * @param status the new status of the host
	 */
	public void updateActiveStatus(boolean status)
	{
		isActive = status;
	}
	
	/**
	 * get the id of the host
	 * @return the id of the host
	 */
	public int getID()
	{
		return idNumber;
	}
	
	/**
	 * get the server status of the host
	 * @return true if the host is the server, false otherwise
	 */
	public boolean getServerStatus()
	{
		return isServer;
	}
	
	/**
	 * update the server status of the host
	 * @param status the new server status of the host
	 */
	public void updateServerStatus(boolean status)
	{
		isServer = status;
	}
}
