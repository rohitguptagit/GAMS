package stats;

import students.Student;

public class Parsing {
	
	protected static String parseIdentifier(int numLetters, String parse){
		String indLetters = parse.substring(0, numLetters);
		int i = 0;
		while(i < parse.length() && !Character.isDigit(parse.charAt(i))) i++;
		
		String indNumber = parse.substring(i,parse.length());
		String output = indLetters + "." + Integer.parseInt(indNumber); //this parses any input string which has 2 letters then a number after separated by whatever (or nothing) - still need to work
																		//on case for MT1-Q1...
		//System.out.println(output);    //demo output format for all cases of professor entries
		return output;
		
	}
	
	protected static Student createStudentFromLine(String parseMe){
		String[] studentInfo = parseMe.split(",");
		int sID = Integer.parseInt(studentInfo[1]);
		String[] lastFirst = studentInfo[2].split("/");
		Student current = new Student(sID,lastFirst[0] ,lastFirst[1], studentInfo[3]);
		return current;
		
	}
	

}
