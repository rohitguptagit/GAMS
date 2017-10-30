package graduateAttributes;

public class DE extends Attribute {
	
	private final String name = "Design";
	private final String shortName = "DE";
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getShortName() {
		return this.shortName;
	}
}
