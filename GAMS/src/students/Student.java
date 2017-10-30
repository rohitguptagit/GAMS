package students;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

import graduateAttributes.Attribute;
import graduateAttributes.CS;
import graduateAttributes.DE;
import graduateAttributes.EE;
import graduateAttributes.EP;
import graduateAttributes.ET;
import graduateAttributes.IE;
import graduateAttributes.IN;
import graduateAttributes.IT;
import graduateAttributes.KB;
import graduateAttributes.LL;
import graduateAttributes.PA;
import graduateAttributes.PR;

public class Student {
	
	private int sId;
	private String lastName, firstName;
	private String major;
	
	private transient HashMap<String, Attribute> attributes = Maps.newHashMap(); //Key: Attribute class name; Value: Attribute object
	//Random rand = new Random();
	@SuppressWarnings("unused")
	private List<Attribute> attributeList = Lists.newArrayList();
	
	public Student(int sId, String lastName, String firstName, String major){
		this.sId = sId;
		this.lastName = lastName;
		this.firstName = firstName;
		this.major = major;
		attributes.put(CS.class.getSimpleName(), new CS());
		attributes.put(KB.class.getSimpleName(), new KB());
		attributes.put(PA.class.getSimpleName(), new PA());
		attributes.put(IT.class.getSimpleName(), new IT());
		attributes.put(DE.class.getSimpleName(), new DE());
		attributes.put(ET.class.getSimpleName(), new ET());
		attributes.put(EP.class.getSimpleName(), new EP());
		attributes.put(EE.class.getSimpleName(), new EE());
		attributes.put(IE.class.getSimpleName(), new IE());
		attributes.put(IN.class.getSimpleName(), new IN());
		attributes.put(LL.class.getSimpleName(), new LL());
		attributes.put(PR.class.getSimpleName(), new PR());	
		attributeList = new ArrayList<Attribute>(attributes.values());
	}


	public HashMap<String, Attribute> strip(HashMap<String, Attribute> attributes){
		HashMap<String, Attribute> stripped = Maps.newHashMap(); //Holds attributes that specifically have indicator data native to the course
		for(Entry<String, Attribute> currentAttribute : attributes.entrySet()){
			if(!currentAttribute.getValue().indicatorScores.isEmpty()){
				stripped.put(currentAttribute.getKey(), currentAttribute.getValue());
			}
		}
		
		return stripped;
		
	}
	
	public int getsId() {
		return sId;
	}
	public void setsId(int sId) {
		this.sId = sId;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}

	public HashMap<String, Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, Attribute> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		//return String.format("Student [ sId= %s, lastName= %s", sId, lastName);
		return "Student ["+"sId=" + sId + ", lastName=" + lastName + ", firstName=" + firstName + " major=" + major + ", attributes=" + strip(attributes) + "]";
	}
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	

}
