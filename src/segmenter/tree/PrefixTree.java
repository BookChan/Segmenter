package segmenter.tree;

import segmenter.Segmenter;

public class PrefixTree {
	public PrefixTreeNode root;
	public int MAX_DEPTH;
	
	public PrefixTree(int depth) {
		this.MAX_DEPTH = depth;
		this.root = new PrefixTreeNode("ROOT", null);
	}
	
	public void compile(String sequence) {
		if (sequence == null || sequence.length() == 0) {
			return;
		}
		
		for (int i = 0; i < sequence.length(); i++) {
			String subSequence = sequence.substring(i);
			PrefixTreeNode node = this.root;
			for (int j = 0; j < subSequence.length() && j < this.MAX_DEPTH + 1; j++) {
				String word = subSequence.substring(j, j + 1);
				node = node.addWord(word);
			}
		}
	}
	
	public PrefixTreeNode search(String sequence) {
		if (sequence == null || sequence.length() == 0) {
			return null;
		}
		
		if (sequence.length() > this.MAX_DEPTH) {
			return null;
		} else {
			PrefixTreeNode node = this.root;
			for (int i = 0; i < sequence.length(); i++) {
				if (node.children != null) {
					node = node.children.get(sequence.substring(i, i + 1));
				}
				
				if (node == null) {
					return null;
				}
			}
			
			return node;
		}
	}
	
	public void traversal(String prefix, PrefixTreeNode node) {
		if (node.children != null) {
			for (String key : node.children.keySet()) {
				PrefixTreeNode subNode = node.children.get(key);
				System.out.println(prefix + subNode.word + " : " + subNode.count);
				
				traversal(prefix + subNode.word, subNode);
			}
		}
	}
	
	public static void main(String[] args) {
		PrefixTree p = new PrefixTree(4);
		p.compile("12345678");
		p.compile("BBC");
		p.traversal("", p.root);
	}
}
