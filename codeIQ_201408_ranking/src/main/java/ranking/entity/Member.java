package ranking.entity;

public class Member implements Comparable<Member> {

	private int memberNo;
	private int playTime;
	
	public Member() { }

	public Member(int memberNo, int playTime) {
		this.memberNo = memberNo;
		this.playTime = playTime;
	}
	
	public int getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}
	public int getPlayTime() {
		return playTime;
	}
	public void setPlayTime(int playTime) {
		this.playTime = playTime;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + memberNo;
		result = prime * result + playTime;
		return result;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Member [memberNo=" + memberNo + ", playTime=" + playTime + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Member))
			return false;
		Member other = (Member) obj;
		if (memberNo != other.memberNo)
			return false;
		if (playTime != other.playTime)
			return false;
		return true;
	}
	@Override
	public int compareTo(Member o) {
		return getPlayTime() < o.getPlayTime() ? 1 :  getPlayTime() > o.getPlayTime() ? -1 : 0;
	}
	
	
}
