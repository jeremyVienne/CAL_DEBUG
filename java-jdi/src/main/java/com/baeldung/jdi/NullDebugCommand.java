public class NullDebugCommand extends DebugCommand {

    @Override
    public Boolean executeOn(ScriptableDebugger dbg) {
        dbg.unkownCommandRequest(getParameters()[0]);
	return false;
    }
}
