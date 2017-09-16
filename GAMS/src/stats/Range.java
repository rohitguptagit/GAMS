package stats;

import java.util.HashMap;

import com.google.common.collect.Maps;

public class Range {

	//performance descriptors/distribution
	enum PERF { 
		BELOW_EXPECTATIONS("Below Expectations"), MARGINAL("Marginal"), MEETS_EXPECTATIONS("Meets Expectations"), EXCEEDS_EXPECTATIONS("Exceeds Expectations");
		
		private String name;
		
		private PERF(String name){
			this.name = name;
		}
		
		public String getName(){
			return this.name;
		}
	}
	
	public HashMap<String, HashMap<PERF, Integer>> perfDist = Maps.newHashMap();
	
	public HashMap<PERF, Integer> genBuckets(){
		HashMap<PERF, Integer> buckets = Maps.newHashMap();
		for(PERF p : PERF.values()){
			buckets.put(p, 0); //HashMap filled with Performance descriptors and initializes the count value of each to 0
		}
		return buckets;
	}
	//descriptor ranges (whatever range is obtained outputs the respective descriptor)
	public PERF rank(Double score){
		if(score.compareTo(new Double("54.5")) < 0){
			return PERF.BELOW_EXPECTATIONS;
		}
		else if(score.compareTo(new Double("54.5")) >= 0 && score.compareTo(new Double("64.5")) < 0){
			return PERF.MARGINAL;
		}
		else if(score.compareTo(new Double("64.5")) >= 0 && score.compareTo(new Double("79.5")) < 0){
			return PERF.MEETS_EXPECTATIONS;
		}
		else{
			return PERF.EXCEEDS_EXPECTATIONS;
		}
	}
	
	public int getTotalStudents(){
		int total = 0;
		for(String key : this.perfDist.keySet()){
			for(PERF perf : this.perfDist.get(key).keySet()){
				total += this.perfDist.get(key).get(perf).intValue();
			}
			return total;
		}
		return total;
	}
}
