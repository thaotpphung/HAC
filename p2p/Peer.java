package p2p;

/**
* This class represents a Peer, including its relevant information and operations
* @version 3/16/2020
*/
public class Peer {
	private String IP;
	private long timeStamp;
	private boolean isActive;
	
	/**
	 * constructor for Peer
	 * @param IP IP address of this peer
	 * @param timeStamp the time when the peer's iP was received
	 */
	public Peer(String IP, long timeStamp)
	{
		this.IP = IP;
		this.timeStamp = timeStamp;
		isActive = false;
	}

	/**
	 * get the IP address of this peer
	 * @return the IP address of this peer
	 */
	public synchronized String getIPAddress()
	{
		return IP;
	}
	
	/**
	 * update the time stamp for this peer
	 * @param timeStamp the new time stamp of this peer
	 */
	public synchronized void updateTimeStamp(long timeStamp)
	{
		this.timeStamp = timeStamp;
	}
	
	/**
	 * get the time stamp of this peer
	 * @return the time stamp of this peer
	 */
	public synchronized long getTimeStamp()
	{
		return timeStamp;
	}
	
	/**
	 * get the active status of this peer
	 * @return true if the peer is active, false otherwise
	 */
	public synchronized boolean getStatus()
	{
		return isActive;
	}
	
	/**
	 * update the active status for this peer
	 * @param status the new status of this peer
	 */
	public synchronized void updateStatus(boolean status)
	{
		isActive = status;
	}
	
	/**
	 * @return the string that has the peer IP address and its active status
	 */
	public synchronized String toString()
	{
		return new String("Peer " + IP +  " " + (isActive ? "Active" : "Inactive"));
	}
}