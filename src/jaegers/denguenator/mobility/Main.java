import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Main {

    public static void main(String[] args) {
        Map<String, Subscriber> subscriberMap = new HashMap<String, Subscriber>();
        String csvFile = "E:/education/semester7/fyp/reducedData.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        String dayId = "20130501";
        Map<String, Integer> mobilityMapper = new HashMap<String, Integer>();
        Map<String, Integer> homePopMapper = new HashMap<String, Integer>();
        Map<String, Integer> homeNightPopMapper = new HashMap<String, Integer>();

        try {
            br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine();

            for (int i = 0; i < 1000000; i++) {
//          while ((line = br.readLine()) != null) {
                line = br.readLine();
                String[] str = line.split(cvsSplitBy);
                String day = str[4].substring(0, 8);
                String time = str[4].substring(8);

                String subscriberId = str[1].equals("1") ? str[3] : str[2];
                String cellId = str[5];


                if (subscriberMap.get(subscriberId) == null) {
                    subscriberMap.put(subscriberId, new Subscriber(subscriberId, cellId, day, time));
                } else {
                    subscriberMap.get(subscriberId).addToMap(cellId, day, time);
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Subscriber tempSubscriber;
        String homeCell;
        String nightHomeCell;
        Day tempDay;
        String mostVisitedCell;
        String mobilityId;
        Integer freq;


        for (Map.Entry<String, Subscriber> entry : subscriberMap.entrySet()) {
            tempSubscriber = entry.getValue();

            homeCell = tempSubscriber.getHomeCellId();
            if (homeCell != null) {
                freq = homePopMapper.get(homeCell);
                homePopMapper.put(homeCell, (freq == null) ? 1 : freq + 1);
            }

            nightHomeCell = tempSubscriber.getNightHomeCellId();
            if (nightHomeCell != null) {
                Integer fr = homeNightPopMapper.get(nightHomeCell);
                homeNightPopMapper.put(nightHomeCell, (fr == null) ? 1 : fr + 1);
            }


            tempDay = tempSubscriber.getMapByDay().get(dayId);
            if (tempDay != null) {
                tempDay.removeHomeCell(homeCell);

                if (tempDay.setMostVisitedCell()) {
                    mostVisitedCell = tempDay.getMostVisitedCell();


                    if (homeCell != null) {
//                        mobilityId = nightHomeCell.concat("-" + mostVisitedCell);
                        mobilityId = homeCell.concat("-" + mostVisitedCell);
                        freq = mobilityMapper.get(mobilityId);
                        mobilityMapper.put(mobilityId, (freq == null) ? 1 : freq + 1);
                    }
                }
            }
        }

        for (Map.Entry<String, Integer> entry : mobilityMapper.entrySet()){
            System.out.println(entry.getKey()+"  "+entry.getValue());
        }

        System.out.println();
        System.out.println();

        for (Map.Entry<String, Integer> entry : homePopMapper.entrySet()) {
            System.out.println(entry.getKey() + "  " + entry.getValue() + " " + homeNightPopMapper.get(entry.getKey()));
        }
    }
}




