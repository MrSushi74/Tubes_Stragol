//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class MusicianScheduling {
//    private List<Instruments> instruments;
//    private List<Musician> musicianList;
////    private Map<Musician, Integer> playCount = new HashMap<>();
//    private int totalWeeks;
//    private Musician[][] schedule;
//    private boolean needsAlt;
//    private boolean foundSchedule = false;
//
//    public MusicianScheduling(List<Instruments> instruments, List<Musician> musicianList, int totalWeeks){
//        this.instruments = instruments;
//        this.musicianList = musicianList;
//        this.totalWeeks = totalWeeks;
//        this.schedule = new Musician[totalWeeks][instruments.size()];
//    }
//    public void generateSchedule(){
//        // set ulang penanda solusi ditemukan
//        foundSchedule = false;
//        dfs(0, 0, new ArrayList<Musician>());
//        if (!foundSchedule) {
//            System.out.println("No valid schedule found for the given constraints.");
//        }
//    }
//
//    private void printSchedule(){
//        for (int i = 0; i < totalWeeks; i++) {
//            System.out.print("Week : " + i + " : ");
//            for (int j = 0; j < instruments.size(); j++) {
//                System.out.print(schedule[i][j].getName() + " ");
//            }
//            System.out.println();
//        }
//    }
//    private void dfs (int week, int index, List<Musician> weeklyAssigned){
//        // selesai untuk semua minggu
//        if (foundSchedule) return; // berheti jika udh nemu
//
//        if (week == totalWeeks){
//            printSchedule();
//            foundSchedule = true;
//            return;
//        }
//
//        // jika semua instrumen pada minggu ini sudah ditugaskan, lanjut ke minggu berikutnya
//        if (index == instruments.size()){
//            dfs(week + 1, 0, new ArrayList<Musician>());
//            return;
//        }
//
//        Instruments instrument = instruments.get(index);
//
//        for (Musician m : musicianList){
//            if (!weeklyAssigned.contains(m) && m.canPlay(instrument) && m.isAvailable(week)) {
//
//
//                // tetapkan
//                schedule[week][index] = m;
//                weeklyAssigned.add(m);
//
//                // rekursif ke slot berikutnya
//                dfs(week, index + 1, weeklyAssigned);
//                if (foundSchedule) return;
//
//                // mundur (backtrack)
//                weeklyAssigned.remove(weeklyAssigned.size() - 1);
//                schedule[week][index] = null;
//            } else {
//                Optional: System.out.println("Cannot assign " + m.getName() + " to " + instrument);
//            }
//        }
//    }
//
//
//}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

public class MusicianScheduling {
    private List<Instruments> instruments;
    private List<Musician> musicianList;
    // Map untuk mencatat berapa kali musisi sudah bermain (Kriteria No. 6)
    private Map<Musician, Integer> playCount = new HashMap<>();
    private int totalWeeks;
    private Musician[][] schedule;
    private boolean foundSchedule = false;

    public MusicianScheduling(List<Instruments> instruments, List<Musician> musicianList, int totalWeeks) {
        this.instruments = instruments;
        this.musicianList = musicianList;
        this.totalWeeks = totalWeeks;
        this.schedule = new Musician[totalWeeks][instruments.size()];

        // Inisialisasi hitungan bermain untuk setiap musisi
        for (Musician m : musicianList) {
            playCount.put(m, 0);
        }
    }

    public void generateSchedule() {
        foundSchedule = false;
        // Menggunakan list untuk melacak musisi yang ditugaskan per minggu
        dfs(0, 0, new ArrayList<Musician>());
        if (!foundSchedule) {
            System.out.println("No valid schedule found for the given constraints.");
        }
    }

    private void printSchedule() {
        System.out.println("\n" + "=".repeat(20 + (totalWeeks * 15)));

        // Header baris pertama: Menampilkan nomor minggu ke arah kanan
        System.out.printf("%-15s |", "INSTRUMEN");
        for (int w = 0; w < totalWeeks; w++) {
            System.out.printf(" Minggu %-6d |", (w + 1));
        }
        System.out.println("\n" + "-".repeat(20 + (totalWeeks * 15)));

        // Baris berikutnya: Menampilkan Instrumen di kiri, lalu nama pemain ke kanan
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

        // Base case: Jika semua minggu selesai
        if (week == totalWeeks) {
            printSchedule();
            foundSchedule = true;
            return;
        }

        // Jika semua instrumen minggu ini selesai, lanjut ke minggu berikutnya
        if (index == instruments.size()) {
            dfs(week + 1, 0, new ArrayList<Musician>());
            return;
        }

        Instruments currentInstrument = instruments.get(index);

        // KRITERIA NO 6: Optimasi pemerataan.
        // Kita mengurutkan musisi berdasarkan jumlah bermain tersedikit (Greedy/Brute Force hybrid)
        // Musisi dengan playCount rendah akan diprioritaskan untuk dipilih.
        List<Musician> prioritizedMusicians = musicianList.stream()
                .sorted(Comparator.comparingInt(m -> playCount.get(m)))
                .collect(Collectors.toList());

        for (Musician m : prioritizedMusicians) {
            // Cek: Belum main di minggu ini AND bisa alat musiknya AND tersedia jadwalnya
            if (!weeklyAssigned.contains(m) && m.canPlay(currentInstrument) && m.isAvailable(week)) {

                // Tetapkan musisi
                schedule[week][index] = m;
                weeklyAssigned.add(m);
                playCount.put(m, playCount.get(m) + 1); // Tambah hitungan main

                // Rekursif ke slot instrumen berikutnya
                dfs(week, index + 1, weeklyAssigned);

                if (foundSchedule) return;

                // Backtrack: Kembalikan keadaan jika solusi tidak ditemukan di jalur ini
                playCount.put(m, playCount.get(m) - 1);
                weeklyAssigned.remove(weeklyAssigned.size() - 1);
                schedule[week][index] = null;
            }
        }
    }
}
