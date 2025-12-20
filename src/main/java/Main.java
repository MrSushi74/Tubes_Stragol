import java.util.*;

public class Main {
    public static void main(String[] args) {
// 1. Define required instruments
        List<Instruments> requiredInstruments = Arrays.asList(
                Instruments.GUITAR,
                Instruments.BASS,
                Instruments.PIANO,
                Instruments.DRUMS
        );

        while (true) {
            // 2. Menu to choose Test Case
            System.out.println("\n=== PENJADWALAN MUSISI ===");
            System.out.println("1. Test Case 1 (A-I Mixed)");
            System.out.println("2. Test Case 2 (A-H Specific)");
            System.out.println("3. Test Case 3 (Double Instruments)");
            System.out.println("0. Exit");
            System.out.print("Pilih Test Case: ");

            int choice = Utils.getInt();
            Utils.getString(); // Flush buffer

            if (choice == 0) break;

            List<Musician> musicians;
            switch (choice) {
                case 1 -> musicians = TestCase.getTestCaseOne();
                case 2 -> musicians = TestCase.getTestCaseTwo();
                case 3 -> musicians = TestCase.getTestCaseThree();
                default -> {
                    System.out.println("Pilihan tidak valid.");
                    continue;
                }
            }

            // 3. Configure Parameters
            System.out.print("Masukkan jumlah minggu: ");
            int totalWeeks = Utils.getInt();

            System.out.print("Masukkan jumlah musisi per instrumen (1 atau 2): ");
            int perInstrument = Utils.getInt();
            Utils.getString(); // Flush buffer

            // 4. Run Scheduler
            System.out.println("\n--- Menjalankan Penjadwalan ---");
            MusicianScheduling scheduler = new MusicianScheduling(requiredInstruments, musicians, totalWeeks, perInstrument);

            boolean finding = true;
            while (finding) {
                if (scheduler.dfsSchedule()) {
                    System.out.print("Cari solusi lain? (y/n): ");
                    if (Utils.getString().equalsIgnoreCase("n")) {
                        finding = false;
                    }
                } else {
                    finding = false;
                }
            }
        }
        System.out.println("Program Selesai.");
    }
}