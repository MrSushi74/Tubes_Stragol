import java.util.*;

public class Main {
    public static void main(String[] args) {
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

            List<Musician> musicians = new ArrayList<>();
            switch (choice) {
                case 1 :
                    musicians = TestCase.getTestCaseOne();
                    break;
                case 2 :
                    musicians = TestCase.getTestCaseTwo();
                    break;
                case 3 :
                    musicians = TestCase.getTestCaseThree();
                    break;
                case 0 :
                    break;
            }

            if (choice == 0){
                break;
            }


            //total weeks
            System.out.print("Masukkan jumlah minggu: ");
            int totalWeeks = Utils.getInt();

            //max musisi per instrumen
            System.out.print("Masukkan jumlah musisi yang dibolehkan per instrumen: ");
            int perInstrument = Utils.getInt();
            Utils.getString();

            System.out.println("\n--- Menjalankan Penjadwalan ---");
            MusicianScheduling scheduler = new MusicianScheduling(requiredInstruments, musicians, totalWeeks, perInstrument);

            boolean finding = true;
            int totalSolutions = 0;
            while (finding) {
                if (scheduler.dfsSchedule()) {
                    System.out.print("Total Solutions : "+ ++totalSolutions +"\nFind Other Solutions? (y/n): ");
                    if (Utils.getString().equalsIgnoreCase("n")) {
                        System.out.println("total solutions found = "+ totalSolutions);
                        finding = false;
                    }
                } else {
                    System.out.println("No More Solutions, total solutions found = "+ totalSolutions);
                    finding = false;
                }
            }
        }
    }
}