package ranking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

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
        // このメソッドを実装してください
        throw new UnsupportedOperationException(
                "メソッドが実装されていません。テスト対象のメソッドを実装してください");
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
}