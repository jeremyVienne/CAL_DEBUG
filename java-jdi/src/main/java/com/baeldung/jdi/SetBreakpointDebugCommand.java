import com.sun.jdi.AbsentInformationException;

public class SetBreakpointDebugCommand extends DebugCommand {
    @Override
    public Boolean executeOn(ScriptableDebugger dbg) {
        try {
            dbg.setBreakPoint(getParameters()[2], Integer.parseInt(getParameters()[1]));
            return false;
        } catch (AbsentInformationException e) {
            e.printStackTrace();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
      return false;
    }
}
