import java.util.ArrayList;
import java.util.List;

public class MusicianScheduling {
    private List<Instruments> instruments;
    private List<Musician> musicianList;
    private int totalWeeks;
    private Musician[][][] schedule;

    public MusicianScheduling(List<Instruments> instruments, List<Musician> musicianList, int totalWeeks){
        this.instruments = instruments;
        this.musicianList = musicianList;
        this.totalWeeks = totalWeeks;
        this.schedule = new Musician[totalWeeks][7][instruments.size()];
    }
}
