import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static List<Instruments> requiredInstruments = Arrays.asList(
            Instruments.GUITAR,
            Instruments.BASS,
            Instruments.PIANO,
            Instruments.DRUMS
    );

    public static List<Instruments> getPlayableInstruments(){
        List<Instruments> selectedList = new ArrayList<>();
        while(true){
            System.out.println("\n--- Available Instruments ---");
            for (Instruments i : Instruments.values()){
                System.out.print("[" + i.name() + "] ");
            }

            String choice = Utils.getString("\nPick Instrument to add (type 'stop' to finish): ");

            if (choice.equalsIgnoreCase("stop")) break;

            try {
                Instruments selected = Instruments.valueOf(choice.toUpperCase().trim());
                if (!selectedList.contains(selected)) {
                    selectedList.add(selected);
                    System.out.println("✅ Added " + selected);
                } else {
                    System.out.println("⚠️ Already added that one, boss!");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Input");
            }
        }
        return selectedList;
    }

    public static void main(String[] args) {

        // 2. Definisi Musisi berdasarkan Test Case
        List<Musician> musicians = new ArrayList<>();

        // 4. Jalankan Penjadwalan
        boolean looper = true;

        while(looper){
            System.out.println("==Menu==");
            System.out.println("1.Find Schedule");
            System.out.println("2.Add Musician");
            System.out.println("3.Add Playable Instrument To a Musician");
            System.out.println("4.Clear Musicians");
            System.out.println("5.Add Test Case 1 Musicians");
            System.out.println("6.Add Test Case 2 Musicians");
            System.out.println("7.Add Test Case 3 Musicians");
            System.out.println("0.Exit");
            String choice = Utils.getString("Choose : ");
            switch (choice){
                case "1":
                        int totalWeeks = Utils.getInt("How Many Weeks?");
                        MusicianScheduling scheduler = new MusicianScheduling(requiredInstruments, musicians, totalWeeks);
                        while (scheduler.dfsSchedule()) {
                            if (Utils.getString("Find other Solutions?").equalsIgnoreCase("n")) break;
                        }
                        break;
                case "2":
                        String musicianName = Utils.getString("Input Musicians Name");
                        //CHANGE LATER
                        if (musicianName.trim().isEmpty()) {
                            musicianName = Utils.getString("Name cannot be empty. Try again: ");
                        }
                        List<Instruments> playableInstruments = getPlayableInstruments();
                        musicians.add(new Musician(musicianName, playableInstruments, new ArrayList<>()));
                        System.out.println(musicianName + " successfully added");
                        break;
                case "3":
                    String targetName = Utils.getString("Which musician needs a new skill?");
                    Musician targetMusician = null;

                    // 1. Find the person
                    for (Musician m : musicians) {
                        if (m.getName().equalsIgnoreCase(targetName)) {
                            targetMusician = m;
                            break;
                        }
                    }

                    if (targetMusician != null) {
                        // 2. Get the new skills from the user
                        List<Instruments> newSkills = getPlayableInstruments();

                        // 3. Add them to the existing list (ensure your Musician class allows this!)
                        for (Instruments skill : newSkills) {
                            if (!targetMusician.getPlayableInstruments().contains(skill)) {
                                targetMusician.getPlayableInstruments().add(skill);
                                System.out.println("✅ " + targetMusician.getName() + " learned " + skill);
                            }
                        }
                    } else {
                        System.out.println("❌ Couldn't find a musician named " + targetName);
                    }
                    break;
                case "4":
                        String confirm = Utils.getString("Are you sure? This deletes the whole roster! (y/n)");
                        if (confirm.equalsIgnoreCase("y")) {
                            musicians.clear();
                            System.out.println("💀 Roster wiped. Start from scratch, boss.");
                        }
                        break;
                case "5":
                    musicians.clear();
                        musicians.addAll(TestCase.getTestCaseOne());
                    System.out.println("musicians added");
                    break;
                case "6":
                    musicians.clear();
                        musicians.addAll(TestCase.getTestCaseTwo());
                    System.out.println("musicians added");
                    break;
                case "7":
//                    musicians.clear();
//                        musicians.addAll(TestCase.getTestCaseThree());
//                    System.out.println("musicians added");
                    musicians.clear();
                    musicians.addAll(TestCase.getTestCaseThree());
                    // Create a NEW list with the extra guitar slot
                    requiredInstruments = new ArrayList<>(Arrays.asList(
                            Instruments.GUITAR, Instruments.GUITAR, // 👈 Two slots!
                            Instruments.BASS, Instruments.PIANO, Instruments.DRUMS
                    ));
                    System.out.println("✅ Case 3 Loaded (2 Guitar slots available)");
                    break;
                case "0":
                    looper = false;
                    break;
            }

        }


    }
}