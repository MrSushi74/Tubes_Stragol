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
    private record Assignment(int week, Instruments instruments, Musician musician){}
    private final List<Instruments> instruments;
    private final List<Musician> musicianList;
    private final Map<Musician, Integer> playCount = new HashMap<>();
    private final int totalWeeks;
    private final Set<Assignment> schedule;
    private final Stack<Schedule> scheduleStack = new Stack<>();
    int totalSolutions = 0;


    public MusicianScheduling(List<Instruments> instruments, List<Musician> musicianList, int totalWeeks) {
        this.instruments = instruments;
        this.musicianList = musicianList;
        this.totalWeeks = totalWeeks;
        this.schedule = new HashSet<>();

        for (Musician m : musicianList) {
            playCount.put(m, 0);
        }

        scheduleStack.push(new Schedule(0,0,0,new ArrayList<>()));
    }


    private void printSchedule() {
        System.out.println("\n" + "=".repeat(20 + (totalWeeks * 15)));
        System.out.printf("%-15s |", "INSTRUMEN");
        for (int w = 0; w < totalWeeks; w++) System.out.printf(" Minggu %-6d |", (w + 1));
        System.out.println("\n" + "-".repeat(20 + (totalWeeks * 15)));

        // Get unique instruments to print row by row
        Set<Instruments> allUsed = new TreeSet<>(); // sorted for clean look
        allUsed.addAll(Main.requiredInstruments);

        for (Instruments inst : allUsed) {
            System.out.printf("%-15s |", inst);
            for (int w = 0; w < totalWeeks; w++) {
                final int week = w;
                String names = schedule.stream()
                        .filter(a -> a.week == week && a.instruments == inst)
                        .map(a -> a.musician.getName())
                        .collect(Collectors.joining(", "));
                System.out.printf(" %-13s |", names.isEmpty() ? "-" : names);
            }
            System.out.println();
        }
        System.out.println("=".repeat(20 + (totalWeeks * 15)));
    }

    private boolean isWeekDone(int week) {
        for (Instruments req : Main.requiredInstruments) {
            boolean found = false;
            for (Assignment a : schedule) {
                if (a.week() == week && a.instruments == req) {
                    found = true;
                    break;
                }
            }
            if (!found) return false; // MISSING AN INSTRUMENT! SHUT IT DOWN!
        }
        return true;
    }

    public boolean dfsSchedule() {
        if (scheduleStack.isEmpty()) return false;

        while (!scheduleStack.isEmpty()) {
            Schedule current = scheduleStack.peek();
            int week = current.week;
            int slotIdx = current.instrumentIndex;

            // 1. SUCCESS: All weeks fully booked
            if (week == totalWeeks) {
                totalSolutions++;
                printSchedule();
                scheduleStack.pop();
                return true;
            }

            // 2. ALL SLOTS FILLED FOR THIS WEEK: Move to next week
            if (slotIdx == instruments.size()) {
                scheduleStack.push(new Schedule(week + 1, 0, 0, new ArrayList<>()));
                continue;
            }

            // 3. TRY TO FILL THE CURRENT SLOT
            Instruments requiredInstrument = instruments.get(slotIdx);
            boolean foundMusician = false;

            for (int i = current.musicianListIndex; i < musicianList.size(); i++) {
                Musician m = musicianList.get(i);

                // Logic: Is musician available, can they play this specific instrument,
                // and are they already playing something else this week?
                if (m.isAvailable(week) &&
                        m.getPlayableInstruments().contains(requiredInstrument) &&
                        !current.weeklyAssigned.contains(m)) {

                    // Save where we are in the musician list for backtracking
                    current.musicianListIndex = i + 1;

                    // Assign
                    Assignment asgn = new Assignment(week, requiredInstrument, m);
                    schedule.add(asgn);
                    current.weeklyAssigned.add(m);
                    playCount.put(m, playCount.get(m) + 1);

                    // PUSH: Move to the NEXT SLOT (slotIndex + 1)
                    scheduleStack.push(new Schedule(week, slotIdx + 1, 0, current.weeklyAssigned));
                    foundMusician = true;
                    break;
                }
            }

            // 4. BACKTRACK: If no musician can fill this slot
            if (!foundMusician) {
                scheduleStack.pop();
                if (!scheduleStack.isEmpty()) {
                    undoSlotAssignment(week);
                }
            }
        }
        return false;
    }

    private void undoSlotAssignment(int week) {
        if (scheduleStack.isEmpty()) return;
        Schedule prev = scheduleStack.peek();

        // We need to find the assignment that matches this week and THIS slot
        int currentSlotIdx = prev.instrumentIndex;
        Instruments currentInst = instruments.get(currentSlotIdx);

        // Filter the set to find the musician who was last assigned to this specific slot
        Optional<Assignment> toRemove = schedule.stream()
                .filter(a -> a.week == week && a.instruments == currentInst)
                // We want the one that was assigned to the musician at (musicianListIndex - 1)
                .filter(a -> a.musician.equals(musicianList.get(prev.musicianListIndex - 1)))
                .findFirst();

        if (toRemove.isPresent()) {
            Assignment a = toRemove.get();
            schedule.remove(a);
            playCount.put(a.musician, playCount.get(a.musician) - 1);
            prev.weeklyAssigned.remove(a.musician);
        }
    }

//    public boolean dfsSchedule() {
//        if (scheduleStack.isEmpty()) {
//            System.out.println("No more solutions available. Total solutions found: " + totalSolutions);
//            return false;
//        }
//
//        while(!scheduleStack.isEmpty()) {
//            Schedule currentSchedule = scheduleStack.peek();
//
//            int week = currentSchedule.week;
//            int index = currentSchedule.musicianListIndex;
//
//            if (week == totalWeeks) {
//                totalSolutions++;
//                printSchedule();
//                scheduleStack.pop();
//                return true;
//            }
//
//            // kalo semua instrumen minggu ini beres, lanjut ke next week
//            if (index == musicianList.size()) {
//                if (isWeekDone(week)) {
//                    scheduleStack.push(new Schedule(week+1,0,0, new ArrayList<>()));
//                } else{
//
//                }
//                scheduleStack.pop();
//                if (scheduleStack.isEmpty()){
//                    Schedule previousSchedule = scheduleStack.peek();
//
//                    Musician lastMusician = musicianList.get(previousSchedule.musicianListIndex);
//
//                    schedule.removeIf(a -> a.week == week && a.musician.equals(lastMusician));
//
//                    playCount.put(lastMusician, playCount.get(lastMusician)-1);
//                    previousSchedule.weeklyAssigned.remove(lastMusician);
//                }
//                continue;
//            }
//
//            Musician m = musicianList.get(index);
//            boolean found = false;
//
//            if (m.isAvailable(week) && !currentSchedule.weeklyAssigned.contains(m)){
//                List<Instruments> playableInstruments = m.getPlayableInstruments();
//
//                if (currentSchedule.instrumentIndex < playableInstruments.size()){
//                    Instruments playableInstrument = playableInstruments.get(currentSchedule.instrumentIndex);
//
//                    //bring schedule to next instrument
//                    currentSchedule.instrumentIndex++;
//
//                    Assignment a = new Assignment(week,playableInstrument,m);
//                    schedule.add(a);
//                    currentSchedule.weeklyAssigned.add(m);
//                    playCount.put(m,playCount.get(m)+1);
//
//                    scheduleStack.push(new Schedule(week,0,index+1, currentSchedule.weeklyAssigned));
//                    found = true;
//                }
//            }
//
//            if (!found){
//                scheduleStack.pop();
//                scheduleStack.push(new Schedule(week,0,index+1,currentSchedule.weeklyAssigned));
//            }
//
//        }
//        System.out.println("No More Solutions! Total: " + totalSolutions);
//        return false;
//    }
}


