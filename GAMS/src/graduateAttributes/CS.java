package graduateAttributes;

public class CS extends Attribute {

	private final String name = "Communication Skills";
	private final String shortName = "CS";
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getShortName() {
		return this.shortName;
	}
	
	
}
