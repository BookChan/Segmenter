package segmenter.tree;

import java.util.HashMap;

public class PrefixTreeNode {
	//!!
	public static long sss = 0;
	
	public String word;
	public PrefixTreeNode parent;
	public HashMap<String, PrefixTreeNode> children;
	
	// compile完成后不变，作为FM,HS,HM的依据
	public int count;
	
	public int sum;
	
	public PrefixTreeNode(String word, PrefixTreeNode parent) {
		this.word = word;
		this.count = 0;
		this.sum = 0;
		
		this.children = null;
		this.parent = parent;
	}
	
	public PrefixTreeNode addWord(String word) {
		if (this.children == null) {
			this.children = new HashMap<String, PrefixTreeNode>();
			
			//!!
			System.out.println(++sss);
		}
		
		PrefixTreeNode node = this.children.get(word);
		if (node == null) {
			node = new PrefixTreeNode(word, this);
			this.children.put(word, node);
		}
		
		node.count += 1;
		
		this.sum += 1;
		
		return node;
	}
}
