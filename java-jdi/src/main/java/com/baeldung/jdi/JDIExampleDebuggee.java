public class JDIExampleDebuggee {

    public static void main(String[] args) {
        String value1 = "Test";
		String value2 = "Test2";
		String value4 = doSomething(3);
    }
    public static String doSomething(int something){
		return "Something : " + something ; 
	}

}