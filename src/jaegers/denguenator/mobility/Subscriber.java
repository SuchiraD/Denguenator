import java.util.HashMap;
import java.util.Map;

/**
 * Created by Janitha on 8/2/2016.
 */
public class Subscriber {

    private String subscriberId;
    private String homeCellId;
    private String nightHomeCellId = null;
    Map<String, Integer> map = new HashMap<String, Integer>();
    Map<String, Integer> nightMap = new HashMap<String, Integer>();
    Map<String, Day> mapByDay = new HashMap<String, Day>();


    public Subscriber(String subscriberId, String homeCellId, String day, String time) {
        this.subscriberId = subscriberId;
        this.homeCellId = homeCellId;
        this.addToMap(homeCellId, day, time);
    }

    public void printMap() {
        for (String name : this.map.keySet()) {
            String key = name.toString();
            String value = map.get(name).toString();
        }
    }

    public void addToMap(String cellId, String day, String time) {
        Integer freq = this.map.get(cellId);
        this.map.put(cellId, (freq == null) ? 1 : freq + 1);
        this.setHomeCell();

        if (this.mapByDay.get(day) == null)
            this.mapByDay.put(day, new Day(day, cellId));
        else
            this.mapByDay.get(day).addToMap(cellId);

        this.addToNightMap(cellId, time);
    }

    public void addToNightMap(String cellId, String time) {
        Utils myUtill = new Utils();

        if (myUtill.isNight(time)) {
            Integer freq = this.nightMap.get(cellId);
            this.nightMap.put(cellId, (freq == null) ? 1 : freq + 1);
            this.setNightHomeCell();
        }
    }

    public void setHomeCell() {
        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : this.map.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        this.homeCellId = maxEntry.getKey();
    }

    public void setNightHomeCell() {
        Map.Entry<String, Integer> maxEntry = null;

        if (!nightMap.isEmpty()) {
            for (Map.Entry<String, Integer> entry : this.nightMap.entrySet()) {
                if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                    maxEntry = entry;
                }
            }

            this.nightHomeCellId = maxEntry.getKey();
        }
    }

    public String getHomeCellId() {
        return homeCellId;
    }

    public void setHomeCellId(String homeCellId) {
        this.homeCellId = homeCellId;
    }

    public Map<String, Day> getMapByDay() {
        return mapByDay;
    }

    public void setMapByDay(Map<String, Day> mapByDay) {
        this.mapByDay = mapByDay;
    }

    public String getNightHomeCellId() {
//        if (nightHomeCellId != null) {
        return nightHomeCellId;
//        }else {
//            return homeCellId;
//        }
    }

    public void setNightHomeCellId(String nightHomeCellId) {
        this.nightHomeCellId = nightHomeCellId;
    }

    public Map<String, Integer> getNightMap() {
        return nightMap;
    }

    public void setNightMap(Map<String, Integer> nightMap) {
        this.nightMap = nightMap;
    }
}
