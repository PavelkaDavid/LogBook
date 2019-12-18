package dev.pavelka.logbook.ui.main.drives;

import java.util.ArrayList;
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
            addItem(createSampleItem(i));
        }
    }

    private static void addItem(DriveItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DriveItem createSampleItem(int position) {
        return new DriveItem(String.valueOf(position), "Start position " + position, "End position " + position, 3 * position, 1.9 * 3 * position);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DriveItem {
        public final String id;
        public final String from;
        public final String to;
        public final double distance;
        public final double price;

        public DriveItem(String id, String from, String to, double distance, double price) {
            this.id = id;
            this.from = from;
            this.to = to;
            this.distance = distance;
            this.price = price;
        }

        @Override
        public String toString() {
            return "Z " + from + ", do " + to;
        }
    }
}
