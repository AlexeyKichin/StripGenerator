package home;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StripTest {
    @Test
    void testValidStrip() {
        Ticket[] tickets = new Strip().getTickets();
        for (int i = 0; i < 10000; i++) {
            for (Ticket ticket : tickets) {
                assertTrue(ticket.isTicketValid());
            }
        }
    }

    @Test
    void testTicketCount() {
        Ticket[] tickets = new Strip().getTickets();
        assertEquals(6, tickets.length);
    }

    @Test
    void testInvalidTicket() {
        Ticket[] tickets = new Strip().getTickets();
        // fill the value which is not filled
        Integer[][] data = tickets[0].getData();
        failingTicket:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] == null) {
                    data[i][j] = 1;
                    break failingTicket;
                }
            }
        }
        assertFalse(tickets[0].isTicketValid());
        // empty the value which is not filled
        Integer[][] data2 = tickets[1].getData();
        failingTicket2:
        for (int i = 0; i < data2.length; i++) {
            for (int j = 0; j < data2[i].length; j++) {
                if (data2[i][j] != null) {
                    data2[i][j] = null;
                    break failingTicket2;
                }
            }
        }
        assertFalse(tickets[1].isTicketValid());
    }

    @Test
    void testNumbersDistribution() {
        Ticket[] tickets = new Strip().getTickets();
        Map<Integer, Set<Integer>> buckets = new HashMap<>();
        for (Ticket ticket : tickets) {
            Integer[][] data = ticket.getData();
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    if (data[i][j] != null) {
                        Set<Integer> numbers = buckets.computeIfAbsent(j, k -> new HashSet<>());
                        assertFalse(numbers.contains(data[i][j]));
                        numbers.add(data[i][j]);
                    }
                }
            }
        }
        testColumnValues(buckets.get(0), 1,9);
        testColumnValues(buckets.get(1), 10,19);
        testColumnValues(buckets.get(2), 20,29);
        testColumnValues(buckets.get(3), 30,39);
        testColumnValues(buckets.get(4), 40,49);
        testColumnValues(buckets.get(5), 50,59);
        testColumnValues(buckets.get(6), 60,69);
        testColumnValues(buckets.get(7), 70,79);
        testColumnValues(buckets.get(8), 80,90);
    }

    private static void testColumnValues(Set<Integer> values, int min, int max){
        for (Integer value : values) {
            assertTrue(value>=min && value<=max);
        }

    }
}
