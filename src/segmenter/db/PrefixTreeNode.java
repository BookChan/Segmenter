package segmenter.db;

import java.util.HashSet;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class PrefixTreeNode {
	@PrimaryKey
    private String pKey;
	
	private int freq;
	
	private HashSet<String> children;

	public void setKey(String key) {
		this.pKey = key;
	}
	
	public String getKey() {
		return this.pKey;
	}
	
	public void setFreq(int freq) {
		this.freq = freq;
	}
	
	public int getFreq() {
		return this.freq;
	}
	
	public int addFreq(int i) {
		freq += i;
		return freq;
	}
	
	public void addChild(String key) {
		if (this.children == null) {
			this.children = new HashSet<String>();
		}
		
		this.children.add(key);
	}
	
	public HashSet<String> getChildren() {
		return this.children;
	}
	
	private PrefixTreeNode() {
		
	}
	
	public PrefixTreeNode(String key) {
		this.pKey = key;
		this.freq = 0;
	}
}
