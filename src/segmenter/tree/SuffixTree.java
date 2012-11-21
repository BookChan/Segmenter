package segmenter.tree;

public class SuffixTree {
	public int MAX_DEPTH;
	public SuffixTreeNode root;
	
	public SuffixTree(int depth) {
		this.MAX_DEPTH = depth;
		this.root = new SuffixTreeNode("ROOT", null);
	}
	
	public void compile(String sequence) {
		if (sequence == null || sequence.length() == 0) {
			return;
		}
		
		for (int i = sequence.length(); i > 0; i--) {
			String subSequence = sequence.substring(0, i);
			SuffixTreeNode node = this.root;
			for (int j = subSequence.length(); j > 0 && subSequence.length() - j < this.MAX_DEPTH + 1; j--) {
				String word = subSequence.substring(j - 1, j);
				node = node.addWord(word);
			}
		}
	}
	
	public SuffixTreeNode search(String sequence) {
		if (sequence == null || sequence.length() == 0) {
			return null;
		}
		
		if (sequence.length() > this.MAX_DEPTH) {
			return null;
		} else {
			SuffixTreeNode node = this.root;
			for (int i = sequence.length(); i > 0; i--) {
				if (node.children != null) {
					node = node.children.get(sequence.substring(i - 1, i));
				}
				
				if (node == null) {
					return null;
				}
			}
			
			return node;
		}
	}
	
	public void traversal(String suffix, SuffixTreeNode node) {
		if (node.children != null) {
			for (String key : node.children.keySet()) {
				SuffixTreeNode subNode = node.children.get(key);
				System.out.println(subNode.word + suffix + " : " + subNode.count);
				
				traversal(subNode.word + suffix, subNode);
			}
		}
	}
	
	public static void main(String[] args) {
		SuffixTree p = new SuffixTree(4);
		p.compile("1234561");
		p.traversal("", p.root);
	}
}
