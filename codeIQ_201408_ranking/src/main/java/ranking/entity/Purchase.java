package ranking.entity;

import java.util.ArrayList;
import java.util.List;

public class Purchase {

	private int purchaseNo;
	private int memberNo;
	private List<Integer> itemNoList;
	
	public Purchase() {
		this.setItemNoList(new ArrayList<Integer>());
	}

	/**
	 * @return the purchaseNo
	 */
	public int getPurchaseNo() {
		return purchaseNo;
	}

	/**
	 * @param purchaseNo the purchaseNo to set
	 */
	public void setPurchaseNo(int purchaseNo) {
		this.purchaseNo = purchaseNo;
	}

	/**
	 * @return the memberNo
	 */
	public int getMemberNo() {
		return memberNo;
	}

	/**
	 * @param memberNo the memberNo to set
	 */
	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PurchaseBase [purchaseNo=" + purchaseNo + ", memberNo="
				+ memberNo + "]";
	}

	public List<Integer> getItemNoList() {
		return itemNoList;
	}

	private void setItemNoList(List<Integer> itemNoList) {
		this.itemNoList = itemNoList;
	}
	
	
	
}
