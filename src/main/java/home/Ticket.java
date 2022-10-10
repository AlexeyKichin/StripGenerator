package home;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Ticket {
    @Getter(AccessLevel.PACKAGE)
    private final Integer[][] data = new Integer[3][9];
    private final int[] rowCount = new int[3];
    private final int[] columnCount = new int[9];

    private boolean push(int row, int col, Integer value) {
        if (rowCount[row] == 5 || data[row][col] != null) {
            return false;
        }
        columnCount[col]++;
        rowCount[row]++;
        data[row][col] = value;
        return true;
    }

    public int pushColumns(
            List<Integer> columns,
            Map<Integer, ArrayList<Integer>> valueBuckets,
            int stripRandomizer // used to randomize strips, make sure cells are not filled in the same way in different strips
    ) {
        Integer[] cols = columns.toArray(new Integer[0]);
        Arrays.sort(cols, (o1, o2) -> columnCount[o1] > columnCount[o2] ? 1 : columnCount[o1] < columnCount[o2] ? -1 : (o1 * o2 * stripRandomizer & 16) > 0 ? 1 : -1);
        Integer[] rows = new Integer[]{0, 1, 2};
        Arrays.sort(rows, (o1, o2) -> rowCount[o1] > rowCount[o2] ? 1 : rowCount[o1] < rowCount[o2] ? -1 : (o1 * o2 * stripRandomizer & 16) > 0 ? 1 : -1);
        for (Integer col : cols) {
            for (Integer row : rows) {
                ArrayList<Integer> values = valueBuckets.get(col);
                int valueIdx = (int) (Math.random() * values.size());
                int value = values.get(valueIdx);
                if (push(row, col, value)) {
                    values.remove(valueIdx);
                    return col;
                }
            }
        }
        return -1;
    }

    public boolean isTicketValid() {
        int[] rowCount = new int[3];
        int[] columnCount = new int[9];

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] != null) {
                    rowCount[i]++;
                    columnCount[j]++;
                }
            }
        }
        for (int i : rowCount) {
            if (i != 5) return false;
        }
        for (int i : columnCount) {
            if (i == 0) return false;
        }
        return true;
    }

    public String getFormattedTicket() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            if (i > 0) sb.append("\n");
            for (int j = 0; j < data[i].length; j++) {
                sb.append(formatCell(data[i][j], j == 0));
            }
        }
        return sb.toString();
    }

    private static String formatCell(Integer cell, boolean firstInRow) {
        StringBuilder sb = new StringBuilder(firstInRow ? "| " : " ");
        if (cell != null) {
            sb.append(cell);
        }
        while (sb.length() <= (firstInRow ? 4 : 3)) {
            sb.append(" ");
        }
        sb.append("|");
        return sb.toString();
    }
}
