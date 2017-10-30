package stats;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import graduateAttributes.Attribute;
import graduateAttributes.Indicator;
import stats.Range.PERF;
import students.Student;

public class StatsCalc {

	public static void main(String[] args) throws Exception {
		String sampleData = "/Users/rgupta/Desktop/GAMS/sampleData_actual.csv";
		String majorData = "/Users/rgupta/Desktop/GAMS/Major_sample.csv";

		run(sampleData, majorData);

	}

	private static void run(String sampleData, String majorData) {
		String[] header = null;
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new FileReader(sampleData));
			String line = bReader.readLine();
			// imports data file (will be 2 files in the future)
			header = line.split(","); // splits data by commas
			System.out.println(line);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// start processing header
		Map<String, HashSet<StatCol>> statHash = Maps.newHashMap(); // Key: Category Name; Value: HashSet of StatCols
		// for that category --modified
		Map<Integer, StatCol> columns = Maps.newHashMap(); // Key: column number; Value: StatCol
		headerParse(header, statHash, columns);

		for (String category : statHash.keySet()) {
			HashSet<StatCol> statCols = statHash.get(category);
			double totalMP = 0.0;
			for (StatCol s : statCols) {
				totalMP += s.getMaxPoints(); // sums maxPoints for each category to get total maxPoints
			}
			for (StatCol s : statCols) {
				s.setSubcatPoints(s.getMaxPoints() / totalMP * s.getCategoryWeight()); // formula calculation to obtain
				// actual maxPoints for that
				// subcategory and sets it as
				// such
			}
		}
		// System.out.println(statHash);
		// System.out.println(columns);

		// Read rows other than header (data)
		HashMap<Integer, Student> studs = Maps.newHashMap();
		BufferedReader genReader;
		try {
			genReader = new BufferedReader(new FileReader(majorData));
			// Reader for global/general file
			String line2 = genReader.readLine();
			while ((line2 = genReader.readLine()) != null) {
				Student s = Parsing.createStudentFromLine(line2);
				studs.put(s.getsId(), s);
			}
			studentDataLoader(bReader, columns, studs);
			genReader.close();
			bReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		studs.entrySet().removeIf(stud -> stud.getValue().strip(stud.getValue().getAttributes()).size() < 1);

		String aaa = generateJson(new ArrayList<Student>(studs.values()), "STUDS");
		System.out.println(aaa);

		// aggregate inds and attr by major
		ArrayList<MajorObject> indMajorsRanges = new ArrayList<>();
		ArrayList<MajorObject> attrMajorsRanges = new ArrayList<>();
		for (Student stud : studs.values()) {
			String major = stud.getMajor();
			MajorObject maj = new MajorObject(major, new Range());
			if (!indMajorsRanges.contains(maj)) {
				indMajorsRanges.add(maj);
			}
			if (!attrMajorsRanges.contains(maj)) {
				attrMajorsRanges.add(maj);
			}
			majorAggregation(indMajorsRanges, attrMajorsRanges, stud, major);
		}

		System.out.println("============================ BY MAJOR ============================");

		System.out.println(majorDistributionJsonify(indMajorsRanges, "PERFORMANCES"));
		//majorDistributionJsonify(attrMajorsRanges);
	}

	private static void majorAggregation(ArrayList<MajorObject> indMajorsRanges,
			ArrayList<MajorObject> attrMajorsRanges, Student stud, String major) {
		for (Entry<String, Attribute> attrEntry : stud.getAttributes().entrySet()) {
			double sumDen = 0.0, sumNum = 0.0; // stores total den and num for attribute through present GAIs
			for (Indicator current : attrEntry.getValue().indicatorScores) {
				Range range = indMajorsRanges.get(indMajorsRanges.indexOf(new MajorObject(major))).range;
				sumDen += current.getDenominator();
				sumNum += current.getNumerator();
				Double result = current.getNumerator() / current.getDenominator() * 100;
				current.setResult(result);
				PERF indPerf = range.rank(result);
				current.setPerformance(indPerf.name());
				
				ArrayList<PerfObject> perfDist = range.inds;
				PerfObject cur = new PerfObject(current.getName());
				if (!perfDist.contains(cur)) {
					perfDist.add(new PerfObject(current.getName(), range.genBuckets()));
				}
				int position = perfDist.indexOf(cur);
				int original = perfDist.get(position).perfs.get(indPerf);
				perfDist.get(position).perfs.put(indPerf, original + 1);
				//perfDist.get(current.getName()).put(indPerf, perfDist.get(current.getName()).get(indPerf) + 1);
			}
			Attribute currentAttr = attrEntry.getValue();
			currentAttr.setNumerator(sumNum);
			currentAttr.setDenominator(sumDen);
			if (sumDen == 0.0) {
				continue;
			}
			double result = sumNum / sumDen * 100;
			currentAttr.setResult(result);

			Range range = attrMajorsRanges.get(attrMajorsRanges.indexOf(new MajorObject(major))).range;
			PERF attrPerf = range.rank(result);
			currentAttr.setPerformance(attrPerf.name());
			
			ArrayList<PerfObject> perfDistAttr = range.atts;
			PerfObject cur = new PerfObject(currentAttr.getName());
			if (!perfDistAttr.contains(cur)) {
				perfDistAttr.add(new PerfObject(currentAttr.getName(), range.genBuckets()));
			}
			int position = perfDistAttr.indexOf(cur);
			int original = perfDistAttr.get(position).perfs.get(attrPerf);
			perfDistAttr.get(position).perfs.put(attrPerf, original + 1);
			/*if (attrMajorsRanges.get(major).perfDist.get(currentAttr.getName()) == null) {
				attrMajorsRanges.get(major).perfDist.put(currentAttr.getName(),
						attrMajorsRanges.get(major).genBuckets());
			}
			attrMajorsRanges.get(major).perfDist.get(currentAttr.getName()).put(attrPerf,
					attrMajorsRanges.get(major).perfDist.get(currentAttr.getName()).get(attrPerf) + 1);*/
		}
	}

	private static void studentDataLoader(BufferedReader bReader, Map<Integer, StatCol> columns,
			Map<Integer, Student> studs) throws IOException {
		String line;
		while ((line = bReader.readLine()) != null) {
			String[] data = line.split(",");
			Student current = studs.get(Integer.parseInt(data[0]));
			if (current == null) {
				throw new IllegalArgumentException("Student ID not found in general file");
			}
			// System.out.println(current);
			// System.out.println(Arrays.toString(data));
			int nullCount = 0;
			for (int i = 3; i < data.length; i++) {
				String pointToParse = (data[i] == null || "".equals(data[i])) ? "0" : data[i]; // blank spaces in the
				// data are treated as
				// zeros
				if (pointToParse.equals("0"))
					nullCount++;
				if (nullCount >= 0.7 * (data.length - 3)) {
					studs.remove(current.getsId());
				}
				Double dataP = Double.valueOf(pointToParse);
				StatCol statData = columns.get(i);
				Attribute attr = current.getAttributes().get(statData.getAttribute().getClass().getSimpleName());
				/* assigns attributes present in the course to the student */

				String indName = statData.getIndicator().getName();
				Indicator indNum = new Indicator(indName, dataP, statData.getSubcatPoints());
				if (attr.indicatorScores.contains(indNum)) { /* identifies identical indicators and sums their
																	numerators and denominators over parsing */
					int position = attr.indicatorScores.indexOf(indNum);
					indNum.setDenominator(attr.indicatorScores.get(position).getDenominator() + indNum.getDenominator()); /* indicator denominator aggregation*/
					indNum.setNumerator(attr.indicatorScores.get(position).getNumerator() + indNum.getNumerator()); /* indicator numerator aggregation (over all grading items) */
					indNum.setResult(indNum.getNumerator()/indNum.getDenominator() * 100);
					attr.indicatorScores.set(position, indNum);
				}
				else {
					attr.indicatorScores.add(indNum);
				}

			}
		}
	}

	public static void headerParse(String[] header, Map<String, HashSet<StatCol>> statHash,
			Map<Integer, StatCol> columns) {
		for (int i = 3; i < header.length; i++) {
			String[] col = header[i].split(";"); // splits header values by semicolons
			StatCol statCol = new StatCol(i, Parsing.parseIdentifier(1, col[0]), Double.parseDouble(col[2]),
					Double.parseDouble(col[3]), col[4]); // parses header elements

			String parsed = Parsing.parseIdentifier(2, col[1]);
			String className = parsed.split("\\.")[0];
			Attribute a = null;
			try {
				Class<?> clazz = Class.forName("graduateAttributes." + className);
				a = (Attribute) clazz.newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}

			statCol.setAttribute(a); // parses indicator and attribute to (XX.#)

			Indicator indData = new Indicator(parsed);
			statCol.setIndicator(indData);
			if (statHash.get(statCol.getCategory()) == null) {
				statHash.put(statCol.getCategory(), Sets.newHashSet());
			}
			statHash.get(statCol.getCategory()).add(statCol);
			columns.put(i, statCol); // adds the parsed values into the variables given by statCol which now define
			// what each actual element in the header represents
		}
	}

	private static String majorDistributionJsonify(ArrayList<MajorObject> majorsRanges, String identifier) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return "import { Performance } from './performance';" + " export const " + identifier + ": Performance[] = "
		+ gson.toJson(majorsRanges) + ";";
		/*for (Entry<String, Range> majorsEntry : majorsRanges.entrySet()) {
			if (majorsEntry.getValue().getTotalStudents() < 5)
				continue;
			System.out.println("\n======== Major: " + majorsEntry.getKey());
			for (Entry<String, HashMap<PERF, Integer>> entry : majorsEntry.getValue().perfDist.entrySet()) {
				System.out.println();
				System.out.println(entry.getKey());
				for (Entry<PERF, Integer> e2 : entry.getValue().entrySet()) {
					System.out.println(e2.getKey().getName() + ": " + e2.getValue());
				}
			}
		}*/
	}

	/**
	 * Utility method to take in all student objects and return them in Json format
	 * 
	 * 
	 * @return all student objects in Json format that angular accepts
	 */
	private static String generateJson(List<Student> students, String identifier) {
		//Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Gson gson = new Gson();
		students.sort(Comparator.comparing(Student::getsId));
		return "import { Student } from './student';" + " export const " + identifier + ": Student[] = "
		+ gson.toJson(students) + ";";

	}

}
