import java.util.ArrayList;
import java.util.List;

public class Musician {
    String name;
    List<Instruments> playableInstruments;
    List<Integer>  unavailableWeeks;

    public Musician(String name, List<Instruments> playableInstruments, List<Integer> unavailableWeeks){
        this.name = name;
        this.playableInstruments = playableInstruments;
        this.unavailableWeeks = unavailableWeeks;
    }

    boolean canPlay (Instruments instrument) {
        return playableInstruments.contains(instrument);
    }

    boolean isAvailable(int week){
        return !unavailableWeeks.contains(week);
    }

    public String getName() {
        return this.name;
    }


}
