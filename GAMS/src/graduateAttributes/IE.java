package graduateAttributes;

public class IE extends Attribute {
	
	private final String name = "Impact of Engineering on Society and the Environment";
	private final String shortName = "IE"; 
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getShortName() {
		return this.shortName;
	}
	
}
