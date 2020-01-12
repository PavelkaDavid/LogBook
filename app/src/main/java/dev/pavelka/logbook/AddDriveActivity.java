package dev.pavelka.logbook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddDriveActivity extends AppCompatActivity {

    static final int FROM_DATE = 0;
    static final int TO_DATE = 1;

    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    int mHour;
    int mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drive);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

}
