package segmenter.db;

import java.io.File;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;


/*
 * Berkeley DB
 */

public class DBTools {
    private static Environment treeEnv;
    private static Environment memEnv;
    
    private static EntityStore prefixStore;
    private static EntityStore suffixStore;
    private static EntityStore memStore;
//    private static EntityStore tempStore;
    
    private static PrimaryIndex<String, PrefixTreeNode> prefixNodeByString;
    private static PrimaryIndex<String, SuffixTreeNode> suffixNodeByString;
    private static PrimaryIndex<String, MemData> memReader;
    private static PrimaryIndex<String, MemHSP1Data> memHReader;
//    private static PrimaryIndex<Integer, String> sentReader;
    
    private static final String dbPath = "./database/";
    private static final String treePath = "tree";
    private static final String memDataPath = "mem";
	
	static {
		File dir = new File(dbPath);
		if (!dir.isDirectory()) {
			dir.mkdir();
		}
		
		File tDir = new File(dbPath + treePath);
		if (!tDir.isDirectory()) {
			tDir.mkdir();
		}
		
		File mDir = new File(dbPath + memDataPath);
		if (!mDir.isDirectory()) {
			mDir.mkdir();
		}
		
		openDB(false);
	}
	
	/*
	 * boolean readOnly
	 */
	public static void openDB(boolean readOnly) {
		try {
			closeDB();
		} catch (Exception e) {
			// ignore
			
			//!!!!!!!!!!
			e.printStackTrace();
		}
		
		EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setSharedCache(true);
        envConfig.setAllowCreate(true);
        
        treeEnv = new Environment(new File("./database/tree"), envConfig);
        memEnv = new Environment(new File("./database/mem"), envConfig);

        StoreConfig storeConfig = new StoreConfig();
        storeConfig.setAllowCreate(true);
        if (readOnly) {
        	storeConfig.setReadOnly(true);
        } 
        
        prefixStore = new EntityStore(treeEnv, "PrefixTree", storeConfig);
        suffixStore = new EntityStore(treeEnv, "SuffixTree", storeConfig);
        memStore = new EntityStore(memEnv, "Mem", storeConfig);
        
        prefixNodeByString = prefixStore.getPrimaryIndex(String.class, PrefixTreeNode.class);
        suffixNodeByString = suffixStore.getPrimaryIndex(String.class, SuffixTreeNode.class);
        memReader = memStore.getPrimaryIndex(String.class, MemData.class);
        memHReader = memStore.getPrimaryIndex(String.class, MemHSP1Data.class);
	}
	
	public static void closeDB() {
		if (prefixStore != null) {
			prefixStore.close();
			suffixStore.close();
			memStore.close();
			treeEnv.close();
		}
	}
	
	public static void saveMemData(MemData data, MemHSP1Data hData) {
		memReader.put(data);
		memHReader.put(hData);
	}
	
	public static MemData getMemData() {
		return memReader.get("MEM");
	}
	
	public static MemHSP1Data getMemHSPData() {
		return memHReader.get("MemHSP1");
	}
	
	public static PrefixTreeNode getPrefixTreeNode(String prefix) {
		return prefixNodeByString.get(prefix);
	}
	
	public static void putPrefixTreeNode(PrefixTreeNode node) {
		prefixNodeByString.put(node);
	}
	
	public static SuffixTreeNode getSuffixTreeNode(String suffix) {
		return suffixNodeByString.get(suffix);
	}
	
	public static void putSuffixTreeNode(SuffixTreeNode node) {
		suffixNodeByString.put(node);
	}
}
