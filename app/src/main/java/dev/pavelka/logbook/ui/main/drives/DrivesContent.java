package dev.pavelka.logbook.ui.main.drives;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Drives list content
 */
public class DrivesContent {

    /**
     * An array of drives
     */
    public static final List<DriveItem> ITEMS = new ArrayList<DriveItem>();

    /**
     * A map of drive items, by ID.
     */
    public static final Map<String, DriveItem> ITEM_MAP = new HashMap<String, DriveItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            try {
                addItem(createSampleItem(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private static void addItem(DriveItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DriveItem createSampleItem(int position) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String fromDate = "05-01-2019 16:35:42";
        String toDate = "05-01-2019 16:53:34";
        Date from = dateFormat.parse(fromDate);
        Date to = dateFormat.parse(toDate);
        return new DriveItem(String.valueOf(position), "Start position " + position, "End position " + position, 3 * position, 1.9 * 3 * position, from, to);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DriveItem {
        public final String id;
        public final String from;
        public final String to;
        public final Date from_datetime;
        public final Date to_datetime;
        public final double distance;
        public final double price;

        public DriveItem(String id, String from, String to, double distance, double price, Date from_datetime, Date to_datetime) {
            this.id = id;
            this.from = from;
            this.to = to;
            this.distance = distance;
            this.price = price;
            this.from_datetime = from_datetime;
            this.to_datetime = to_datetime;
        }

        @Override
        public String toString() {
            return "Z " + from + ", do " + to;
        }
    }
}
