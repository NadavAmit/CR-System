package bgu.spl.net;


import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {

	//FIELDS
	private  static Database instance;
	private ConcurrentHashMap<String,String> admins;
	private ConcurrentHashMap<String,String> students;						//<STUDENT NAME, PASSWORD>
	private ConcurrentHashMap<Integer, Integer> seatsINCourse;                 //<courseNumber, capacity>
	private ConcurrentHashMap<Integer, ArrayList<Integer>> courseKdam;         //<courseNumber, array of courseNumbers>
	private ConcurrentHashMap<String, ArrayList<Integer>> studentCourses;     //<studentName, array of coursesNumbers>
	private ConcurrentHashMap<Integer, String> courseNumberAndName;
	private ConcurrentHashMap<Integer, Integer> maximumSeatsInCourse;       //<CourseNumber, maximum capacity>
	private List<String> lines;
	private Object lock1;
	//to prevent user from creating new Database
	private Database() {
		// TODO: implement
		admins = new ConcurrentHashMap<String,String>();
		students = new ConcurrentHashMap<String,String>();
		seatsINCourse = new ConcurrentHashMap<Integer, Integer>();
		courseKdam = new ConcurrentHashMap<Integer, ArrayList<Integer>>();
		studentCourses = new ConcurrentHashMap<String, ArrayList<Integer>>();
		courseNumberAndName = new ConcurrentHashMap<Integer,String>();
		maximumSeatsInCourse = new ConcurrentHashMap<Integer, Integer>();
		lines = new ArrayList<String>();
		lock1 = new Object();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	private static class DatabaseSingletonHandler{
		private static Database instance= new Database();
	}

	public static Database getInstance() {
		return DatabaseSingletonHandler.instance;
	}
	
	/**
	 * loades the courses from the file path specified 
	 * into the Database, returns true if successful.
	 */
	public boolean initialize(String coursesFilePath) throws IOException {
		// TODO: implement
		try {
			File myObj = new File(coursesFilePath);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				lines.add(data);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			return false;
		}
		settleCourses();
		return true;
	}


	public boolean checkNameExist(String message, String name){
		if(message.equals("ADMINREG")){
			synchronized (admins){
				if(admins.containsKey(name)){
					return true;
				}
				return false;
			}
		}
		else{
			synchronized (students){
				if(students.containsKey(name)){
					return true;
				}
				return false;
			}
		}
	}
	public boolean checkUserNamePassword(int adminOrStudent, String userName, String password){
		if(adminOrStudent == 0){        //ADMIN
			if(admins.get(userName).equals(password)){
				return true;
			}
			return false;
		}
		else{					//STUDENT
			if(students.get(userName).equals(password)){
				return true;
			}
			return false;
		}
	}

	public void addAdmin(String userName, String password){
		admins.put(userName, password);
	}

	public void  addStudent(String userName, String password){
		students.put(userName,password);
		ArrayList<Integer> list = new ArrayList<Integer>();
		studentCourses.put(userName,list);
	}

	public boolean checkCapacityAndRegister(String studentName, String courseNumber){
		int intCourseNumber = Integer.parseInt(courseNumber);
		synchronized (lock1){
			if(seatsINCourse.get(intCourseNumber) > 0){
				studentCourses.get(studentName).add(intCourseNumber);        //ADD THE COURSE
				int newCapacity = seatsINCourse.get(intCourseNumber);       //UPDATE CAPACITY
				newCapacity = newCapacity - 1;
				seatsINCourse.put(intCourseNumber, newCapacity);
				return true;
			}
			return false;
		}
	}

	public boolean checkKdamCourses(String name, String number){
		int intNumber = Integer.parseInt(number);
		if(!courseKdam.containsKey(intNumber)){
			return false;
		}
		for(Integer s : courseKdam.get(intNumber)){
			if(!studentCourses.get(name).contains(s)){
				return false;
			}
		}
		return true;
	}

	public String getKdamCourses(String courseNumber){
		int intCourseNumber = Integer.parseInt(courseNumber);
		if(!courseKdam.containsKey(intCourseNumber)){
			return "THERE IS NO COURSE WITH THE SAME NAME";
		}
		String kdam = "[";
		for(Map.Entry<Integer, String> n : courseNumberAndName.entrySet()){
			if(courseKdam.get(intCourseNumber).contains(n.getKey())){
				kdam = kdam + n.getKey() + ", ";
			}
		}
		kdam = kdam.substring(0, kdam.length() - 2);
		kdam = kdam + "]";
		return kdam;
	}

	public String getCourseStat(String courseNumber){
		int intcourseNumber = Integer.parseInt(courseNumber);
		if(!courseKdam.containsKey(intcourseNumber)){
			return "THERE IS NO COURSE WITH THE SAME NUMBER";
		}
		String status = "Course : "  + courseNumberAndName.get(intcourseNumber) + "\n" + "Seats Available : " + seatsINCourse.get(intcourseNumber)
				+"/" + maximumSeatsInCourse.get(intcourseNumber) + "\n";
		status = status + "Students Registered:";
		status = status + " [";
		List<String> list = new ArrayList<String>();
		for(Map.Entry<String,String> s : students.entrySet()){
			list.add(s.getKey());
		}
		list.sort(Comparator.naturalOrder());
		for(String studentName : list){
			if(studentCourses.get(studentName).contains(intcourseNumber)){
				status = status + " " + studentName + ",";
			}
		}
		status = status.substring(0, status.length()- 1);
		status = status + "]";
		return status;
	}

	public boolean isStudentRegistered(String studentName, String courseNumber){
		int intCourseNumber = Integer.parseInt(courseNumber);
		if(!students.containsKey(studentName)){
			return false;
		}
		if(studentCourses.get(studentName).contains(intCourseNumber)){
			return true;
		}
		return false;
	}

	public boolean unregisterStudent(String studentName, String courseNumber){
		int intCourseNumber = Integer.parseInt(courseNumber);
		if(isStudentRegistered(studentName, courseNumber)){
			studentCourses.get(studentName).remove(intCourseNumber);
			int seats = seatsINCourse.get(intCourseNumber);
			seats = seats + 1;
			seatsINCourse.put(intCourseNumber,seats);
			return  true;
		}
		return false;
	}

	public String getMyCourses(String studentName){
		String result = "[ ";
		for(Map.Entry<Integer, String> course : courseNumberAndName.entrySet()){
			if(studentCourses.get(studentName).contains(course.getKey())){
				result = result + course.getKey() + ", ";
			}
		}
		result = result.substring(0,result.length() - 2);
		result = result + "]";
		return result;
	}

	private void settleCourses(){
		for(String line : lines){
			int index = line.indexOf('|');
			String courseNum = line.substring(0,index);
			line = line.substring(index + 1);
			index = line.indexOf('|');
			String courseName = line.substring(0,index);
			line = line.substring(index + 1);
			index = line.indexOf('|');
			String kdam = line.substring(0,index);
			ArrayList<Integer> kdams = getKdamsFromText(kdam);   //"[30,12]"
			String max = line.substring(index + 1);
			int maxNumberOfSeats = Integer.parseInt(max);
			int intCourseNum = Integer.parseInt(courseName);
			courseNumberAndName.put(intCourseNum, courseName);
			courseKdam.put(intCourseNum, kdams);
			maximumSeatsInCourse.put(intCourseNum, maxNumberOfSeats);
			seatsINCourse.put(intCourseNum, maxNumberOfSeats);
		}
	}

	private ArrayList<Integer> getKdamsFromText(String text){
		ArrayList<Integer> result = new ArrayList<Integer>();
		if(text.length() == 2){     //[]
			return result;
		}
		text = text.substring(1);
		text = text.substring(0,text.length() - 1);
		String course = "";
		int index = text.indexOf(',');
		while(index != -1){
			course = text.substring(0, index);
			int intCourse = Integer.parseInt(course);
			result.add(intCourse);
			text = text.substring(index + 1);
			index = text.indexOf(',');
		}
		int intText = Integer.parseInt(text);
		result.add(intText);
		return result;
	}
}
