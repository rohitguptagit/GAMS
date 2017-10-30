package graduateAttributes;

public class KB extends Attribute {
	
	private final String name = "Knowledge Base for Engineering";
	private final String shortName = "KB";
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getShortName() {
		return this.shortName;
	}

}
