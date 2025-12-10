import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicianScheduling {
    private List<Instruments> instruments;
    private List<Musician> musicianList;
//    private Map<Musician, Integer> playCount = new HashMap<>();
    private int totalWeeks;
    private Musician[][] schedule;
    private boolean needsAlt;

    public MusicianScheduling(List<Instruments> instruments, List<Musician> musicianList, int totalWeeks){
        this.instruments = instruments;
        this.musicianList = musicianList;
        this.totalWeeks = totalWeeks;
        this.schedule = new Musician[totalWeeks][instruments.size()];
    }
    public void generateSchedule(){
        dfs(0, 0, new ArrayList<Musician>());
    }

    private void printSchedule(){
        for (int i = 0; i < totalWeeks; i++) {
            System.out.print("Week : " + i + " : ");
            for (int j = 0; j < instruments.size(); j++) {
                System.out.print(schedule[i][j].getName() + " ");
            }
            System.out.println();
        }
    }
    private void dfs (int week, int index, List<Musician> weeklyAssigned){
        //finish all week
        if (week == totalWeeks){
            printSchedule();
            return;
        }

        if(index == instruments.size()){
            for (int i = 0; i < instruments.size(); i++) {

            }
        }
    }


}
