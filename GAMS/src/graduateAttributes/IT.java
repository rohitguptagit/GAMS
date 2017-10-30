package graduateAttributes;

public class IT extends Attribute {
	
	private final String name = "Individual and Team Work";
	private final String shortName = "IT";
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getShortName() {
		return this.shortName;
	}

}
