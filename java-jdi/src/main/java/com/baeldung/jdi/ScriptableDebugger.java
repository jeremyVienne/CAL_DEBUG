import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.StepRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.AbsentInformationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScriptableDebugger {

    private Class debugClass;
    private ArrayList<Integer> breakPointLines;

    private ArrayList<Integer> breakPointOnceLines;
    private VirtualMachine vm;
    private ScriptableDebuggerCommandLineInterface commandLineInterface;
    private static ProgramTrace pgm = ProgramTrace.getPgm();
    public static Boolean traceMode = false; 

    public void addBreakPointAtLine(int lineNumber) {
        this.breakPointLines.add(lineNumber);
    }

    /**
     * Sets the debug class as the main argument in the connector and launches the VM
     *
     * @return VirtualMachine
     * @throws IOException
     * @throws IllegalConnectorArgumentsException
     * @throws VMStartException
     */
    public VirtualMachine connectAndLaunchVM() throws IOException, IllegalConnectorArgumentsException, VMStartException {
        LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager().defaultConnector();
        Map<String, Connector.Argument> arguments = launchingConnector.defaultArguments();
        arguments.get("main").setValue("JDIExampleDebuggee");//debugClass.getName());
        VirtualMachine vm = launchingConnector.launch(arguments);
        return vm;
    }

    /**
     * Creates a request to prepare the debug class, add filter as the debug class and enables it
     *
     * @param vm
     */
    public void enableClassPrepareRequest(VirtualMachine vm) {
        ClassPrepareRequest classPrepareRequest = vm.eventRequestManager().createClassPrepareRequest();
        System.out.println(debugClass.getName());
        classPrepareRequest.addClassFilter(debugClass.getName());
        classPrepareRequest.enable();
    }

    /***********
     * DEBUGGER CONTROL INTERFACE
     * /***********

     /***
     *
     * @param lineNumber
     * @param className
     * @throws AbsentInformationException
     */

    public void setBreakPoint(String className, int lineNumber) throws AbsentInformationException {
        for (ReferenceType targetClass : vm.allClasses())
            if (targetClass.name().equals(className)) {
                Location location = targetClass.locationsOfLine(lineNumber).get(0);
                System.out.println("Setting breakpoint on: " + location.toString());
                addBreakPointAtLine(lineNumber);
                BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
                bpReq.enable();
            }
    }

    /**
     * Enables step request for a break point
     *
     * @param event
     */
    public void enableStepRequest(LocatableEvent event) throws IncompatibleThreadStateException, AbsentInformationException {
        if(!traceMode){

        StepRequest stepRequest = vm.eventRequestManager().createStepRequest(event.thread(), StepRequest.STEP_MIN, StepRequest.STEP_INTO);
        StackFrame stackFrame = event.thread().frame(0);
        if(stackFrame.location().toString().contains(debugClass.getName())) {
            if(this.breakPointOnceLines.contains(stackFrame.location().lineNumber())){
                event.request().disable();
                this.breakPointOnceLines.remove(this.breakPointOnceLines.indexOf(stackFrame.location().lineNumber()));
                this.breakPointLines.remove(this.breakPointLines.indexOf(stackFrame.location().lineNumber()));
            }
            Map<LocalVariable, Value> visibleVariables = stackFrame.getValues(stackFrame.visibleVariables());
            String cName = stackFrame.location().declaringType().toString(); 
            String mName = stackFrame.location().method().toString();
            int line = stackFrame.location().lineNumber();
            Trace t = new Trace(visibleVariables,cName, mName, line  );
            pgm.addTrace(t);
            stepRequest.enable();
            

        }  
        }
        else{ 
            throw  new IncompatibleThreadStateException();
        }
        
    }

    public void printStack(LocatableEvent event)throws IncompatibleThreadStateException, AbsentInformationException {

        System.out.println(event.thread().toString());
        StackFrame stackFrame = event.thread().frame(0);
        if(stackFrame.location().toString().contains(debugClass.getName())) {
            Map<LocalVariable, Value> visibleVariables = stackFrame.getValues(stackFrame.visibleVariables());
            System.out.println("Variables at " +stackFrame.location().toString() +  " > ");
            System.out.println("Line number : " + stackFrame.location().lineNumber());
            System.out.println("Method name : " + stackFrame.location().method().toString());
            System.out.println("Class Name : " + stackFrame.location().declaringType());
            for (Map.Entry<LocalVariable, Value> entry : visibleVariables.entrySet()) {
                System.out.println(entry.getKey().name() + " = " + entry.getValue());
            }
        }
    }

    public void next(LocatableEvent event)throws IncompatibleThreadStateException, AbsentInformationException {
        if(traceMode){

          pgm.next().display();
        }
    }
    public void previous(LocatableEvent event)throws IncompatibleThreadStateException, AbsentInformationException {
        if(traceMode){
            pgm.previous().display();
        }
        
    }
    public void method(LocatableEvent event)throws IncompatibleThreadStateException, AbsentInformationException {
        StackFrame stackFrame = event.thread().frame(0);
        if(stackFrame.location().toString().contains(debugClass.getName())) {
            System.out.println("=========================================");
            System.out.println("Method name : " + stackFrame.location().method().toString());
            System.out.println("=========================================");
        }
        
    }

    public void breakpoints(LocatableEvent event)throws IncompatibleThreadStateException, AbsentInformationException {
        System.out.println("=========================================");
        int inc = 1;
        for(int i : breakPointLines){
            System.out.println("breakpoint " + inc + " : line " + i);
            inc ++;
        }   
        System.out.println("=========================================");
        
    }

    public void replayOff(LocatableEvent event)throws IncompatibleThreadStateException, AbsentInformationException {
        traceMode = false; 
        System.out.println("Test1");
    }

    public void replay(LocatableEvent event)throws IncompatibleThreadStateException, AbsentInformationException {
        traceMode = true; 
        System.out.println("Test2");
    }


    public void arguments(LocatableEvent event)throws IncompatibleThreadStateException, AbsentInformationException, ClassNotLoadedException{
        StackFrame stackFrame = event.thread().frame(0);
        if(stackFrame.location().toString().contains(debugClass.getName())) {
            List<LocalVariable> argum = stackFrame.location().method().arguments();
            System.out.println("=========================================");
            System.out.println("Method name : " + stackFrame.location().method().toString());
            for(LocalVariable l : argum){
                System.out.println("args : " + l.name() + " : " + l.type());
            }
            System.out.println("=========================================");
        }
    }

    public void unkownCommandRequest(String commandName) {
        System.out.println("Error: unknown command " + commandName);
    }

    public Boolean executeCommand(DebugCommand cmd) {
        return cmd.executeOn(this);
    }

    /***********
     * DEBUGGER CREATION
     * ***********/

    public void attachTo(Class debuggeeClass, int attachPoint) {
        this.debugClass = debuggeeClass;
        this.breakPointLines = new ArrayList<Integer>();
        this.breakPointOnceLines = new ArrayList<Integer>();

        try {
            vm = connectAndLaunchVM();

            enableClassPrepareRequest(vm);

            startDebugger();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalConnectorArgumentsException e) {
            e.printStackTrace();
        } catch (VMStartException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } catch (VMDisconnectedException e) {
            e.printStackTrace();
            System.out.println("Virtual Machine is disconnected: " + e.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startDebugger() throws VMDisconnectedException, InterruptedException {
        commandLineInterface = new ScriptableDebuggerCommandLineInterface(this);
        EventSet eventSet = null;
        while ((eventSet = vm.eventQueue().remove()) != null) {
            System.out.println(eventSet.toString());
            for (Event event : eventSet) {
                if (event instanceof ClassPrepareEvent) {
                    SetBreakpointDebugCommand setBPCommand = new SetBreakpointDebugCommand();
                    setBPCommand.setParameters(new String[]{"break", "4", "JDIExampleDebuggee"});
                    executeCommand(setBPCommand);
                }

                if (event instanceof BreakpointEvent) {
                    commandLineInterface.waitForInput(event);
                }

                if (event instanceof StepEvent) {
                    event.request().disable();
                    System.out.println("Stopped at: " + ((StepEvent) event).location().toString());
                    commandLineInterface.waitForInput(event);
                }
                vm.resume();
            }
        }
    }

}
