package ranking;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RankingCreatorの抽象クラスです。
 * Connection取得のためのメソッドを実装しています。
 */
public abstract class AbstractRankingCreator<T> {
    
    /** DBのURLです。 */
    public static final String DB_URL =
            "jdbc:oracle:thin:@10.15.213.93:1521:XE";
    
    /** DB接続ユーザー名とパスワードです。 */
    public static final String DB_MEMBER_USERNAME = "member";
    public static final String DB_MEMBER_PASSWORD = "member";
    public static final String DB_MEMBER2_USERNAME = "member2";
    public static final String DB_MEMBER2_PASSWORD = "member2";
    public static final String DB_ITEM_USERNAME = "item";
    public static final String DB_ITEM_PASSWORD = "item";
    public static final String DB_PURCHASE_USERNAME = "purchase";
    public static final String DB_PURCHASE_PASSWORD = "purchase";
    public static final String DB_PURCHASE2_USERNAME = "purchase2";
    public static final String DB_PURCHASE2_PASSWORD = "purchase2";

    /**
     * DBへ接続し、ソートされたアイテムのListを返すメソッドです。
     * 
     * @return アイテムのList
     * @throws SQLException
     */
    public final List<ItemInfo> execute() throws Exception {
        // Connectionを保持するためのオブジェクトを取得します。
        T connectionPool = createConnectionPool();
        
        List<ItemInfo> itemList;
        try {
            long start = System.nanoTime();
            itemList = doExecute(connectionPool);
            
            // doExecute実行時間のログ出力
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.INFO,
                    String.valueOf((System.nanoTime() - start) * 0.000001)
                            + " sec");
        } finally {
            closeConnection(connectionPool);
        }
        return itemList;
    }
    
    /**
     * 順位順でソートされたアイテムのListを返すメソッドです。
     * 採点時はこのメソッドの実装をテストします。
     * 実装はRankingCreatorクラスで行ってください。
     * 
     * @return アイテムのList
     */
    protected abstract List<ItemInfo> doExecute(T connectionPool) throws Exception;

    /**
     * DBへの接続を閉じる処理を行うためのメソッドです。
     *
     * @param connectionPool コネクションプール
     */
    protected abstract void closeConnection(T connectionPool) throws SQLException;

    /**
     * DBへの接続を保持するオブジェクトを返すメソッドです。
     * 
     * @return T DBへの接続を保持するオブジェクト
     */
    protected abstract T createConnectionPool();
}
