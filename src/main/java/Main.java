import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Definisi Alat Musik yang dibutuhkan (Minimal 4 sesuai Kriteria)
        // Menggunakan PIANO sebagai representasi Keyboard sesuai enum yang ada
        List<Instruments> requiredInstruments = Arrays.asList(
                Instruments.GUITAR,
                Instruments.BASS,
                Instruments.PIANO,
                Instruments.DRUMS
        );

        // 2. Definisi Musisi berdasarkan Test Case
        List<Musician> musicians = TestCase.getTestCaseThree();

        // 3. Konfigurasi Total Minggu (Contoh: 4 Minggu)
        int totalWeeks = 2;

        // 4. Jalankan Penjadwalan
        System.out.println("=== Menjalankan Penjadwalan Test Case 1 ===");
        MusicianScheduling scheduler = new MusicianScheduling(requiredInstruments, musicians, totalWeeks);
        while (scheduler.dfsSchedule()) {
            System.out.println("Mau lagi? (y/n)");
            if (Utils.getString().equalsIgnoreCase("n")) break;
        }

    }
}