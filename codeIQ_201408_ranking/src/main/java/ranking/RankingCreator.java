package ranking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import ranking.entity.*;
import ranking.util.MapUtil;

/**
 * Processing flows:
 * (done) 1. Load all MEMBER
 * (done) 2. get 100 members with longest play time
 * (done) 3. Load all ITEM
 * (done) 4. get 100 items with highest price
 * 5. get PURCHASE_NO of those 100 members from PURCHASE table
 * 6. get list of row in PURCHASE_DTL where PURCHASE_NO in above list and ITEM_NO is in top 100 items
 * 7. build of collection of ItemInfo from the list above and sort 
 */

/**
 * 回答クラスです。doExecuteを実装してください。
 * 必要に応じてその他のメソッド等を修正しても構いません。
 * 
 * @see AbstractRankingCreator
 */
public class RankingCreator
        extends AbstractRankingCreator<HashMap<String, Connection>> {

    @Override
    protected List<ItemInfo> doExecute(HashMap<String, Connection> connectionPool)
            throws Exception {
        
    	List<Member> top100MemberList = getTop100MemberList();
        List<Item> top100ItemList = getTop100ItemList();
        
        Map<Integer,Integer> purchaseCountMap = new HashMap<Integer,Integer>();
        
        purchaseCountMap = getCountOfTop100ItemAndTop100Member(
        		purchaseCountMap,
        		top100MemberList, top100ItemList, 
        		AbstractRankingCreator.DB_PURCHASE_USERNAME, 
        		AbstractRankingCreator.DB_PURCHASE_PASSWORD );
        
        purchaseCountMap = getCountOfTop100ItemAndTop100Member(
        		purchaseCountMap,
        		top100MemberList, top100ItemList, 
        		AbstractRankingCreator.DB_PURCHASE2_USERNAME, 
        		AbstractRankingCreator.DB_PURCHASE2_PASSWORD );
        
        List<ItemInfo> ret = buildItemInfoList(top100ItemList, purchaseCountMap);
        
        return ret;    	
    }

    @Override
    protected void closeConnection(HashMap<String, Connection> connectionPool)
            throws SQLException {
        //コネクションプールの作成方法を変更したい人は変更可能です。
        for (Connection con : connectionPool.values()) {
            con.close();
        }
    }

    @Override
    protected HashMap<String, Connection> createConnectionPool() {
        //コネクションプールの作成方法を変更したい人は変更可能です。
        HashMap<String, Connection> connectionPool =
                new HashMap<String, Connection>();
        try {
            connectionPool.put("member",
                    getConnection(DB_MEMBER_USERNAME, DB_MEMBER_PASSWORD));
            connectionPool.put("member2",
                    getConnection(DB_MEMBER2_USERNAME, DB_MEMBER2_PASSWORD));
            connectionPool.put("item",
                    getConnection(DB_ITEM_USERNAME, DB_ITEM_PASSWORD));
            connectionPool.put("purchase",
                    getConnection(DB_PURCHASE_USERNAME, DB_PURCHASE_PASSWORD));
            connectionPool.put("purchase2",
                    getConnection(DB_PURCHASE2_USERNAME,
                            DB_PURCHASE2_PASSWORD));
        } catch (SQLException e) {
            throw new RuntimeException("DBへの接続に失敗しました。", e);
        }
        
        return connectionPool;
    }

    /**
     * DBへのConnectionを返すメソッドです。
     *
     * @param username
     * @return DBへのConnection
     */
    protected final Connection getConnection(String username, String password)
            throws SQLException {
              //コネクションプールの作成方法を変更したい人は変更可能です。
        return DriverManager.getConnection(DB_URL, username, password);
    }
    
    // constant definitions
    
    static int MAX_FETCH_SIZE = 10000;
    
    static int MAX_MEMBER_SIZE = 100;
    static int MAX_ITEM_SIZE = 100;
    
    static final String CLM_MEMBER_NO = "MEMBER_NO";
    static final String CLM_PLAY_TIME = "PLAY_TIME";
    
    static final String CLM_ITEM_NO = "ITEM_NO";
    static final String CLM_ITEM_NM = "ITEM_NM";
    static final String CLM_STANDARD_PRICE = "STANDARD_PRICE";
    
    static final String CLM_PURCHASE_NO = "PURCHASE_NO";
    static final String CLM_BRANCH_NO = "BRANCH_NO";
    
    
   
    // data processing methods
    public List<Member> getTop100MemberList() throws Exception {
    	ArrayList<Member> allMemberList = new ArrayList<Member>();
    	
    	//connect to server and get MEMBER
    	Connection con = getConnection(
                AbstractRankingCreator.DB_MEMBER_USERNAME,
                AbstractRankingCreator.DB_MEMBER_PASSWORD);
        PreparedStatement stmt = con.prepareStatement("select * from member");
        ResultSet rs = stmt.executeQuery();
        rs.setFetchSize(MAX_FETCH_SIZE);
        
        long start = System.currentTimeMillis();
        
        //long cucum = 0;
        
        while(rs.next()) {
        	//long startSmall = System.currentTimeMillis();
            Member member = new Member();
            member.setMemberNo(rs.getInt(CLM_MEMBER_NO));
            member.setPlayTime(rs.getInt(CLM_PLAY_TIME));
            allMemberList.add(member);
            //long endSmall = System.currentTimeMillis();
            //cucum+= (endSmall - startSmall);
            
            //TODO: upgrade to add only member which is better than minimum
        }
        
        //System.out.println("small time = " + cucum);
        
        //connect to server and get MEMBER2
    	con = getConnection(
                AbstractRankingCreator.DB_MEMBER2_USERNAME,
                AbstractRankingCreator.DB_MEMBER2_PASSWORD);
        stmt = con.prepareStatement("select * from member");
        rs = stmt.executeQuery();
        rs.setFetchSize(MAX_FETCH_SIZE);
        
        while(rs.next()) {
            Member member = new Member();
            member.setMemberNo(rs.getInt(CLM_MEMBER_NO));
            member.setPlayTime(rs.getInt(CLM_PLAY_TIME));
            allMemberList.add(member);
            
            //TODO: upgrade to add only member which is better than minimum
        }
    	
        Collections.sort(allMemberList);
        
        int finalIndex = allMemberList.size() < MAX_MEMBER_SIZE ? allMemberList.size() : MAX_MEMBER_SIZE;
        
    	List<Member> sortedMemberList = allMemberList.subList(0, finalIndex);
    	
    	long end = System.currentTimeMillis();
        System.out.println("total time for MEMBER = " + (end - start));
    	
    	return sortedMemberList;
    }
    
    public List<Item> getTop100ItemList() throws Exception {
    	ArrayList<Item> allItemList = new ArrayList<Item>();
    	
    	//connect to server and get MEMBER
    	Connection con = getConnection(
                AbstractRankingCreator.DB_ITEM_USERNAME,
                AbstractRankingCreator.DB_ITEM_PASSWORD);
        PreparedStatement stmt = con.prepareStatement("select * from item");
        ResultSet rs = stmt.executeQuery();
        rs.setFetchSize(MAX_FETCH_SIZE);
        
        long start = System.currentTimeMillis();
        
        //long cucum = 0;
        
        while(rs.next()) {
        	//long startSmall = System.currentTimeMillis();
            Item item = new Item();
            item.setItemNo(rs.getInt(CLM_ITEM_NO));
            item.setItemName(rs.getString(CLM_ITEM_NM));
            item.setStandardPrice(rs.getInt(CLM_STANDARD_PRICE));
            allItemList.add(item);
            //long endSmall = System.currentTimeMillis();
            //cucum+= (endSmall - startSmall);
            
            //TODO: upgrade to add only item which is better than minimum
        }
        
        //System.out.println("small time = " + cucum);
    	
        Collections.sort(allItemList);
        
        int finalIndex = allItemList.size() < MAX_ITEM_SIZE ? allItemList.size() : MAX_ITEM_SIZE;
        
    	List<Item> sortedItemList = allItemList.subList(0, finalIndex);
    	
    	long end = System.currentTimeMillis();
        System.out.println("total time for ITEM = " + (end - start));
    	
    	return sortedItemList;
    }
    
    public Map<Integer,Integer> getCountOfTop100ItemAndTop100Member(
    		Map<Integer,Integer> countMap,
    		List<Member> top100MemberList,
    		List<Item> top100ItemList,
    		String dbPurchaseUsername, String dbPurchasePassword) throws Exception {
    	HashMap<Integer,Purchase> allPurchaseMap = new HashMap<Integer,Purchase>();
    	
    	// build select SQL
    	StringBuilder sqlSelectPurchase = new StringBuilder("select * from purchase where ");
    	sqlSelectPurchase.append(CLM_MEMBER_NO).append(" in (");
    	
    	for (Member member : top100MemberList) {
    		sqlSelectPurchase.append(member.getMemberNo()).append(",");
    	}
    	
    	//remove last comma
    	sqlSelectPurchase.deleteCharAt(sqlSelectPurchase.length() - 1);
    	sqlSelectPurchase.append(")");
    	
    	System.out.println(" PURCHASE sql = " + sqlSelectPurchase.toString());
    	
    	//connect to server and get PURCHASE
    	Connection con = getConnection(dbPurchaseUsername,dbPurchasePassword);
        PreparedStatement stmt = con.prepareStatement(sqlSelectPurchase.toString());
        ResultSet rs = stmt.executeQuery();
        rs.setFetchSize(MAX_FETCH_SIZE);
        
        long start = System.currentTimeMillis();
        
        //long cucum = 0;
        
        while(rs.next()) {
        	//long startSmall = System.currentTimeMillis();
            Purchase purchase = new Purchase();
            purchase.setPurchaseNo(rs.getInt(CLM_PURCHASE_NO));
            purchase.setMemberNo(rs.getInt(CLM_MEMBER_NO));
            allPurchaseMap.put(purchase.getPurchaseNo(), purchase);
            //long endSmall = System.currentTimeMillis();
            //cucum+= (endSmall - startSmall);
            
        }
        
        //System.out.println("small time = " + cucum);
        
        HashMap<Integer,Integer> purchaseCountMap = new HashMap<Integer,Integer>(countMap);
        
        // build select SQL
    	StringBuilder sqlSelectPurchaseDtl = new StringBuilder("select * from purchase_dtl where ");
    	
    	// don't add purchase_no to query
    	/*
    	sqlSelectPurchaseDtl.append(CLM_PURCHASE_NO).append(" in (");
    	
    	for (Purchase purchase : allPurchaseMap.values()) {
    		sqlSelectPurchaseDtl.append(purchase.getPurchaseNo()).append(",");
    	}
        
    	sqlSelectPurchaseDtl.deleteCharAt(sqlSelectPurchaseDtl.length() - 1);
    	sqlSelectPurchaseDtl.append(") ");
    	sqlSelectPurchaseDtl.append(" and ");
    	*/
    	
    	sqlSelectPurchaseDtl.append(CLM_ITEM_NO).append(" in (");
    	
    	for (Item item : top100ItemList) {
    		sqlSelectPurchaseDtl.append(item.getItemNo()).append(",");
    	}
    	
    	sqlSelectPurchaseDtl.deleteCharAt(sqlSelectPurchaseDtl.length() - 1);
    	sqlSelectPurchaseDtl.append(")");
    	
    	System.out.println(" PURCHASE_DTL sql = " + sqlSelectPurchaseDtl.toString());
    	
    	//connect to server and get PURCHASE_DTL
    	con = getConnection(dbPurchaseUsername,dbPurchasePassword);
        stmt = con.prepareStatement(sqlSelectPurchaseDtl.toString());
        rs = stmt.executeQuery();
        rs.setFetchSize(MAX_FETCH_SIZE);
        
        //long cucum = 0;
        
        int count = 0;
        
        while(rs.next()) {
        	//long startSmall = System.currentTimeMillis();

        	Integer itemNo = rs.getInt(CLM_ITEM_NO);
        	Integer purchaseNo = rs.getInt(CLM_PURCHASE_NO);
        	
        	// skip this item if purchase not belong to top 100 member
        	if (!allPurchaseMap.keySet().contains(purchaseNo))
        		continue;
        	//TODO choose better Set type for faster search of purchase_no
        	
        	count++;
        	
			if (purchaseCountMap.containsKey(itemNo)) {
				purchaseCountMap.put(itemNo, purchaseCountMap.get(itemNo) + 1);
			} else {
				purchaseCountMap.put(itemNo, 1);
			}
            
            //long endSmall = System.currentTimeMillis();
            //cucum+= (endSmall - startSmall);
            
        }
    	
        System.out.println("Scanned over " + count + " purchases.");
    	
    	long end = System.currentTimeMillis();
        System.out.println("total time for PURCHASE = " + (end - start));
    	
    	return purchaseCountMap;
    }
    
    public List<ItemInfo> buildItemInfoList(List<Item> top100ItemList,
    		Map<Integer,Integer> itemCountMap) {
    	List<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();
    	
    	//create ItemInfo list
    	for (Item item : top100ItemList) {
    		ItemInfo itemInfo = new ItemInfo();
    		itemInfo.setItemName(item.getItemName());
    		itemInfo.setCount(itemCountMap.get(item.getItemNo()));
    		itemInfoList.add(itemInfo);
    	}
    	
    	//sort ItemInfo list
    	
    	Comparator<ItemInfo> itemInfoComparator = new Comparator<ItemInfo>() {

    		public int compare(ItemInfo s1, ItemInfo s2) {
    		   
    		   //ascending order
    		   return s1.getCount() < s2.getCount() ? 1 : s1.getCount() > s2.getCount() ? -1 : 0;

    		   //descending order
    		   //return StudentName2.compareTo(StudentName1);
    	    }};
    	
    	Collections.sort(itemInfoList, itemInfoComparator);
    	
    	//assign rank to ItemInfo list
    	int rank = 0;
    	int posInArray = 0;
    	long currentCount = itemInfoList.get(0).getCount() + 1;
    	for (ItemInfo itemInfo : itemInfoList) {
    		posInArray++;
    		if (itemInfo.getCount() < currentCount)
    			rank = posInArray;
    		currentCount = itemInfo.getCount();
    		itemInfo.setRank(rank);
    	}
    	
    	return itemInfoList;
    }
    
}