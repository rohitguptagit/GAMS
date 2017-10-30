package stats;

import java.util.Objects;

public class MajorObject {

	String name;
	Range range;
	
	public MajorObject(String name, Range range) {
		this.name = name;
		this.range = range;
	}
	
	public MajorObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof MajorObject)) return false;
		
		MajorObject ind = (MajorObject) o;
		return ind.getName().equals(this.getName());
	}
}
