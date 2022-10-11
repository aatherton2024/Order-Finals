/**
* Student is a class used to create students which consist of a name and four
* classes. This class is a supporting class for ExamScheduler.
*/
public class Student {

  /* Student's name */
	private String name;

  /* Student's 1st class */
	private String class1;

  /* Student's 2nd class */
  private String class2;

  /* Student's 3rd class */
  private String class3;

  /* Student's 4th class */
  private String class4;

	/**
	* Constructs a new student
	* @param theName is student name
	* @param c1 is class 1
	* @param c2 is class 2
  * @param c3 is class 3
  * @param c4 is class 4
	*/
	public Student(String theName, String c1, String c2, String c3, String c4) {
		name = theName;
		class1 = c1;
		class2 = c2;
    class3 = c3;
    class4 = c4;
	}

	/**
	* Get method for student name
	* @post: returns student name
	* @return student's name
	*/
	public String getName() {
		return name;
	}

  /**
	* Get method for student classes
	* @post: returns an array of the student's classes
	* @return array of student's classes
	*/
  public String[] getClasses() {
    String[] c = new String[] {class1, class2, class3, class4};
    return c;
  }

  /**
	* toString method for student class
	* @post: returns student name and classes
	* @return student's name and classes
	*/
  public String toString() {
    String[] c = getClasses();
    return getName() + ": " + c[0] + ", " + c[1] + ", " + c[2] + ", " + c[3];
  }

  /**
	* Main method for Student; used for testing purposes
	*/
	public static void main(String[] args) {
		Student c = new Student("alex", "1", "2", "3", "4");
		System.out.println(c);
	}
}
