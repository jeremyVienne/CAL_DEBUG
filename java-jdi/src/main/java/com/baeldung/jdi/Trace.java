import java.util.Map;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Value;
public class Trace{
    private Map<LocalVariable, Value> visibleVariables;
    private String className;
    private String methodName;
    private int ligne;
    
    public Trace(Map<LocalVariable, Value> variables, String cName, String mName, int line){ 

        this.className = cName;
        this.methodName = mName;
        this.visibleVariables = variables;
        this.ligne =  line;

    }
    
   public String getClassName() {
	   return this.className;
   }
   
   public String getMethodName() {
	   return this.methodName;
   }
   public int getLine() {
	   return this.ligne;
   }

   public Map<LocalVariable, Value> getvariables(){
       return this.visibleVariables;
   }

   public void display(){
       System.out.println("=========================================");
        System.out.println("Line number : " + this.ligne);
        System.out.println("Method name : " + this.methodName);
        System.out.println("Class Name : " + this.className);
        for (Map.Entry<LocalVariable, Value> entry : this.visibleVariables.entrySet()) {
            System.out.println(entry.getKey().name() + " = " + entry.getValue());
        }
        System.out.println("=========================================");
   }
}