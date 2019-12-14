import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.AbsentInformationException;
public class NextCommand extends DebugCommand {
    @Override
    public Boolean executeOn(ScriptableDebugger dbg)
     {
        try {
            dbg.next((LocatableEvent) getEvent());
            return false;
        }
        catch(IncompatibleThreadStateException e1) {
            return false ;
        }
        catch(AbsentInformationException e2){
            return false; 
        }
        
    }
}
