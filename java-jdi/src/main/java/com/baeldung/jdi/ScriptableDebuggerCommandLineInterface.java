import com.sun.jdi.event.Event;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class ScriptableDebuggerCommandLineInterface {

    private ScriptableDebugger dbg;
    private Scanner commandScanner;
    private Map<String, DebugCommand> commandMap;
    

    public ScriptableDebuggerCommandLineInterface(ScriptableDebugger controlledDebugger) {
        this.dbg = controlledDebugger;
        commandScanner = new Scanner(System.in);
        commandMap = new HashMap<String, DebugCommand>();
        commandMap.put("step", new StepDebugCommand());
        commandMap.put("stack", new PrintStackDebugCommand());
        commandMap.put("replay", new ReplayCommand()); 
        commandMap.put("replayoff", new ReplayOffCommand());
        commandMap.put("next", new NextCommand());
        commandMap.put("previous", new PreviousCommand());
        commandMap.put("arguments", new ArgumentsCommand());
        commandMap.put("breakpoints", new BreakPointsCommand());
        commandMap.put("method", new MethodCommand());
        commandMap.put("break", new SetBreakpointDebugCommand());
    }

    public void waitForInput(Event ev) {
        DebugCommand cmd = scanCommand(ev);
        if(!dbg.executeCommand(cmd))
          waitForInput(ev);
    }

    public DebugCommand scanCommand(Event ev) {
        System.out.println(ev.toString());
        String[] inputCommand = commandScanner.nextLine().split(" ");
        System.out.println(inputCommand.toString());
        DebugCommand cmd = getCommandFromInput(inputCommand[0]);
        System.out.println(cmd.toString());
        cmd.setParameters(inputCommand);
        cmd.setEvent(ev);
        return cmd;
    }

    private DebugCommand getCommandFromInput(String inputCommand) {
        return commandMap.containsKey(inputCommand) ? commandMap.get(inputCommand) : new NullDebugCommand();
    }
}
