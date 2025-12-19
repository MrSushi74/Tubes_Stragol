import java.util.ArrayList;
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
        List<Musician> musicians = new ArrayList<>();

            // A & B
        musicians.add(new Musician("A",
                                   List.of(Instruments.GUITAR), List.of(0)));
        musicians.add(new Musician("B",
                                   List.of(Instruments.BASS), List.of(0)));

    // C & D
        musicians.add(new Musician("C",
                                   List.of(Instruments.PIANO), List.of(0)));
        musicians.add(new Musician("D",
                                   List.of(Instruments.DRUMS), List.of(0)));

    // E & F
        musicians.add(new Musician("E",
                                   List.of(Instruments.GUITAR), List.of(1)));
        musicians.add(new Musician("F",
                                   List.of(Instruments.BASS), List.of(1)));

    // G
        musicians.add(new Musician("G",
                                   List.of(Instruments.PIANO), List.of(1)));

    // H
        musicians.add(new Musician("H",
                                   List.of(Instruments.DRUMS), List.of(1)));


        // 3. Konfigurasi Total Minggu (Contoh: 4 Minggu)
        int totalWeeks = 2;

        // 4. Jalankan Penjadwalan
        System.out.println("=== Menjalankan Penjadwalan Test Case 1 ===");
        MusicianScheduling scheduler = new MusicianScheduling(requiredInstruments, musicians, totalWeeks);

        // Output akan otomatis memberikan pemerataan (Kriteria No. 6)
        // karena menggunakan urutan playCount terendah di MusicianScheduling
        scheduler.generateSchedule();
    }
}