package p2plist;

public class PeerInfo {
	private String ip;
	private long lastReceived;
	private boolean status;

	public PeerInfo(String ip, long lastReceived, boolean status) {
		this.ip = ip;
		this.lastReceived = lastReceived;
		this.status = status;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getIp() {
		return ip;
	}

	public long getlastReceived() {
		return lastReceived;
	}
	
	public void setlastReceived(long newTime) {
		this.lastReceived = newTime;
	}

	public PeerInfo getPeerInfo(String ip) {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PeerInfo) {
			return ( ((PeerInfo) obj).ip == ip && ((PeerInfo) obj).lastReceived == lastReceived );
		}
		return false;
	}

}
