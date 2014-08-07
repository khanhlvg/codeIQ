package ranking;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * RankingCreatorのテストクラスです。
 * 回答確認と実行時間確認のために利用します。
 */
public class RankingCreatorTest {

    public static final String COLUMN_ITEM_COUNT = "item_count";
    public static final String COLUMN_ITEM_NAME = "item_nm";

    /** テスト対象クラスです。*/
    public RankingCreator rankingCreator;

    public final String RESOURCE_PATH = getClass().getResource(".").getPath();
    
    public File selectSql = new File(RESOURCE_PATH, "selectItem.sql");

    @Before
    public void setUp() throws Exception {
        // RarnkingCreatorのセットアップをします。
        rankingCreator = new RankingCreator();
    }

    /**
     * 答えが正しいか確認するためのテストケースです。
     * 評価はこのテストを実行して行います。
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testContentsOfList() throws Exception {
        // 期待される回答の取得
        Connection con = rankingCreator.getConnection(
                AbstractRankingCreator.DB_ITEM_USERNAME,
                AbstractRankingCreator.DB_ITEM_PASSWORD);
        PreparedStatement stmt = con.prepareStatement(getSql(selectSql));
        ResultSet rs = stmt.executeQuery();
        List<ItemInfo> expectedItems = new ArrayList<ItemInfo>();
        while(rs.next()) {
            ItemInfo itemInfo = new ItemInfo();
            itemInfo.setCount(rs.getInt(COLUMN_ITEM_COUNT));
            itemInfo.setItemName(rs.getString(COLUMN_ITEM_NAME));
            itemInfo.setRank(rs.getRow());
            expectedItems.add(itemInfo);
        }
        
        // テスト対象メソッド実行
        List<ItemInfo> items = rankingCreator.execute();
        
        assertThat(items, is(expectedItems));
    }

    /**
     * このケースは回答の検証には使用しません。
     * 
     * @throws Exception
     */
    @Test
    public void testGetConnection() throws Exception {
        RankingCreator rankingCreator = new RankingCreator();
        Connection con = rankingCreator.getConnection(
                AbstractRankingCreator.DB_ITEM_USERNAME,
                AbstractRankingCreator.DB_ITEM_PASSWORD);

        assertThat("Connectionが取得できていること",
                con, is(any(Connection.class)));
    }
    
    private String getSql(File sqlFile) throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(selectSql));
        StringBuilder str = new StringBuilder();
        String line = r.readLine();
        while (line != null) {
            str.append(line);
            line = r.readLine();
        }
        return str.toString();
    }
}
