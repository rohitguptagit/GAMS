package graduateAttributes;

import java.text.DecimalFormat;
import java.util.Objects;

public class Indicator {

	private double numerator, denominator, result;
	private String name, performance; //performance description for each student's indicator relative to the enum value present in the Range class
	
	public Indicator(String name){
		this.name = name;
	}
	
	public Indicator(String name, double num, double den){
		this.name = name;
		this.numerator = num;
		this.denominator = den;
		this.result = num/den * 100; //numerator and denominator calculation per indicator per student
	}
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int hashCode() {
		return Objects.hash(this.name);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof Indicator)) return false;
		
		Indicator ind = (Indicator) o;
		return ind.getName().equals(this.getName());
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat(".##"); //rounds to 2 decimal places
		return "Indicator [name=" + name + ", numerator=" + df.format(numerator) + ", denominator=" + denominator +", result=" + df.format(result)
				+ ", performance=" + performance + "]";
	}
	
	
	
}
