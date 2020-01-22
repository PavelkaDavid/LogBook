package dev.pavelka.logbook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dev.pavelka.logbook.ui.main.gps.GPSTracker;

public class AddDriveActivity extends AppCompatActivity {

    static final int FROM_DATE = 0;
    static final int TO_DATE = 1;

    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    int mHour;
    int mMinute;

    int PLACE_FROM_PICKER_REQUEST = 1;
    int PLACE_TO_PICKER_REQUEST = 2;

    GPSTracker mGPS;
    SharedPreferences myPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drive);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Preferences
        myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String lastPrice = myPrefs.getString("lastPrice", null);

        EditText price = findViewById(R.id.price);
        price.setText(lastPrice);

        // GPS
        mGPS = new GPSTracker(this);
        if (mGPS.canGetLocation) {
            mGPS.getLocation();
        }

        // PLACES API
        // Initialize Places.
        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_place_api));

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);
    }

    @Override
    public void onStop () {
        super.onStop();

        mGPS.stopUsingGPS();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickGpsFrom(View v) {
        LatLng latLng = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());
        EditText from = (EditText) findViewById(R.id.from);
        from.setText(getAddress(latLng));
    }

    public void onClickGpsTo(View v) {
        LatLng latLng = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());
        EditText to = (EditText) findViewById(R.id.to);
        to.setText(getAddress(latLng));
    }

    public void onClickChooseFrom(View v) {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, PLACE_FROM_PICKER_REQUEST);
    }

    public void onClickChooseTo(View v) {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, PLACE_TO_PICKER_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_FROM_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String address = place.getAddress();
                EditText from = (EditText) findViewById(R.id.from);
                from.setText(address);
            }
        }
        else if (requestCode == PLACE_TO_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String address = place.getAddress();
                EditText to = (EditText) findViewById(R.id.to);
                to.setText(address);
            }
        }
    }

    public void onClickFrom(View v) {
        datePicker(FROM_DATE);
    }

    public void onClickTo(View v) {
        datePicker(TO_DATE);
    }

    public void onClickBtn(View v) {

        EditText fromDate = (EditText) findViewById(R.id.fromDate);
        EditText from = (EditText) findViewById(R.id.from);
        EditText toDate = (EditText) findViewById(R.id.toDate);
        EditText to = (EditText) findViewById(R.id.to);
        EditText distance = (EditText) findViewById(R.id.distance);
        EditText price = (EditText) findViewById(R.id.price);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            if (dateFormat.parse(fromDate.getText().toString()).compareTo(dateFormat.parse(toDate.getText().toString())) > 0) {
                Toast.makeText(this, "Datum počátku cesty musí být menší než datum konce cesty!", Toast.LENGTH_LONG).show();
                return;
            }

            Double.parseDouble(distance.getText().toString());
            Double.parseDouble(price.getText().toString());

            String fromText = from.getText().toString();
            String toText = to.getText().toString();
            if (fromText.equals("") || toText.equals("")) {
                Toast.makeText(this, "Vyplňte všechna pole", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Vyplňte všechna pole", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences.Editor e = myPrefs.edit();
        e.putString("lastPrice", price.getText().toString());
        e.commit();

        Intent intent = new Intent();
        intent.putExtra("fromDate", fromDate.getText().toString());
        intent.putExtra("from", from.getText().toString());
        intent.putExtra("toDate", toDate.getText().toString());
        intent.putExtra("to", to.getText().toString());
        intent.putExtra("distance", distance.getText().toString());
        intent.putExtra("price", price.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void datePicker(int type){
        final int typeBox = type;

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                        date_time = checkDigit(dayOfMonth) + "." + checkDigit(monthOfYear + 1) + "." + year;
                        timePicker(typeBox);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePicker(int type) {
        final int typeBox = type;

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        date_time = date_time + " " + checkDigit(hourOfDay) + ":" + checkDigit(minute);

                        EditText editBoxFromDate = (EditText) findViewById(R.id.fromDate);
                        EditText editBoxToDate = (EditText) findViewById(R.id.toDate);

                        switch (typeBox)
                        {
                            case FROM_DATE:
                                editBoxFromDate.setText(date_time);
                                break;
                            case TO_DATE:
                                editBoxToDate.setText(date_time);
                                break;
                        }
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public String getAddress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    // In this sample, get just a single address.
                    1);
        } catch (IOException e) {
            // Catch network or other I/O problems.
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // Catch invalid latitude or longitude values.
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            String result = TextUtils.join(System.getProperty("line.separator"), addressFragments);
            return result;
        }

        return null;
    }

}
