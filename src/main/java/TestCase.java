import java.util.ArrayList;
import java.util.List;

public class TestCase {
    public static List<Musician> getTestCaseOne() {
        List<Musician> musicians = new ArrayList<>();

        // Musisi A & B (Libur Minggu 0)
        musicians.add(new Musician("A", List.of(Instruments.GUITAR, Instruments.BASS), List.of(0,1)));
        musicians.add(new Musician("B", List.of(Instruments.GUITAR, Instruments.BASS), List.of(0,1)));

        // Musisi C & D (Libur Minggu 0)
        musicians.add(new Musician("C", List.of(Instruments.PIANO), List.of(2,3)));
        musicians.add(new Musician("D", List.of(Instruments.PIANO), List.of()));

        // Musisi E & F (Libur Minggu 1)
        musicians.add(new Musician("E", List.of(Instruments.DRUMS), List.of()));
        musicians.add(new Musician("F", List.of(Instruments.DRUMS), List.of()));

        // Musisi G (Libur Minggu 1)
        musicians.add(new Musician("G", List.of(Instruments.GUITAR),List.of()));

        // Musisi H & I (Libur Minggu 1)
        musicians.add(new Musician("H", List.of(Instruments.BASS),List.of()));
        musicians.add(new Musician("I", List.of(Instruments.BASS),List.of()));

        return musicians;
    }

    public static List<Musician> getTestCaseTwo(){
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

        return musicians;
    }

    public static List<Musician> getTestCaseThree(){
        List<Musician> musicians = new ArrayList<>();
        //A & B
        musicians.add(new Musician("A",List.of(Instruments.GUITAR,Instruments.BASS),List.of(0)));
        musicians.add(new Musician("B",List.of(Instruments.GUITAR,Instruments.BASS),List.of(1)));

        //C & D
        musicians.add(new Musician("C",List.of(Instruments.PIANO),List.of(0)));
        musicians.add(new Musician("D",List.of(Instruments.PIANO),List.of()));

        //E & F
        musicians.add(new Musician("E",List.of(Instruments.DRUMS), List.of(1)));
        musicians.add(new Musician("F",List.of(Instruments.DRUMS), List.of(0)));

        // G
        musicians.add(new Musician("G",List.of(Instruments.GUITAR), List.of(1)));

        // H & I
        musicians.add(new Musician("H",List.of(Instruments.BASS), List.of(1)));
        musicians.add(new Musician("I",List.of(Instruments.BASS), List.of(0)));

        return musicians;
    }
}
