package home;

import lombok.Getter;

import java.util.*;
import java.util.stream.IntStream;

public class Strip {
    private final Map<Integer, ArrayList<Integer>> buckets = new HashMap<>();
    private final TreeMap<Integer, Set<Integer>> bucketsBySize = new TreeMap<>();
    @Getter
    private final Ticket[] tickets = new Ticket[6];
    private final boolean randomizer;

    Strip(boolean randomizer) {
        this.randomizer = randomizer;
        setupStrip:
        while (true) {
            fillSourceBuckets();
            for (int i = 0; i < tickets.length; i++) {
                tickets[i] = prepareTicket();
                if (tickets[i] == null) {
                    continue setupStrip;  // ok, random distribution caused the conflict, redo the strip
                }
            }
            // all tickets good
            break;
        }
    }

    private void fillSourceBuckets() {
        buckets.clear();
        bucketsBySize.clear();
        fillOneSourceBucket(0, 1, 9);
        fillOneSourceBucket(1, 10, 19);
        fillOneSourceBucket(2, 20, 29);
        fillOneSourceBucket(3, 30, 39);
        fillOneSourceBucket(4, 40, 49);
        fillOneSourceBucket(5, 50, 59);
        fillOneSourceBucket(6, 60, 69);
        fillOneSourceBucket(7, 70, 79);
        fillOneSourceBucket(8, 80, 90);
    }

    private void fillOneSourceBucket(int column, int from, int to) {
        ArrayList<Integer> options = new ArrayList<>();
        IntStream.rangeClosed(from, to).forEach(options::add);
        buckets.put(column, options);
        bucketsBySize.computeIfAbsent(options.size(), k -> new HashSet<>()).add(column);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Ticket ticket : tickets) {
            sb.append(ticket.getFormattedTicket()).append("\n----------------------------------------------\n");
        }
        return sb.toString();
    }

    private Ticket prepareTicket() {
        Ticket ticket = new Ticket();
        for (int x = 0; x < 15; x++) {
            colSearch:
            for (Integer sizeKey : bucketsBySize.descendingKeySet()) {
                Set<Integer> oneSizeColumns = bucketsBySize.get(sizeKey);
                List<Integer> columns = new ArrayList<>(oneSizeColumns);
                while (!columns.isEmpty()) {
                    int pushedColumn = ticket.pushColumns(columns, buckets, randomizer ? (int) (Math.random() * 10) : 0);
                    if (pushedColumn >= 0) {
                        if (oneSizeColumns.size() == 1) {
                            bucketsBySize.remove(sizeKey);
                        } else {
                            oneSizeColumns.remove(pushedColumn);
                        }
                        if (sizeKey > 1) {
                            Set<Integer> newSizeSet = bucketsBySize.computeIfAbsent(sizeKey - 1, k -> new HashSet<>());
                            newSizeSet.add(pushedColumn);
                        }
                        break colSearch;
                    } else {
                        return null; // wrong distribution, restart
                    }
                }
            }
        }
        return ticket;
    }

    public static void main(String[] args) {
        final boolean RANDOMIZE_POSITIONS = false;
        long initTime = System.currentTimeMillis();
        int stripNumber = 0;
        for (int stripCount = 0; stripCount < 10000; stripCount++) {
            Strip strip = new Strip(RANDOMIZE_POSITIONS);
            if (stripNumber++ < 10) {
                System.out.println("Strip " + stripNumber);
                System.out.println(strip);
            }
        }
        System.out.println("Execution Time: " + (System.currentTimeMillis() - initTime));
    }
}
