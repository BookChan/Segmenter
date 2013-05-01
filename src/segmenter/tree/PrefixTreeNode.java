package segmenter.tree;

import java.util.HashMap;
import java.util.HashSet;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class PrefixTreeNode {
	//!!
	public static long sss = 0;
	
	@PrimaryKey
	private String word;
	
	public String parent;
	public HashSet<String> children;
	
	// compile完成后不变，作为FM,HS,HM的依据
	public int count;
	
	public int sum;
	
	// entorpy
	public double entorpy = 0;
	
	// depth of the tree node
	public int depth;
	
	public PrefixTreeNode(String word, String parent, int depth) {
		this.word = word;
		this.count = 0;
		this.sum = 0;
		this.depth = depth;
		
		this.children = null;
		this.parent = parent;

		//!!
		sss++;
	}
	
	public PrefixTreeNode addWord(String word) {
		if (this.children == null) {
			this.children = new HashSet<String>();
		}
		
		this.children.add(word);
		PrefixTreeNode node = this.children.get(word);
		if (node == null) {
			node = new PrefixTreeNode(word, this, depth + 1);
			this.children.put(word, node);
		}
		
		node.count += 1;
		
		this.sum += 1;
		
		return node;
	}
}
