import structure5.*;
import java.util.Scanner;
import java.util.Random;

/**
* Class to schedule finals times of students so that no student has multiple
* finals scheduled for the same time slot. Additionally, the number of slots of
* finals offered is as small as possible (given first selected course).
*/
public class SetFinals {

  /* Graph of student's classes */
  private GraphListUndirected<String, Integer> g;

  /* Holds slots and finals in each slot */
  private Vector<Vector<String>> vec;

  /* Stores all courses */
  private Vector<Association<String, String>> courses;

  /* Keeps track of current slot number */
  private Integer timeSlot;

  /* Vector to hold all students */
  private Vector<Student> students;

  /**
  * Constructor for SetFinals
  * Creates an empty graph g which stores relationships between class enrollment
  * Creates a vector of vectors vec which stores finals in their slots
  * Creates a vector courses which stores the name of all of the courses
  * @post calls clearAll, which creates empty instance variables
  */
  public SetFinals() {
    clearAll();
  }

  /**
  * Method to scan file of student information into a graph
  * @pre students are only in 4 classes, file is properly formatted
  * @post returns graph w/ classes as vertices and edges as enrollment relation
  * @return a graph with classes as vertices and edges as enrollment relation
  */
  public GraphListUndirected<String, Integer> studentScanner() {
    for (Student stu : students) {
      graphGenerator(stu);
    }
    return g;
  }

  /**
  * Helper method to scan students from input file into students instance var
  * @pre: input file has students with exactly four classes
  * @pre: student names and classes are on separate lines
  * @post: fills students with all students in file
  */
  private void studentScanHelper() {
    students = new Vector<Student>();
    Scanner s = new Scanner(System.in);
    while (s.hasNext()) {
      String name = s.nextLine();
      String class1 = s.nextLine();
      String class2 = s.nextLine();
      String class3 = s.nextLine();
      String class4 = s.nextLine();
      Student stu = new Student(name, class1, class2, class3, class4);
      students.add(stu);
    }
  }

  /**
  * Helper method for studentScanner
  * Takes a student and adds their classes to graph
  * @param s the student to be added to graph
  * @post adds classes and their enrollment relationships to graph
  */
  private void graphGenerator(Student s) {
    String[] classes = s.getClasses();
    for (int i = 0; i < classes.length; i++) {
      if (!g.contains(classes[i])) {
        g.add(classes[i]);
        Association<String, String> c = new Association<String, String>(classes[i], s.getName());
        courses.add(c);
      } else {
        for (int j = 0; j < courses.size(); j++) {
          if (classes[i].equals(courses.elementAt(j).getKey())) {
            String old = courses.elementAt(j).getValue();
            courses.elementAt(j).setValue(old + " " + s.getName());
          }
        }
      }
    }
    ggHelper(classes);
  }

  /**
  * Helper method for graphGenerator
  * Adds edges for each added class
  * @param classes an array of 4 classes
  * @post adds edges between the 4 classes
  */
  private void ggHelper(String[] classes) {
    g.addEdge(classes[0], classes[1], 1);
    g.addEdge(classes[0], classes[2], 2);
    g.addEdge(classes[0], classes[3], 3);
    g.addEdge(classes[1], classes[2], 4);
    g.addEdge(classes[1], classes[3], 5);
    g.addEdge(classes[2], classes[3], 6);
  }

  /**
  * Helper method to get graph instance variable
  * Used primarily for testing
  * @post returns g
  * @return g
  */
  private GraphListUndirected<String, Integer> getGraph() {
    return g;
  }

  /**
  * Helper method to get vec instance variable
  * @post returns vec
  * @return vec
  */
  private Vector<Vector<String>> getVec() {
    return vec;
  }

  /**
  * Helper method to get courses instance variable
  * Used primarily for testing
  * @post returns courses
  * @return courses
  */
  private Vector<Association<String, String>> getCourses() {
    return courses;
  }

  /**
  * Recursive greedy algorithm generates slots for exam schedule
  * @post return vec, a vector of vectors where nested vectors are finals in
  * a specific time slot
  * @post no student has finals at same time, solution cannot be simplified
  * given starting class
  * @return a vector of vectors where nested vectors are finals in a specific
  * time slot
  */
  public Vector<Vector<String>> alg() {
    //base case
    if (g.isEmpty()) {
      return vec;
    }
    //gets our first class and removes from structure
    Vector<String> slot = new Vector<String>();
    String chosen = chooseClass(slot);

    //we are now looking at all the class in g
    //want to compare them to the elements in our slot
    for (String class1 : g) {
      if (class1 != chosen && algHelper(class1, slot)) {
        slot.add(class1);
      }
    }
    //now all possible classes are in this slot
    //we now have to remove classes from graph
    for (int i = 0; i < slot.size(); i++) {
      extensionSlotHelper(slot.elementAt(i));
      g.remove(slot.elementAt(i));
    }
    vec.add(slot);
    timeSlot++;
    return alg();
  }

  /**
  * Helper method to randomly select a class from graph
  * @post: selects a class and adds to a time slot
  * @return a class that is added to a time slot
  */
  private String chooseClass(Vector<String> slot) {
    Random rand = new Random();
    int i = rand.nextInt(g.size());
    int iter = 0;
    String chosen = "";
    for (String first : g) {
      if (iter == i) {
        chosen = first;
        slot.add(chosen);
        break;
      }
      iter++;
    }
    return chosen;
  }

  /**
  * Helper method for alg
  * Checks if class1 shares and edge with other classes in this time slot
  * @param class1 a class to potentially be added to this slot
  * @param slot a vector that stores finals in a specific time slot
  * @post returns true if class can be added to slot, else false
  * @return boolean indicating if class can be added to slot
  */
  private boolean algHelper(String class1, Vector<String> slot) {
    for (int i = 0; i < slot.size(); i++) {
      if (g.containsEdge(class1, slot.elementAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
  * Method to print out exam slots and the finals scheduled in them
  * @param v the vector containing slots to be printed
  * @post prints out every exam slot and the finals in that slot
  */
  public void printSlots(Vector<Vector<String>> v) {
    for (int i = 0; i < v.size(); i++) {
      String str = "";
      for (int j = 0; j < v.elementAt(i).size(); j++) {
        str += v.elementAt(i).elementAt(j) + " ";
      }
      System.out.println("Slot " + (i + 1) + ": " + str);
    }
  }

  /**
  * Method to print exam schedule in alphabetical order
  * Each printed class should have its slot and enrolled students
  * @post: prints out classes, slots, and enrolled students
  */
  private void extensionSlotHelper(String class1) {
    for (Association<String, String> c : courses) {
      if (class1.equals(c.getKey())) {
        String old = c.getValue();
        c.setValue("Slot " + timeSlot + ": " + old);
        break;
      }
    }
  }

  /**
  * Insertion sort method used to order associations in courses alphabetically
  * by key value (class name)
  * @post: orders classes in courses data structure alphabetically
  */
  private void sortHelper() {
    int numSorted = 1; // number of values in place
    while (numSorted < courses.size()) {
      Association<String, String> temp = courses.get(numSorted); // first unsorted value
      int index = numSorted;
      while (index > 0 && temp.getKey().compareTo(courses.get(index - 1).getKey()) < 0) {
        courses.set(index, courses.get(index - 1));
        index--;
      }
      courses.set(index, temp);
      numSorted++;
    }
  }

  /**
  * Method to print exam schedule in alphabetical order
  * Each printed class should have its slot and enrolled students
  * @post: prints out classes, slots, and enrolled students
  */
  public void printAlphaBonusSolution() {
    sortHelper();
    System.out.println("Here is the exam schedule alphabetized by class name:");
    for (Association<String, String> class1 : courses) {
      System.out.println(class1.getKey() + ": " + class1.getValue());
    }
  }

  /**
  * Method to print best and worst random solutions to exam slots
  * @pre: numTests non-negative
  * @param numTests the number of tests to be performed
  * @param low is our smallest solution so far
  * @param high is our highest solution so far
  * @post: prints out classes, slots, and enrolled students
  */
  public void printRandomSolution(int numTests,
  Vector<Vector<String>> low, Vector<Vector<String>> high) {
    Assert.pre(numTests >= 0, "Input a positive number of tests");
    //base case
    if (numTests == 0) {
      System.out.println("Lowest solution has " + low.size() + " slots:");
      printSlots(low);
      System.out.println("Highest solution has " + high.size() + " slots:");
      printSlots(high);

    } else {
      //reset and scan and run algorithm
      clearAll();
      studentScanner();
      alg();
      //we either update lowest or highest or don't on recursive call
      if (timeSlot < low.size()) {
        printRandomSolution(numTests - 1, vec, high);
      } else if (timeSlot > high.size()) {
        printRandomSolution(numTests - 1, low, vec);
      } else {
        printRandomSolution(numTests - 1, low, high);
      }
    }
  }

  /**
  * Helper method for constructor and printRandomSolution
  * Resets all data structures
  * @post: all data structures are empty
  */
  private void clearAll() {
    g = new GraphListUndirected<String, Integer>();
    vec = new Vector<Vector<String>>();
    courses = new Vector<Association<String, String>>();
    timeSlot = 1;
  }

  /**
  * Main method for SetFinals
  * Can take command line argument to find optimal random solution
  * To find random solutions: $ java SetFinals #ofTests < filename
  * To find alphabetized catalog: $ java SetFinals < filename
  * If no command line argument, will run non random portions of the lab
  *
  * @post reads in file, generates graph, generates slots, prints out slots
  * and prints courses in alpha order
  *
  * @post if command line #tests argument, will print slots and then print out
  * high and low random solutions
  */
  public static void main(String[] args) {
    //print out slots and courses in them
    SetFinals sched = new SetFinals();
    sched.studentScanHelper();
    sched.studentScanner();
    sched.alg();
    sched.printSlots(sched.getVec());

    //alphabetized catalog
    if (args.length == 0) {
      sched.printAlphaBonusSolution();

      //random optimal and nonoptimal solution
    } else {
      int k = Integer.parseInt(args[0]);
      sched.printRandomSolution(k, sched.getVec(), sched.getVec());
    }
  }
}
