package graduateAttributes;

public class ET extends Attribute {
	
	private final String name = "Use of Engineering Tools";
	private final String shortName = "ET";
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getShortName() {
		return this.shortName;
	}
}
