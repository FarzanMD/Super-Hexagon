package Model;
import org.json.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HistoryRecord {
    private int score;
    private String name;
    private String date;

    public HistoryRecord(int score, String name, String date) {
        this.score = score;
        this.name = name;
        this.date = date;
    }

    public int getScore() { return score; }
    public String getName() { return name; }
    public String getDate() { return date; }

    //addJSON
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("score", score);
        obj.put("name", name);
        obj.put("date", date);
        return obj;
    }

    public static HistoryRecord fromJson(JSONObject obj) {
        return new HistoryRecord(
                obj.getInt("score"),
                obj.getString("name"),
                obj.getString("date")
        );
    }

    //Save
    public static void saveHistory(List<HistoryRecord> records) {
        JSONArray array = new JSONArray();
        for (HistoryRecord record : records) {
            array.put(record.toJson());
        }

        try (FileWriter file = new FileWriter("history.json")) {
            file.write(array.toString(2)); // Pretty print
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //load
    public static List<HistoryRecord> loadHistory() {
        List<HistoryRecord> records = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/Model/history.json")));
            JSONArray array = new JSONArray(content);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                records.add(HistoryRecord.fromJson(obj));
            }
        } catch (IOException e) {

        }

        return records;
    }


}
