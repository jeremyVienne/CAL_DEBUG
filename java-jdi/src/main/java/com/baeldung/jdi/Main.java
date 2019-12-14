
public class Main {

	public static String doSomething(int something){
		return "Something : " + something ; 
	}
	
	public static void main(String[] args) {
		String value1 = "Test";
		String value2 = "Test2";
		System.out.println(value1 + value2);
		String value4 = doSomething(3);

	}

}
