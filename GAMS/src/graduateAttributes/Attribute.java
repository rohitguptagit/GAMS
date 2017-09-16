package graduateAttributes;

import java.text.DecimalFormat;
import java.util.HashMap;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

public abstract class Attribute {
	
	private double numerator, denominator, result;
	private String performance; //indicates performance measure relative to enum values in Range class
	public HashMap<String, Indicator> indicatorScores = Maps.newHashMap(); //Key: Indicator name (CS.3); Value: Final value (what the student obtained) for that indicator
	
	public Attribute(){
	}
	
	public Attribute(double num, double den){
		this.numerator = num;
		this.denominator = den;
		this.result = num/den * 100; //calculates numerator and denominator per attribute per student in each course
	}

	public abstract String getName();

	public double getNumerator() {
		return numerator;
	}

	public void setNumerator(double numerator) {
		this.numerator = numerator;
	}

	public double getDenominator() {
		return denominator;
	}

	public void setDenominator(double denominator) {
		this.denominator = denominator;
	}

	public String getPerformance() {
		return performance;
	}

	public void setPerformance(String performance) {
		this.performance = performance;
	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat(".##"); //rounds to 2 decimal places
		return "Attribute [name=" + getName() + " numerator=" + df.format(numerator) + ", denominator=" + denominator +  ", result=" + df.format(result) + ", performance=" + performance
				+ ", indicatorScores=" + indicatorScores + "]";
	}
	
/*	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}*/
	
}
