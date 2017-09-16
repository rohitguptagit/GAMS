package stats;

import graduateAttributes.Attribute;
import graduateAttributes.Indicator;

public class StatCol {
	
	Attribute attribute;
	Indicator indicator;
	int column;
	double maxPoints, categoryWeight, subcatPoints;
	String category, subcategory;

	public StatCol(int col, String subcategory, double maxPoints, double categoryWeight, String category) {
		this.column = col; //column number in the parsing to keep track of current and previous locations while performing parsing and numerical manipulation
		this.maxPoints = maxPoints;
		this.categoryWeight = categoryWeight;
		this.category = category; //Tutorials for example
		this.subcategory = subcategory; //T-4 for example
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public Indicator getIndicator() {
		return indicator;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	public double getMaxPoints() {
		return maxPoints;
	}

	public void setMaxPoints(double maxPoints) {
		this.maxPoints = maxPoints;
	}

	public double getCategoryWeight() {
		return categoryWeight;
	}

	public void setCategoryWeight(double categoryWeight) {
		this.categoryWeight = categoryWeight;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public double getSubcatPoints() {
		return subcatPoints;
	}

	public void setSubcatPoints(double subcatPoints) {
		this.subcatPoints = subcatPoints;
	}

	@Override
	public String toString() {
		return "StatCol [attribute=" + attribute + ", indicator=" + indicator + ", column=" + column + ", maxPoints="
				+ maxPoints + ", categoryWeight=" + categoryWeight + ", subcatPoints=" + subcatPoints + ", category="
				+ category + ", subcategory=" + subcategory + "]";
	}

}
