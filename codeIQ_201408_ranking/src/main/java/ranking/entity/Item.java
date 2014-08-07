package ranking.entity;

public class Item implements Comparable<Item> {

	private int itemNo;
	private String itemName;
	private int standardPrice;
	
	public Item() { }
	
	/**
	 * @return the itemNo
	 */
	public int getItemNo() {
		return itemNo;
	}
	/**
	 * @param itemNo the itemNo to set
	 */
	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}
	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/**
	 * @return the standardPrice
	 */
	public int getStandardPrice() {
		return standardPrice;
	}
	/**
	 * @param standardPrice the standardPrice to set
	 */
	public void setStandardPrice(int standardPrice) {
		this.standardPrice = standardPrice;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Item [itemNo=" + itemNo + ", itemName=" + itemName
				+ ", standardPrice=" + standardPrice + "]";
	}

	@Override
	public int compareTo(Item o) {
		// TODO Auto-generated method stub
		return getStandardPrice() < o.getStandardPrice() ? 1 :  getStandardPrice() > o.getStandardPrice() ? -1 : 0;
	}
	
	
	
}
