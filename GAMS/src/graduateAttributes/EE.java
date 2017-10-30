package graduateAttributes;

public class EE extends Attribute {
	
	private final String name = "Ethics and Equity";
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
