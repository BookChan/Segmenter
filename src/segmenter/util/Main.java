package segmenter.util;

import segmenter.Corpus;
import segmenter.Segmenter;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Segmenter s = new Segmenter(new Corpus());
		s.preProcess();
		s.train(2);
		s.test();
	}

}
