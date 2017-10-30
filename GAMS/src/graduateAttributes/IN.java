package graduateAttributes;

public class IN extends Attribute {
	
	private final String name = "Investigation";
	private final String shortName = "IN";
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getShortName() {
		return this.shortName;
	}
}
