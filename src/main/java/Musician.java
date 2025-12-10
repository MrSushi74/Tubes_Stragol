import java.util.ArrayList;
import java.util.List;

public class Musician {
    private String name;
    private List<Instruments> playableInstruments;
    private List<Integer>  unavailableWeeks;

    public Musician(String name, List<Instruments> playableInstruments, List<Integer> unavailableWeeks){
        this.name = name;
        this.playableInstruments = playableInstruments;
        this.unavailableWeeks = unavailableWeeks;
    }

    public String getName(){
        return this.name;
    }

    public List<Instruments> getPlayableInstrument(){
        return this.playableInstruments;
    }

    public List<Integer> getUnavailableWeeks(){
        return this.unavailableWeeks;
    }

    boolean canPlay(Instruments instrument) {
        return playableInstruments.contains(instrument);
    }

    boolean isAvailable(int week){
        return !unavailableWeeks.contains(week);
    }

    boolean

}
