package stats;

import java.util.HashMap;
import java.util.Objects;

import com.google.common.base.Joiner;

import stats.Range.PERF;

public class PerfObject {
	HashMap<PERF, Integer> perfs;
	String name;
	
	public PerfObject(String name) {
		this.name = name;
		this.perfs = new HashMap<>();
	}
	public PerfObject(String name, HashMap<PERF, Integer> perfs) {
		this.name = name;
		this.perfs = perfs;
	}
	public HashMap<PERF, Integer> getPerfs() {
		return perfs;
	}
	public void setPerfs(HashMap<PERF, Integer> perfs) {
		this.perfs = perfs;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof PerfObject)) return false;
		
		PerfObject ind = (PerfObject) o;
		return ind.getName().equals(this.getName());
	}
	
	@Override
	public String toString() {
		return "PerfObject [name=" + name + ", perfs=" + Joiner.on("\n").withKeyValueSeparator(" -> ").join(perfs) + "]";
	}
	
}
