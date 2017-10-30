package tests;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;

import graduateAttributes.Indicator;
import stats.StatCol;
import stats.StatsCalc;

public class StatsColTest {

	@Test
	public void testHeaderParse() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String[] testHeader = "OrgDefinedId,Last Name,First Name,T1;IT.1;0.5;3;Tutorials,T2;IT.1;0.5;3;Tutorials,T3;IT.1;0.5;3;Tutorials,T4;IT.1;0.5;3;Tutorials,T5;IT.1;0.5;3;Tutorials".split(",");
		Map<String, HashSet<StatCol>> hashTest = Maps.newHashMap();
		Map<Integer, StatCol> columns = Maps.newHashMap();
		StatsCalc.headerParse(testHeader, hashTest, columns);
		System.out.println(hashTest.size());
		hashTest.entrySet().stream().forEach(entry -> {
			System.out.println(entry.getKey());
			entry.getValue().stream().forEach(statCol -> {
				System.out.println("\t" + statCol);
			});
		});
		assertEquals(5, hashTest.get("Tutorials").size());
	}
	
	@Test
	public void testGson() {
		HashSet<Indicator> inds = Sets.newHashSet();
		inds.add(new Indicator("test"));
		inds.add(new Indicator("test2"));
		inds.add(new Indicator("test3"));
		inds.add(new Indicator("test"));
		Gson gson = new Gson();
		System.out.println(gson.toJson(inds)); 
	}

}
