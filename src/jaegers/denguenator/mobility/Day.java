import java.util.HashMap;
import java.util.Map;

/**
 * Created by Janitha on 8/3/2016.
 */
public class Day {

    private String dayId;
    private String mostVisitedCell;
    Map<String, Integer> map = new HashMap<String, Integer>();

    public Day(String dayId, String mostVisitedCell) {
        this.dayId = dayId;
        this.mostVisitedCell = mostVisitedCell;
        this.addToMap(mostVisitedCell);
    }

    public void addToMap(String cellId) {
        Integer freq = this.map.get(cellId);
        this.map.put(cellId, (freq == null) ? 1 : freq + 1);
    }

    public void removeHomeCell(String cellId) {
        if (map.containsKey(cellId))
            map.remove(cellId);
    }

    public boolean setMostVisitedCell() {
        Map.Entry<String, Integer> maxEntry = null;

        if (!this.map.isEmpty()) {
            for (Map.Entry<String, Integer> entry : this.map.entrySet()) {
                if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                    maxEntry = entry;
                }
            }

            this.mostVisitedCell = maxEntry.getKey();
            return true;
        } else {
            return false;
        }
    }

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public String getMostVisitedCell() {
        return mostVisitedCell;
    }

    public void setMostVisitedCell(String mostVisitedCell) {
        this.mostVisitedCell = mostVisitedCell;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
}
