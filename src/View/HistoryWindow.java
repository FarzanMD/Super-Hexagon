package View;

import Model.HistoryRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HistoryWindow extends JFrame {

    private JTable historyTable;
    private DefaultTableModel tableModel;

    public HistoryWindow(List<HistoryRecord> historyRecords) {
        setTitle("Player History");
        setSize(500, 400);
        setLocationRelativeTo(null);

        // Table columns
        String[] columns = {"Rank", "Score", "Name", "Date"};
        tableModel = new DefaultTableModel(columns, 0);
        historyTable = new JTable(tableModel);

        // Fill the table

        //i dont know what this shit is doing but it runs it well
        historyRecords.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        int rank = 1;
        for (HistoryRecord record : historyRecords) {
            tableModel.addRow(new Object[]{
                    rank++,
                    record.getScore(),
                    record.getName(),
                    record.getDate()
            });
        }

        JScrollPane scrollPane = new JScrollPane(historyTable);
        add(scrollPane, BorderLayout.CENTER);
    }
}
