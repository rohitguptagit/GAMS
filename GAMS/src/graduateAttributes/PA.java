package graduateAttributes;

public class PA extends Attribute {
	
	private final String name = "Problem Analysis";
	private final String shortName = "PA";
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getShortName() {
		return this.shortName;
	}
}
