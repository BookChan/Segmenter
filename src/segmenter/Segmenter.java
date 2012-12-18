package segmenter;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import segmenter.tree.PrefixTree;
import segmenter.tree.PrefixTreeNode;
import segmenter.tree.SuffixTree;
import segmenter.tree.SuffixTreeNode;

public class Segmenter {
	public int MAX_LENGTH = 4;
	public Corpus corpus;
	public PrefixTree prefixTree;
	public SuffixTree suffixTree;
	
	public double[] FM;
	public int[] FM_COUNT;
	
	public double[] HRM;
	public int[] HRM_COUNT;
	
	public double[] HLM;
	public int[] HLM_COUNT;
	
	public HashMap<String, Integer> F;
	
	public Segmenter(Corpus corpus) {
		this.corpus = corpus;
		
		this.FM = new double[this.MAX_LENGTH];
		this.FM_COUNT = new int[this.MAX_LENGTH];
		this.HRM = new double[this.MAX_LENGTH];
		this.HRM_COUNT = new int[this.MAX_LENGTH];
		this.HLM = new double[this.MAX_LENGTH];
		this.HLM_COUNT = new int[this.MAX_LENGTH];
		
		this.prefixTree = new PrefixTree(this.MAX_LENGTH);
		this.suffixTree = new SuffixTree(this.MAX_LENGTH);
		
		this.F = new HashMap<String, Integer>();
	}
	
	public void preProcess() {
		System.out.println("Preprocessing...");
		
		//!! 统计字符数与前缀节点数的关系
		long sum = 0;
		int temp = 0;
		
		while (this.corpus.hasNext()) {
			String sequence = this.corpus.nextString();
			
			prefixTree.compile(sequence);
			suffixTree.compile(sequence);
			
			//!!
			sum += sequence.length();
			temp += sequence.length();
			if (temp >= 10000) {
				temp -= 10000;
				System.out.println(sum + "\t" + PrefixTreeNode.sss);
			}
		}
		this.corpus.reset();
		
		System.out.println("Creating tree: done!");
		update();
	}
	
	public void train(int ITER) {
		for (int iter = 1; iter <= ITER; iter++) {
			System.out.println("in " + iter + "th iter...");
			
			HashMap<String, Integer> newF = new HashMap<String, Integer>();
			
			//!!
			System.out.println("0%");
			
			while (this.corpus.hasNext()) {
				String sequence = this.corpus.nextString();
				List<String> list = evaluate(sequence, false);
				
				for (String seg : list) {
					Integer f = newF.get(seg);
					
					if (f == null) {
						f = 0;
					}
					
					newF.put(seg, f + 1);
				}
				
				//!!
				System.out.println("\b" + corpus.seek * 100 / corpus.size + "%");
			}
			this.corpus.reset();
			
			this.F = newF;
		}
	}
	
	public void test() {
		for (int i = 0; corpus.hasNext() && i < 20; i++) {
			String sequence = this.corpus.nextString();
			List<String> list = evaluate(sequence, true);
		}
		this.corpus.reset();
	}
	
	// 目前只处理字符串，而不是把sequence当为某类型的序列。
	// 其中这种类型可以把像"asb"的当为一个元素而不是目前的三个元素
	public List<String> evaluate(String sequence, boolean explain) {
		int L = sequence.length();
		
		//!!
		System.out.println("Line size: " + L);
		
		// 构建后退数组
		// 时间复杂度:O(L * MAX_LENGTH * MAX_LENGTH)
		//!! 可改进的地方:L的大小!!
		double[][] beta = new double[L][this.MAX_LENGTH];
		int[][] path = new int[L][this.MAX_LENGTH];
		
		for (int i = 0; i < L; i++) {
			// scale first
			double maxScale = 0.0;
			for (int k = 0; k < i && k < this.MAX_LENGTH; k++) {
				maxScale = maxScale >= beta[i - k - 1][k] ? maxScale : beta[i - k - 1][k];
			}
			
			for (int k = 0; k < i && k < this.MAX_LENGTH; k++) {
				beta[i - k - 1][k] /= maxScale;
			}
			
			for (int j = 0; j < this.MAX_LENGTH && j < L - i; j++) {
				if (i == 0) {
					beta[i][j] = PV(sequence.substring(i, i + j + 1));
					path[i][j] = 0;
				} else {
					double max = 0.0;
					int maxIndex = 0;
					
					for (int k = 0; k < this.MAX_LENGTH && k < i; k++) {
						int t = i - k - 1;
						double value =
								beta[t][k]
										* LRV(sequence.substring(t, i), sequence.substring(i, i + j + 1))
										;
						
						//!!
//						System.out.println("LRV: " + sequence.substring(t, i) + " " + sequence.substring(i, i + j + 1) + " " + 
//								LRV(sequence.substring(t, i), sequence.substring(i, i + j + 1)));
						
						if (max < value) {
							max = value;
							maxIndex = k;
						}
					}
					
					//!!
					beta[i][j] = max + PV(sequence.substring(i, i + j + 1));
					path[i][j] = maxIndex;
				}
			}
		}
		
		// 在Segmenter.MAX_LENGTH个元素中，找最大值
		double maxValue = 0.0;
		int maxIndex = 0;
		
		for (int i = 0; i < this.MAX_LENGTH && i < L; i++) {
			if (maxValue < beta[L - i - 1][i]) {
				maxValue = beta[L - i - 1][i];
				maxIndex = i;
			}
		}
		
		int nextI = L - maxIndex - 1;
		int nextJ = maxIndex;
		int previousI = L;
		LinkedList<String> queue = new LinkedList<String>();
		
		while (nextI > 0) {
			queue.addFirst(sequence.substring(nextI, previousI));
			
			previousI = nextI;
			nextI = nextI - path[nextI][nextJ] - 1;
			nextJ = path[nextI][nextJ];
		}
		queue.addFirst(sequence.substring(nextI, previousI));
		
		
		// 测试用，输出概率数组
		if (explain) {
			LinkedList<String> explainQueue = new LinkedList<String>();
			DecimalFormat df=new DecimalFormat();
			df.applyPattern("0.00");
			
			nextI = L - maxIndex - 1;
			nextJ = maxIndex;
			previousI = L;
			
			while (nextI > 0) {
				explainQueue.addFirst(sequence.substring(nextI, previousI) +
						" : " +df.format(
						(beta[nextI][nextJ] / beta[nextI - path[nextI][nextJ] - 1][path[nextI][nextJ]])));
				
				previousI = nextI;
				nextI = nextI - path[nextI][nextJ] - 1;
				nextJ = path[nextI][nextJ];
			}
			
			for (String line : explainQueue) {
				System.out.println(line);
			}
			System.out.println();
		}
		
		
		return queue;
	}
	
	// update the value of FM,HLM,HRM 
	//   without changing data of treeNode
	public void update() {
		visitPrefix(this.prefixTree.root, "", 0);
		visitSuffix(this.suffixTree.root, 0);
		
		for (int i = 0; i < this.MAX_LENGTH; i++) {
			if (FM_COUNT[i] != 0) {
				FM[i] /= FM_COUNT[i];
			}
			
			if (HLM_COUNT[i] != 0) {
				HLM[i] /= HLM_COUNT[i];
			}
			
			if (HRM_COUNT[i] != 0) {
				HRM[i] /= HRM_COUNT[i];
			}
		}
	}
	
	public void visitPrefix(PrefixTreeNode node, String prefix, int depth) {
		if (depth > 0 && depth < this.MAX_LENGTH) {
			this.F.put(prefix, node.count);
		}
		
		if (depth <= this.MAX_LENGTH && depth > 0) {
			this.FM[depth - 1] += node.count;
			this.FM_COUNT[depth - 1] += 1;
		}
		
		if (depth > 1) {
			double p = 1.0 * node.count / node.parent.sum;
			this.HRM[depth - 2] -= p * Math.log(p);
		}
		
		if (depth <= this.MAX_LENGTH) {
			if (node.children != null) {
				for (String key : node.children.keySet()) {
					PrefixTreeNode child = node.children.get(key);
					visitPrefix(child, prefix + key, depth + 1);
				}
			}
			
			//!!
			if (depth == 2) {
//				System.out.println(HRM[depth - 1]);
			}
			
			if (depth > 0) {
				this.HRM_COUNT[depth - 1] += 1;
			}
		}
	}
	
	public void visitSuffix(SuffixTreeNode node, int depth) {
		if (depth > 1) {
			double p = 1.0 * node.count / node.parent.sum;
			this.HLM[depth - 2] -= p * Math.log(p);
		}
		
		if (depth <= this.MAX_LENGTH) {
			if (node.children != null) {
				for (String key : node.children.keySet()) {
					SuffixTreeNode child = node.children.get(key);
					visitSuffix(child, depth + 1);
				}
			}
			
			if (depth > 0) {
				this.HLM_COUNT[depth - 1] += 1;
			}
		}
	}
	
	// Point Value
	private double PV(String subSequence) {
		if (subSequence == null || subSequence.length() == 0) {
			System.out.println("PV error!");
			System.exit(0);
		}
		
		int L = subSequence.length();
		Integer f = this.F.get(subSequence);
		if (f == null) {
			return 0;
		}
		
		return Math.pow(1.0 * f / FM[L - 1], L);
	}
	
	private double LRV(String left, String right) {
		if (left == null || left.length() == 0 || 
				right == null || right.length() == 0) {
			System.out.println("LRV error!");
			System.exit(0);
		}
		
		int Ll = left.length();
		int Lr = right.length();
		
		double hsr = HSR(left);
		double hsl = HSL(right);
		
//		System.out.println(hsr + " " + HRM[Ll - 1]);
		
		return Math.pow(hsr * hsl / HRM[Ll - 1] / HLM[Lr - 1] + 0.0000000001 , 1.0);
	}
	
	private double HSR(String left) {
		PrefixTreeNode node = this.prefixTree.search(left);
		
		if (node == null) {
			return 0;
		}
		
		if (node.entorpy == 0.0) {
			double entorpy = 0;
			for (String key : node.children.keySet()) {
				PrefixTreeNode child = node.children.get(key);
				
				double p = 1.0 * child.count / node.sum;
				entorpy -= p * Math.log(p);
			}
			
			node.entorpy = entorpy;
		}
		
		return node.entorpy;
	}
	
	private double HSL(String right) {
		SuffixTreeNode node = this.suffixTree.search(right);
		
		if (node == null) {
			return 0;
		}
		
		if (node.entorpy == 0.0) {
			double entorpy = 0;
			for (String key : node.children.keySet()) {
				SuffixTreeNode child = node.children.get(key);
				
				double p = 1.0 * child.count / node.sum;
				entorpy -= p * Math.log(p);
			}
			node.entorpy = entorpy;
		}
		
		return node.entorpy;
	}
}
