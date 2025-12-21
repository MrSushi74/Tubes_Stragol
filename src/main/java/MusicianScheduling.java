// MusicianScheduling.java updates

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
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

    private final List<Instruments> instruments;
    private final List<Musician> musicianList;
    private final Map<Musician, Integer> playCount = new HashMap<>();
    private final int totalWeeks;
    private final int musiciansPerInstrument;
    private final List<List<List<Musician>>> finalSchedule; 
    private final Stack<Schedule> scheduleStack = new Stack<>();
    int totalSolutions = 0;
    boolean foundSolution = false;

    public MusicianScheduling(List<Instruments> instruments, List<Musician> musicianList, int totalWeeks, int musiciansPerInstrument) {
        this.instruments = instruments;
        this.musicianList = musicianList;
        this.totalWeeks = totalWeeks;
        this.musiciansPerInstrument = musiciansPerInstrument;

        //[week][instrument][musicianList]
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
        //stack init
        scheduleStack.push(new Schedule(0, 0, 0, 0, new ArrayList<>()));
    }


    public boolean dfsSchedule() {
        //no more schedule solutions
        if (scheduleStack.isEmpty()) {
            return false;
        }

        // if found a solution in last call, backtrack 
        if (foundSolution) {
            backtrack();
            foundSolution = false;
        }

        while (!scheduleStack.isEmpty()) {
            Schedule current = scheduleStack.peek();

            //All weeks are scheduled
            if (current.week == totalWeeks) {
                totalSolutions++;
                printSchedule();
                foundSolution = true;
                return true;
            }

            // instrument full
            if (current.assignedToCurrentInstrument >= musiciansPerInstrument) {
                scheduleStack.pop();
                scheduleStack.push(new Schedule(current.week, current.instrumentIndex + 1, 0, 0, current.weeklyAssigned));
                continue;
            }

            // all instruments scheduled
            if (current.instrumentIndex == instruments.size()) {
                scheduleStack.pop();
                scheduleStack.push(new Schedule(current.week + 1, 0, 0, 0, new ArrayList<>()));
                continue;
            }
            
            Instruments inst = instruments.get(current.instrumentIndex);
            List<Musician> prioritized = new ArrayList<>(musicianList);

            prioritized.sort(new Comparator<Musician>() {
                @Override
                public int compare(Musician m1, Musician m2) {
                    return Integer.compare(playCount.get(m1), playCount.get(m2));
                }
            });

            boolean foundChoice = false;
            for (int i = current.musicianListIndex; i < prioritized.size(); i++) {
                Musician m = prioritized.get(i);

                //skip if musicians only swap
                if (current.assignedToCurrentInstrument > 0) {
                    List<Musician> assignedToThisInstrument = finalSchedule.get(current.week).get(current.instrumentIndex);
                    Musician lastMusician = assignedToThisInstrument.getLast();
                    int firstIndex = musicianList.indexOf(lastMusician);
                    int currentIndex = musicianList.indexOf(m);
                    if (currentIndex <= firstIndex) continue;
                }

                //assign musician if musician can play, available, unassigned this week, and unassigned this spot
                if (m.isAvailable(current.week) &&
                        m.canPlay(inst) &&
                        !current.weeklyAssigned.contains(m) &&
                        !finalSchedule.get(current.week).get(current.instrumentIndex).contains(m)) {

                    // next musician starts after this
                    current.musicianListIndex = i + 1;

                    // Assign
                    finalSchedule.get(current.week).get(current.instrumentIndex).add(m);
                    current.weeklyAssigned.add(m);
                    playCount.put(m, playCount.get(m) + 1);

                    // push next state
                    scheduleStack.push(new Schedule(current.week, current.instrumentIndex, 0, current.assignedToCurrentInstrument + 1, current.weeklyAssigned));
                    foundChoice = true;
                    break;
                }
            }

            //backtrack if no musicians fit the criteria
            if (!foundChoice) {
                //if musicians are assigned, just try next instrument
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
}