package dev.pavelka.logbook.ui.main;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.Calendar;
import java.util.Date;

import dev.pavelka.logbook.R;

/**
 * A header fragment containing a simple view.
 */
public class HeaderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static HeaderFragment newInstance(int index) {
        HeaderFragment fragment = new HeaderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_header, container, false);

        final DatePickerDialog.OnDateSetListener mDateListenerFrom;
        final EditText dateFrom = root.findViewById(R.id.dateFrom);

        final DatePickerDialog.OnDateSetListener mDateListenerTo;
        final EditText dateTo = root.findViewById(R.id.dateTo);

        mDateListenerFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                Log.d( "onDateSet" , month + "/" + day + "/" + year );
                dateFrom.setText( new StringBuilder().append( day ).append( "-" )
                        .append( month ).append( "-" ).append( year ) );
            }
        };

        mDateListenerTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                Log.d( "onDateSet" , month + "/" + day + "/" + year );
                dateTo.setText( new StringBuilder().append( day ).append( "-" )
                        .append( month ).append( "-" ).append( year ) );
            }
        };

        dateFrom.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =  new DatePickerDialog(getActivity(),
                        mDateListenerFrom,
                        year,month,day
                );

                dialog.show();
            }
        });

        dateTo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =  new DatePickerDialog(getActivity(),
                        mDateListenerTo,
                        year,month,day
                );

                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });

        return root;
    }
}