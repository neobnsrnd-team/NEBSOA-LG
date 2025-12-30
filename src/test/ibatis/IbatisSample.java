package test.ibatis;

public class IbatisSample {

	private String userId;
	private String userName;
	private String descr;
	private Float count;
	private String loc;
	
	public Float getCount() {
		return count;
	}
	public void setCount(Float count) {
		this.count = count;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
