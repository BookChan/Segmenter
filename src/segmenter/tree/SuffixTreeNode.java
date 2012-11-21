package segmenter.tree;

import java.util.HashMap;

public class SuffixTreeNode {
	public String word;
	public SuffixTreeNode parent;
	public HashMap<String, SuffixTreeNode> children;
	
	// compile完成后不变，作为FM,HS,HM的依据
	public int count;
	
	public int sum;
	
	public SuffixTreeNode(String word, SuffixTreeNode parent) {
		this.word = word;
		this.parent = parent;
		this.children = null;
		this.sum = 0;
	}
	
	public SuffixTreeNode addWord(String word) {
		if (this.children == null) {
			this.children = new HashMap<String, SuffixTreeNode>();
		}
		
		SuffixTreeNode node = this.children.get(word);
		if (node == null) {
			node = new SuffixTreeNode(word, this);
			this.children.put(word, node);
		}
		
		node.count += 1;
		this.sum += 1;
		
		return node;
	}
}
