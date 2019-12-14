import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.AbsentInformationException;
public class ReplayCommand extends DebugCommand {
    @Override
    public Boolean executeOn(ScriptableDebugger dbg)
     {
        try {
            dbg.replay((LocatableEvent) getEvent());
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
