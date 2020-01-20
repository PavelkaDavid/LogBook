package dev.pavelka.logbook.ui.main.drives;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.pavelka.logbook.MainActivity;
import dev.pavelka.logbook.database.DatabaseHandler;
import dev.pavelka.logbook.database.Drive;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

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

    /**
     * Context
     */
    public static Context CONTEXT = new MainActivity();

    static {
        //getAllItems();
    }

    public static void getAllItems() {
        DatabaseHandler db = new DatabaseHandler(CONTEXT);

        List<Drive> drivesList = new ArrayList<>();
        for (Drive drive : db.getAllDrives()) {
            drivesList.add(new Drive(drive));
        }

        ITEMS.clear();
        ITEM_MAP.clear();
        for (Drive drive : drivesList) {
            ITEMS.add(getDriveItemFromDrive(drive));
            ITEM_MAP.put(getDriveItemFromDrive(drive).id, getDriveItemFromDrive(drive));
        }
    }

    public static void addItem(DriveItem item) {
        DatabaseHandler db = new DatabaseHandler(CONTEXT);
        db.addDrive(getDriveFromDriveItem(item));
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void removeItem(DriveItem item) {
        DatabaseHandler db = new DatabaseHandler(CONTEXT);
        db.deleteDrive(getDriveFromDriveItem(item));
        ITEMS.remove(item);
    }

    private static Drive getDriveFromDriveItem(DriveItem item) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        Drive drive = new Drive();
        if (item.id != null) {
            drive.setID(Integer.parseInt(item.id));
        }
        drive.setFromDate(dateFormat.format(item.from_datetime));
        drive.setToDate(dateFormat.format(item.to_datetime));
        drive.setFrom(item.from);
        drive.setTo(item.to);
        drive.setDistance(item.distance);
        drive.setPrice(item.price);

        return drive;
    }

    private static DriveItem getDriveItemFromDrive(Drive drive) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        DriveItem item = null;
        try {
            item = new DriveItem(drive.getID() + "", drive.getFrom(), drive.getTo(),
                    drive.getDistance(), drive.getPrice(), dateFormat.parse(drive.getFromDate()),
                    dateFormat.parse(drive.getToDate()));
            return item;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DriveItem implements Comparable<DriveItem> {
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
        public int compareTo(DriveItem u) {
            if (from_datetime == null || u.from_datetime == null) {
                return 0;
            }
            return from_datetime.compareTo(u.from_datetime);
        }

        @Override
        public String toString() {
            return "Z " + from + ", do " + to;
        }
    }
}
