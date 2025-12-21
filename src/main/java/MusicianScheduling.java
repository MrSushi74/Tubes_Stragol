// MusicianScheduling.java updates

import java.util.*;
import java.util.stream.Collectors;

public class MusicianScheduling {
    private static class Schedule {
        int week;
        int instrumentIndex;
        int musicianListIndex;
        int assignedToCurrentInstrument;
        List<Musician> weeklyAssigned;

        public Schedule(int week, int instrumentIndex, int musicianListIndex, int assignedToCurrentInstrument, List<Musician> weeklyAssigned) {
            this.week = week;
            this.instrumentIndex = instrumentIndex;
            this.musicianListIndex = musicianListIndex;
            this.assignedToCurrentInstrument = assignedToCurrentInstrument;
            this.weeklyAssigned = new ArrayList<>(weeklyAssigned);
        }
    }

    private List<Instruments> instruments;
    private List<Musician> musicianList;
    private Map<Musician, Integer> playCount = new HashMap<>();
    private int totalWeeks;
    private int musiciansPerInstrument;
    private List<List<List<Musician>>> finalSchedule; // Updated to hold multiple musicians per slot
    private Stack<Schedule> scheduleStack = new Stack<>();
    int totalSolutions = 0;
    boolean foundSolution = false;

    public MusicianScheduling(List<Instruments> instruments, List<Musician> musicianList, int totalWeeks, int musiciansPerInstrument) {
        this.instruments = instruments;
        this.musicianList = musicianList;
        this.totalWeeks = totalWeeks;
        this.musiciansPerInstrument = musiciansPerInstrument;

        // Initialize schedule as a 3D structure: [week][instrument][list of musicians]
        this.finalSchedule = new ArrayList<>();
        for (int i = 0; i < totalWeeks; i++) {
            List<List<Musician>> weekList = new ArrayList<>();
            for (int j = 0; j < instruments.size(); j++) {
                weekList.add(new ArrayList<>());
            }
            finalSchedule.add(weekList);
        }

        for (Musician m : musicianList) {
            playCount.put(m, 0);
        }
        // Initial state: week 0, instrument 0, musician index 0, 0 musicians assigned to instrument 0
        scheduleStack.push(new Schedule(0, 0, 0, 0, new ArrayList<>()));
    }

    private void printSchedule() {
        // Adjust line length for better visibility if using 2 musicians
        int lineLength = 20 + (totalWeeks * 18);
        System.out.println("\n" + "=".repeat(lineLength));

        // Print Header
        System.out.printf("%-15s |", "INSTRUMEN");
        for (int w = 0; w < totalWeeks; w++) {
            System.out.printf(" Minggu %-9d |", (w + 1));
        }
        System.out.println("\n" + "-".repeat(lineLength));

        // Print Rows for each instrument
        for (int i = 0; i < instruments.size(); i++) {
            String instrumentName = instruments.get(i).toString();
            System.out.printf("%-15s |", instrumentName);

            for (int w = 0; w < totalWeeks; w++) {
                // Get the list of musicians for this specific week and instrument
                List<Musician> assigned = finalSchedule.get(w).get(i);

                if (assigned.isEmpty()) {
                    System.out.printf(" %-16s |", "-");
                } else {
                    // Join musician names with a comma if there are multiple
                    String names = assigned.stream()
                            .map(Musician::getName)
                            .collect(Collectors.joining(", "));
                    System.out.printf(" %-16s |", names);
                }
            }
            System.out.println();
        }

        System.out.println("=".repeat(lineLength) + "\n");
    }

    public boolean dfsSchedule() {
        // If the stack is empty, there are no more possibilities
        if (scheduleStack.isEmpty()) {
            return false;
        }

        // IMPORTANT: If we are here and totalSolutions > 0, it means the user
        // just said 'y' to "Mau lagi?". We MUST backtrack one step first
        // to remove the last musician of the previous solution.
        if (foundSolution) {
            backtrack();
            foundSolution = false;
        }

        while (!scheduleStack.isEmpty()) {
            Schedule current = scheduleStack.peek();

            // 1. Success condition: All weeks are scheduled
            if (current.week == totalWeeks) {
                totalSolutions++;
                printSchedule();
                foundSolution = true; // Mark that we found one so the next call knows to backtrack
                return true;
            }

            // 2. If current instrument is fully staffed, move to the next instrument
            if (current.assignedToCurrentInstrument >= musiciansPerInstrument) {
                scheduleStack.pop();
                scheduleStack.push(new Schedule(current.week, current.instrumentIndex + 1, 0, 0, current.weeklyAssigned));
                continue;
            }

            // 3. If all instruments for the week are done, move to next week
            if (current.instrumentIndex == instruments.size()) {
                scheduleStack.pop();
                scheduleStack.push(new Schedule(current.week + 1, 0, 0, 0, new ArrayList<>()));
                continue;
            }

            Instruments inst = instruments.get(current.instrumentIndex);
            List<Musician> prioritized = musicianList.stream()
                    .sorted(Comparator.comparingInt(m -> playCount.get(m)))
                    .toList();

            boolean foundChoice = false;
            for (int i = current.musicianListIndex; i < prioritized.size(); i++) {
                Musician m = prioritized.get(i);

                // Logic to skip swapped spots:
                // If we are picking the 2nd musician for the SAME instrument,
                // ensure their index in the original musicianList is HIGHER than the 1st musician.
                if (current.assignedToCurrentInstrument > 0) {
                    Musician firstMusician = finalSchedule.get(current.week).get(current.instrumentIndex).get(0);
                    int firstIndex = musicianList.indexOf(firstMusician);
                    int currentIndex = musicianList.indexOf(m);
                    if (currentIndex <= firstIndex) continue;
                }

                // Validation: Available, Can Play, Not already in another slot this week, Not already in THIS slot
                if (m.isAvailable(current.week) &&
                        m.canPlay(inst) &&
                        !current.weeklyAssigned.contains(m) &&
                        !finalSchedule.get(current.week).get(current.instrumentIndex).contains(m)) {

                    // Update the index for this state so we don't pick this musician again for this slot
                    current.musicianListIndex = i + 1;

                    // Assign
                    finalSchedule.get(current.week).get(current.instrumentIndex).add(m);
                    current.weeklyAssigned.add(m);
                    playCount.put(m, playCount.get(m) + 1);

                    // Push new state to find the next musician (or next instrument)
                    scheduleStack.push(new Schedule(current.week, current.instrumentIndex, 0, current.assignedToCurrentInstrument + 1, current.weeklyAssigned));
                    foundChoice = true;
                    break;
                }
            }

            // 4. Backtrack if no musician could be found for the current requirement
            if (!foundChoice) {
                //but only found 1, we can skip to the next instrument
                if (current.assignedToCurrentInstrument > 0) {
                    scheduleStack.pop();
                    scheduleStack.push(new Schedule(current.week, current.instrumentIndex + 1, 0, 0, current.weeklyAssigned));
                } else {
                    backtrack();
                }
            }
        }
        return false;
    }

    private void backtrack() {
        scheduleStack.pop();
        if (!scheduleStack.isEmpty()) {
            Schedule prev = scheduleStack.peek();
            List<Musician> assigned = finalSchedule.get(prev.week).get(prev.instrumentIndex);
            if (!assigned.isEmpty()) {
                Musician lastM = assigned.remove(assigned.size() - 1);
                playCount.put(lastM, playCount.get(lastM) - 1);
                prev.weeklyAssigned.remove(lastM);
            }
        }
    }
}