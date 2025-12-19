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

        // A & B: Bisa Guitar dan Bass. Libur Minggu 1 & 2 (Indeks 0 & 1)
        musicians.add(new Musician("A",
                Arrays.asList(Instruments.GUITAR, Instruments.BASS), Arrays.asList(0, 1)));
        musicians.add(new Musician("B",
                Arrays.asList(Instruments.GUITAR, Instruments.BASS), Arrays.asList(0, 1)));

        // C & D: Keyboard (PIANO). C Libur Minggu 3 & 4 (Indeks 2 & 3)
        musicians.add(new Musician("C",
                Arrays.asList(Instruments.PIANO), Arrays.asList(2, 3)));
        musicians.add(new Musician("D",
                Arrays.asList(Instruments.PIANO), new ArrayList<>()));

        // E & F: Drum. Selalu tersedia
        musicians.add(new Musician("E",
                Arrays.asList(Instruments.DRUMS), new ArrayList<>()));
        musicians.add(new Musician("F",
                Arrays.asList(Instruments.DRUMS), new ArrayList<>()));

        // G: Guitar. Selalu tersedia
        musicians.add(new Musician("G",
                Arrays.asList(Instruments.GUITAR), new ArrayList<>()));

        // H & I: Bass. Selalu tersedia
        musicians.add(new Musician("H",
                Arrays.asList(Instruments.BASS), new ArrayList<>()));
        musicians.add(new Musician("I",
                Arrays.asList(Instruments.BASS), new ArrayList<>()));

        // 3. Konfigurasi Total Minggu (Contoh: 4 Minggu)
        int totalWeeks = 4;

        // 4. Jalankan Penjadwalan
        System.out.println("=== Menjalankan Penjadwalan Test Case 1 ===");
        MusicianScheduling scheduler = new MusicianScheduling(requiredInstruments, musicians, totalWeeks);

        // Output akan otomatis memberikan pemerataan (Kriteria No. 6)
        // karena menggunakan urutan playCount terendah di MusicianScheduling
        scheduler.generateSchedule();
    }
}