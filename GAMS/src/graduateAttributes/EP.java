package graduateAttributes;

public class EP extends Attribute {
	
	private final String name = "Economics and Project Management";
	private final String shortName = "EE";
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getShortName() {
		return this.shortName;
	}
}
