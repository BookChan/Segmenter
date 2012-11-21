package segmenter.util;

import java.text.DecimalFormat;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DecimalFormat df=new DecimalFormat();
		df.applyPattern("0.00");
		System.out.println(df.format(3123.1233));
	}

}
