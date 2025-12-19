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
    private boolean foundSchedule = false;

    public MusicianScheduling(List<Instruments> instruments, List<Musician> musicianList, int totalWeeks){
        this.instruments = instruments;
        this.musicianList = musicianList;
        this.totalWeeks = totalWeeks;
        this.schedule = new Musician[totalWeeks][instruments.size()];
    }
    public void generateSchedule(){
        // set ulang penanda solusi ditemukan
        foundSchedule = false;
        dfs(0, 0, new ArrayList<Musician>());
        if (!foundSchedule) {
            System.out.println("No valid schedule found for the given constraints.");
        }
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
        // selesai untuk semua minggu
        if (foundSchedule) return; // berheti jika udh nemu

        if (week == totalWeeks){
            printSchedule();
            foundSchedule = true;
            return;
        }

        // jika semua instrumen pada minggu ini sudah ditugaskan, lanjut ke minggu berikutnya
        if (index == instruments.size()){
            dfs(week + 1, 0, new ArrayList<Musician>());
            return;
        }

        Instruments instrument = instruments.get(index);

        for (Musician m : musicianList){
            if (!weeklyAssigned.contains(m) && m.canPlay(instrument) && m.isAvailable(week)) {
                // tetapkan
                schedule[week][index] = m;
                weeklyAssigned.add(m);
                
                // rekursif ke slot berikutnya
                dfs(week, index + 1, weeklyAssigned);
                if (foundSchedule) return;
                
                // mundur (backtrack)
                weeklyAssigned.remove(weeklyAssigned.size() - 1);
                schedule[week][index] = null;
            }
        }
    }


}
