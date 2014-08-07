package ranking;

/**
 * アイテムを表すクラスです。
 */
public class ItemInfo {
    
    /** アイテム名です */
    private String itemName;

    /**
     * 現在のプレイ時間上位者100人が購入した
     * 標準売価上位100件のアイテムの合計数
     */
    private long count;
    
    /** 合計数の順位 **/
    private long rank;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getRank() {
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemInfo itemInfo = (ItemInfo) o;

        if (count != itemInfo.count) return false;
        if (rank != itemInfo.rank) return false;
        if (itemName != null ? !itemName.equals(itemInfo.itemName) : itemInfo.itemName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemName != null ? itemName.hashCode() : 0;
        result = 31 * result + (int) (count ^ (count >>> 32));
        result = 31 * result + (int) (rank ^ (rank >>> 32));
        return result;
    }
}
