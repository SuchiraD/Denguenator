import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Janitha on 8/3/2016.
 */
public class Utils {
    public boolean isNight(String time) {
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        Date eight = null;
        try {
            eight = parser.parse("20:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date twelve = null;
        try {
            twelve = parser.parse("24:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date zero = null;
        try {
            zero = parser.parse("00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date six = null;
        try {
            six = parser.parse("06:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String someOtherDate = this.getTime(time);

        try {
            Date userDate = parser.parse(someOtherDate);
            if (userDate.after(eight) && userDate.before(twelve) || userDate.after(zero) && userDate.before(six)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            // Invalid date was entered
        }

        return false;
    }

    public String getTime(String time) {
        String h = time.substring(0, 2);
        String m = time.substring(2, 4);
        String s = time.substring(4, 6);

        return h + ":" + m + ":" + s;
    }


}
