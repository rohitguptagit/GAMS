package graduateAttributes;

public class LL extends Attribute {
	
	private final String name = "Life-long Learning";
	private final String shortName = "LL";
	
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getShortName() {
		return this.shortName;
	}
}
