import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ClassNotLoadedException;
public class ArgumentsCommand extends DebugCommand {
    @Override
    public Boolean executeOn(ScriptableDebugger dbg)
     {
        try {
            dbg.arguments((LocatableEvent) getEvent());
            return false;
        }
        catch(IncompatibleThreadStateException e1) {
            return false ;
        }
        catch(AbsentInformationException e2){
            return false; 
        }
        catch(ClassNotLoadedException e3){
            return false; 
        }
    }
}
