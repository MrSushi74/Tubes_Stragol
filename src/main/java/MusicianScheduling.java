import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

public class MusicianScheduling {
    private List<Instruments> instruments;
    private List<Musician> musicianList;
    private Map<Musician, Integer> playCount = new HashMap<>();
    private int totalWeeks;
    private Musician[][] schedule;
    private boolean foundSchedule = false;
    int totalSolutions = 0;

    public MusicianScheduling(List<Instruments> instruments, List<Musician> musicianList, int totalWeeks) {
        this.instruments = instruments;
        this.musicianList = musicianList;
        this.totalWeeks = totalWeeks;
        this.schedule = new Musician[totalWeeks][instruments.size()];

        for (Musician m : musicianList) {
            playCount.put(m, 0);
        }
    }

    public void generateSchedule() {
        foundSchedule = false;
        totalSolutions = 0;
        dfs(0, 0, new ArrayList<Musician>());
        if (totalSolutions == 0){
            System.out.println("No valid solutions found");
        }
        else if (!foundSchedule) {
            System.out.println("No more solutions, total solutions : " + totalSolutions );
        }
    }

    private void printSchedule() {
        System.out.println("\n" + "=".repeat(20 + (totalWeeks * 15)));

        System.out.printf("%-15s |", "INSTRUMEN");
        for (int w = 0; w < totalWeeks; w++) {
            System.out.printf(" Minggu %-6d |", (w + 1));
        }
        System.out.println("\n" + "-".repeat(20 + (totalWeeks * 15)));

        for (int i = 0; i < instruments.size(); i++) {
            String instrumentName = instruments.get(i).toString();
            System.out.printf("%-15s |", instrumentName);

            for (int w = 0; w < totalWeeks; w++) {
                String musicianName = (schedule[w][i] != null) ? schedule[w][i].getName() : "-";
                System.out.printf(" %-13s |", musicianName);
            }
            System.out.println();
        }

        System.out.println("=".repeat(20 + (totalWeeks * 15)) + "\n");
    }

    private void dfs(int week, int index, List<Musician> weeklyAssigned) {
        if (foundSchedule) return;

        // kalo semua minggu beres diisi
        if (week == totalWeeks) {
            this.totalSolutions++;
            printSchedule();
            // On Demand BackTracking for solution
            System.out.print("Find another Solution? Current Total Solutions : " + totalSolutions + " (y/n): ");
            String choice = Utils.getString().trim().toLowerCase();
            if (choice.equals("n")) {
                foundSchedule = true;
            }

            return;
        }

        // kalo semua instrumen minggu ini beres, lanjut ke next week
        if (index == instruments.size()) {
            dfs(week + 1, 0, new ArrayList<Musician>());
            return;
        }

        Instruments currentInstrument = instruments.get(index);

        // musisi dengan playCount dikit diprioritaskan
        List<Musician> prioritizedMusicians = musicianList.stream()
                .sorted(Comparator.comparingInt(m -> playCount.get(m)))
                .toList();

        for (Musician m : prioritizedMusicians) {
            // Cek: kalo musisi belum main & bisa instrumennya & jadwal ga libur
            if (!weeklyAssigned.contains(m) && m.canPlay(currentInstrument) && m.isAvailable(week)) {

                // jadwalin musisinya
                schedule[week][index] = m;
                weeklyAssigned.add(m);
                playCount.put(m, playCount.get(m) + 1);

                // Rekursif ke next slot instrumen
                dfs(week, index + 1, weeklyAssigned);

                if (foundSchedule) return; // kalo direkursif ternyata scheduleFound, dia akan return trs

                // Backtrack: remove jadwal musisi sebelumnya dan dikosongkan lagi untuk diisi nanti
                playCount.put(m, playCount.get(m) - 1);
                weeklyAssigned.remove(weeklyAssigned.size() - 1);
                schedule[week][index] = null;
            }
        }
    }
}
