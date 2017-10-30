package graduateAttributes;

public class PR extends Attribute {
	
	private final String name = "Professionalism";
	private final String shortName = "PR";
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getShortName() {
		return this.shortName;
	}
}
