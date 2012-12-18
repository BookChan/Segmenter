package segmenter.util;

import java.text.DecimalFormat;
import java.util.LinkedList;

import segmenter.tree.PrefixTreeNode;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DecimalFormat df=new DecimalFormat();
		df.applyPattern("0.00");
		System.out.println(df.format(3123.1233));
		
		LinkedList<segmenter.tree.PrefixTreeNode> list = new LinkedList<>();
		for (int i = 0; i < 8000000; i++) {
			list.add(new PrefixTreeNode("" + i, null, 0));
		}
	}

}
