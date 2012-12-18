package segmenter.db;

import java.util.HashMap;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class MemHSP1Data {
	@PrimaryKey
    private final String pKey = "MemHSP1";
	
	// HSP1L与HSP1R ***
	private HashMap<String, Double> mHSP1L;
	private HashMap<String, Double> mHSP1R;

	public HashMap<String, Double> getmHSP1L() {
		return mHSP1L;
	}

	public HashMap<String, Double> getmHSP1R() {
		return mHSP1R;
	}
	
	private MemHSP1Data() {
		
	}
	
	public MemHSP1Data(
			HashMap<String, Double> HSP1L, HashMap<String, Double> HSP1R
			) {
		mHSP1L = HSP1L;
		mHSP1R = HSP1R;
	}
}
