package segmenter.db;

import java.util.HashMap;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class MemData {
	@PrimaryKey
    private final String pKey = "MEM";
	
	// 词频:F **
	private HashMap<String, Integer> mF;
	
	// 平均频率FM of L
	private double[] mFM;
	
	// HRM, HLM of 长度L
	private double[] mHLM;
	private double[] mHRM;
	
	public HashMap<String, Integer> getmF() {
		return mF;
	}

	public double[] getmFM() {
		return mFM;
	}

	public double[] getmHLM() {
		return mHLM;
	}

	public double[] getmHRM() {
		return mHRM;
	}

	private MemData() {
		
	}
	
	public MemData(HashMap<String, Integer> F, double[] FM,
			double[] HLM, double[] HRM) {
		mF = F;
		mFM = FM;
		mHLM = HLM;
		mHRM = HRM;
	}
}
