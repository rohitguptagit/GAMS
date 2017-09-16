package stats;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;

import graduateAttributes.Attribute;
import graduateAttributes.Indicator;
import stats.Range.PERF;
import students.Student;

public class StatsCalc {

	public static void main(String[] args) throws Exception {
		
		@SuppressWarnings("resource")
		BufferedReader bReader = new BufferedReader(new FileReader("/Users/rgupta/Desktop/GAMS/sampleData_actual.csv")); //imports data file (will be 2 files in the future)
		String line = bReader.readLine();
		String[] header = line.split(","); //splits data by commas
		//start processing header
		HashMap<String, HashSet<StatCol>> statHash = Maps.newHashMap(); //Key: Category Name; Value: HashSet of StatCols for that category  --modified
		HashMap<Integer, StatCol> columns = Maps.newHashMap(); //Key: column number; Value: StatCol
		headerParse(header, statHash, columns);
		
		for(String category : statHash.keySet()){
			HashSet<StatCol> statCols = statHash.get(category);
			double totalMP = 0.0;
			for(StatCol s : statCols){
				totalMP += s.getMaxPoints(); //sums maxPoints for each category to get total maxPoints
			}
			for(StatCol s : statCols){
				s.setSubcatPoints(s.getMaxPoints()/totalMP * s.getCategoryWeight()); //formula calculation to obtain actual maxPoints for that subcategory and sets it as such
			}
		}
		//System.out.println(statHash);
		//System.out.println(columns);
	
		//Read rows other than header (data)
		HashMap<Integer, Student> studs = Maps.newHashMap();
		BufferedReader genReader = new BufferedReader(new FileReader("/Users/rgupta/Desktop/GAMS/Major_sample.csv")); //Reader for global/general file
		String line2 = genReader.readLine();
		while((line2 = genReader.readLine()) != null){
			Student s = Parsing.createStudentFromLine(line2);
			studs.put(s.getsId(), s);
		}
		genReader.close();
		
		while((line = bReader.readLine()) != null){
			String[] data = line.split(",");
			Student current = studs.get(Integer.parseInt(data[0]));
			if(current == null){
				throw new Exception("Student ID not found in general file");
			}
			//System.out.println(current);
			//System.out.println(Arrays.toString(data));
			int nullCount = 0;
			for(int i = 3; i<data.length; i++){
				String pointToParse = (data[i] == null || "".equals(data[i])) ? "0" : data[i]; //blank spaces in the data are treated as zeros
				if(pointToParse.equals("0")) nullCount++;
				if(nullCount >= 0.7 * (data.length - 3)){
					studs.remove(current.getsId());
				}
				Double dataP = Double.valueOf(pointToParse);
				StatCol statData = columns.get(i);
				Attribute attr = current.getAttributes().get(statData.getAttribute().getClass().getSimpleName()); //assigns attributes present in the course to the student

				String indName = statData.getIndicator().getName();
				Indicator indNum = new Indicator(indName, dataP, statData.getSubcatPoints());
				if(attr.indicatorScores.containsKey(indName)){ //identifies identical indicators and sums their numerators and denominators over parsing
					indNum.setDenominator(attr.indicatorScores.get(indName).getDenominator() + indNum.getDenominator()); //indicator denominator aggregation
					indNum.setNumerator(attr.indicatorScores.get(indName).getNumerator() + indNum.getNumerator()); //indicator numerator aggregation (over all grading items)
				}				
				attr.indicatorScores.put(indName, indNum);
				
			}
		}
		bReader.close();
		studs.entrySet().removeIf(stud -> stud.getValue().strip(stud.getValue().getAttributes()).size() < 1);
		
		//Per indicator and attribute stats
		Range indRanges = new Range();
		Range attrRanges = new Range(); //this whole part calculates the results for each student in terms of numerator and denominator and assigns it the descriptor value according to those present in the Range class
		
	
		
		String aaa = generateJson(new ArrayList<Student>(studs.values()),"test");
		System.out.print(aaa);
		
		
		
		
		
		for(Student stud : studs.values()){
			for(Entry<String,Attribute> attrEntry : stud.getAttributes().entrySet()){
				double sumDen = 0.0, sumNum = 0.0; //stores total den and num for attribute through present GAIs
				for(Entry<String, Indicator> indEntry : attrEntry.getValue().indicatorScores.entrySet()){
					Indicator current = indEntry.getValue();
					sumDen += current.getDenominator();
					sumNum += current.getNumerator();
					Double result = current.getNumerator()/current.getDenominator() * 100;
					indEntry.getValue().setResult(result);
					PERF indPerf = indRanges.rank(result);
					current.setPerformance(indPerf.name());
					if(indRanges.perfDist.get(current.getName()) == null){
						indRanges.perfDist.put(current.getName(), indRanges.genBuckets());
					}
					indRanges.perfDist.get(current.getName()).put(indPerf, indRanges.perfDist.get(current.getName()).get(indPerf) + 1);
				}
				Attribute currentAttr = attrEntry.getValue();
				currentAttr.setNumerator(sumNum);
				currentAttr.setDenominator(sumDen);
				if(sumDen == 0.0){
					continue;
				}
				double result = sumNum/sumDen * 100;
				currentAttr.setResult(result);
				PERF attrPerf = attrRanges.rank(result);
				currentAttr.setPerformance(attrPerf.name());
				if(attrRanges.perfDist.get(currentAttr.getName()) == null){
					attrRanges.perfDist.put(currentAttr.getName(), attrRanges.genBuckets());
				}
				attrRanges.perfDist.get(currentAttr.getName()).put(attrPerf, attrRanges.perfDist.get(currentAttr.getName()).get(attrPerf) + 1);
			}
			//System.out.println(stud);
			//System.out.println(stud.toJson());
		}
		
		
		
		for(Entry<String, HashMap<PERF, Integer>> entry : indRanges.perfDist.entrySet()){
			System.out.println(entry.getKey());
			for(Entry<PERF, Integer> e2 : entry.getValue().entrySet()){
				System.out.println(e2.getKey().getName() + ": " + e2.getValue());
			}
		}
		System.out.println();
		for(Entry<String, HashMap<PERF, Integer>> entry : attrRanges.perfDist.entrySet()){
			System.out.println();
			System.out.println(entry.getKey());
			for(Entry<PERF, Integer> e2 : entry.getValue().entrySet()){
				System.out.println(e2.getKey().getName() + ": " + e2.getValue()); //Prints out the actual performance descriptors
			}
		}
		
		//aggregate inds and attr by major
		HashMap<String, Range> indMajorsRanges = Maps.newHashMap();
		HashMap<String, Range> attrMajorsRanges = Maps.newHashMap();
		for(Student stud : studs.values()){
			String major = stud.getMajor();
			if(indMajorsRanges.get(major) == null){
				indMajorsRanges.put(major, new Range());
			}
			if(attrMajorsRanges.get(major) == null){
				attrMajorsRanges.put(major, new Range());
			}
			for(Entry<String,Attribute> attrEntry : stud.getAttributes().entrySet()){
				double sumDen = 0.0, sumNum = 0.0; //stores total den and num for attribute through present GAIs
				for(Entry<String, Indicator> indEntry : attrEntry.getValue().indicatorScores.entrySet()){
					Indicator current = indEntry.getValue();
					sumDen += current.getDenominator();
					sumNum += current.getNumerator();
					Double result = current.getNumerator()/current.getDenominator() * 100;
					indEntry.getValue().setResult(result);
					PERF indPerf = indMajorsRanges.get(major).rank(result);
					current.setPerformance(indPerf.name());
					if(indMajorsRanges.get(major).perfDist.get(current.getName()) == null){
						indMajorsRanges.get(major).perfDist.put(current.getName(), indMajorsRanges.get(major).genBuckets());
					}
					indMajorsRanges.get(major).perfDist.get(current.getName()).put(indPerf, indMajorsRanges.get(major).perfDist.get(current.getName()).get(indPerf) + 1);
				}
				Attribute currentAttr = attrEntry.getValue();
				currentAttr.setNumerator(sumNum);
				currentAttr.setDenominator(sumDen);
				if(sumDen == 0.0){
					continue;
				}
				double result = sumNum/sumDen * 100;
				currentAttr.setResult(result);
				PERF attrPerf = attrMajorsRanges.get(major).rank(result);
				currentAttr.setPerformance(attrPerf.name());
				if(attrMajorsRanges.get(major).perfDist.get(currentAttr.getName()) == null){
					attrMajorsRanges.get(major).perfDist.put(currentAttr.getName(), attrMajorsRanges.get(major).genBuckets());
				}
				attrMajorsRanges.get(major).perfDist.get(currentAttr.getName()).put(attrPerf, attrMajorsRanges.get(major).perfDist.get(currentAttr.getName()).get(attrPerf) + 1);
			}
		}
		
		System.out.println("============================ BY MAJOR ============================");
		
		majorDistribution(indMajorsRanges);
		System.out.println();
		majorDistribution(attrMajorsRanges);
		
	}

	private static void headerParse(String[] header, HashMap<String, HashSet<StatCol>> statHash,
			HashMap<Integer, StatCol> columns)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		for(int i=3; i<header.length;i++){
			String[] col = header[i].split(";"); //splits header values by semicolons
			StatCol statCol = new StatCol(i, Parsing.parseIdentifier(1,col[0]), Double.parseDouble(col[2]), Double.parseDouble(col[3]), col[4]); //parses header elements
			
			String parsed = Parsing.parseIdentifier(2,col[1]);
			String className = parsed.split("\\.")[0];
			Class<?> clazz = Class.forName("graduateAttributes." +className);
			Attribute a = (Attribute) clazz.newInstance();
			statCol.setAttribute(a); //parses indicator and attribute to (XX.#)
			
			Indicator indData = new Indicator(parsed);
			statCol.setIndicator(indData);
			if(statHash.get(statCol.getCategory()) == null){
				statHash.put(statCol.getCategory(), Sets.newHashSet());
			}
			statHash.get(statCol.getCategory()).add(statCol);
			columns.put(i, statCol); //adds the parsed values into the variables given by statCol which now define what each actual element in the header represents
		}
	}

	private static void majorDistribution(HashMap<String, Range> majorsRanges) {
		for (Entry<String, Range> majorsEntry : majorsRanges.entrySet()) {
			if(majorsEntry.getValue().getTotalStudents() < 5) continue;
			System.out.println("\n======== Major: " +majorsEntry.getKey());
			for (Entry<String, HashMap<PERF, Integer>> entry : majorsEntry.getValue().perfDist.entrySet()) {
				System.out.println();
				System.out.println(entry.getKey());
				for (Entry<PERF, Integer> e2 : entry.getValue().entrySet()) {
					System.out.println(e2.getKey().getName() + ": " + e2.getValue());
				}
			}
		}
	}
	
	/**
	 * Utility method to take in all student objects and return them in Json format
	 * 
	 * 
	 * @return all student objects in Json format that angular accepts
	 */
	private static String generateJson(List<Student> students, String identifier) {
		Gson gson = new Gson();
		
		return "import { Student } from './student';"
				+ " export const " + identifier + ": Student[] = " + gson.toJson(students) + ";";		
		
	}
	
}
