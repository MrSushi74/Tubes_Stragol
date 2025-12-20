import java.util.*;
import java.util.stream.Collectors;

public class MusicianScheduling {
    private static class Schedule{
        int week;
        int instrumentIndex;
        int musicianListIndex;
        List<Musician> weeklyAssigned;

        public Schedule (int week, int instrumentIndex, int musicianListIndex, List<Musician> weeklyAssigned){
            this.week = week;
            this.instrumentIndex = instrumentIndex;
            this.musicianListIndex = musicianListIndex;
            this.weeklyAssigned = new ArrayList<>(weeklyAssigned);
        }
    }
    private List<Instruments> instruments;
    private List<Musician> musicianList;
    private Map<Musician, Integer> playCount = new HashMap<>();
    private int totalWeeks;
    private Musician[][] schedule;
    private boolean foundSchedule = false;

    private Stack<Schedule> scheduleStack = new Stack<>();
    int totalSolutions = 0;


    public MusicianScheduling(List<Instruments> instruments, List<Musician> musicianList, int totalWeeks) {
        this.instruments = instruments;
        this.musicianList = musicianList;
        this.totalWeeks = totalWeeks;
        this.schedule = new Musician[totalWeeks][instruments.size()];

        for (Musician m : musicianList) {
            playCount.put(m, 0);
        }

        scheduleStack.push(new Schedule(0,0,0,new ArrayList<>()));
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

    public boolean dfsSchedule() {
        if (scheduleStack.isEmpty()) {
            System.out.println("No more solutions available. Total solutions found: " + totalSolutions);
            return false;
        }

        while(!scheduleStack.isEmpty()) {
            Schedule currentSchedule = scheduleStack.peek();

            int week = currentSchedule.week;
            int index = currentSchedule.instrumentIndex;

            if (week == totalWeeks) {
                totalSolutions++;
                printSchedule();
                scheduleStack.pop();
                return true;
            }


            // kalo semua instrumen minggu ini beres, lanjut ke next week
            if (index == instruments.size()) {
                scheduleStack.pop();
                scheduleStack.push(new Schedule(week + 1, 0, 0, new ArrayList<>()));
                continue;
            }

            Instruments currentInstrument = instruments.get(index);

            // musisi dengan playCount dikit diprioritaskan
            List<Musician> prioritizedMusicians = musicianList.stream()
                    .sorted(Comparator.comparingInt(m -> playCount.get(m)))
                    .toList();

            boolean foundChoice = false;
            for (int i = currentSchedule.musicianListIndex; i < prioritizedMusicians.size(); i++) {
                Musician m = prioritizedMusicians.get(i);

                if (!currentSchedule.weeklyAssigned.contains(m) && m.canPlay(currentInstrument) && m.isAvailable(currentSchedule.week)) {
                    // Update index musisi buat backtracking nanti kalau balik ke state ini
                    currentSchedule.musicianListIndex = i + 1;

                    // Simpan ke jadwal
                    schedule[currentSchedule.week][currentSchedule.instrumentIndex] = m;
                    currentSchedule.weeklyAssigned.add(m);
                    playCount.put(m, playCount.get(m) + 1);

                    // Push state baru buat instrumen berikutnya
                    scheduleStack.push(new Schedule(currentSchedule.week, currentSchedule.instrumentIndex + 1, 0, currentSchedule.weeklyAssigned));
                    foundChoice = true;
                    break;
                }
            }

            // 4. Kalau gak ada musisi yang cocok (DEAD END), lakukan Backtrack
            if (!foundChoice) {
                scheduleStack.pop(); // Buang state buntu
                if (!scheduleStack.isEmpty()) {
                    Schedule prev = scheduleStack.peek();
                    // GUE TAMBAHIN INI: Lepas musisi dari slot sebelumnya
                    if (prev.instrumentIndex < instruments.size()) {
                        Musician lastM = schedule[prev.week][prev.instrumentIndex];
                        if (lastM != null) {
                            playCount.put(lastM, playCount.get(lastM) - 1); // Kurangin playCount lagi
                            prev.weeklyAssigned.remove(lastM);             // Hapus dari daftar minggu ini
                            schedule[prev.week][prev.instrumentIndex] = null; // Kosongin slot jadwal
                        }
                    }
                }
            }

        }
        System.out.println("No More Solutions! Total: " + totalSolutions);
        return false;
    }
}


