package p2pt;

public class PeerInfo {
	private String ip;
	private long lastReceived;

	public PeerInfo(String ip, long lastReceived) {
		this.ip = ip;
		this.lastReceived = lastReceived;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getlastReceived() {
		return lastReceived;
	}

	public void setlastReceived(long lastReceived) {
		this.lastReceived = lastReceived;
	}
	
	public PeerInfo getPeerInfo(String ip) {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PeerInfo) {
			return ((PeerInfo) obj).ip == ip;
		}
		return false;
	}

}
