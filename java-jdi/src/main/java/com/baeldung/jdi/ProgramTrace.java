import java.util.ArrayList;
public class ProgramTrace{

    private static final ProgramTrace pgm = new ProgramTrace();
    private ArrayList<Trace> traces;
    private int index;
    private ProgramTrace(){
        this.traces = new ArrayList<Trace>();
        this.index = 0;
    }
    public static ProgramTrace getPgm(){
        return pgm;
    }

    public void addTrace(Trace t ){
        this.index ++;
        this.traces.add(t);
    }


    public Trace next(){
        if(this.index < this.traces.size()-1){this.index ++ ;}
        return this.traces.get(this.index);
    }

    public Trace previous(){
        if(this.index > 0){
            this.index --;
        }
        return this.traces.get(this.index);
    } 
}