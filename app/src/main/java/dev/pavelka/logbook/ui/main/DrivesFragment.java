package dev.pavelka.logbook.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.pavelka.logbook.AddDriveActivity;
import dev.pavelka.logbook.R;
import dev.pavelka.logbook.ui.main.drives.DrivesContent;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DrivesFragment extends Fragment {

    static final int ADD_DRIVE_REQUEST = 1;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    MyDriveRecyclerViewAdapter myDriveRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DrivesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DrivesFragment newInstance(int columnCount) {
        DrivesFragment fragment = new DrivesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myDriveRecyclerViewAdapter = new MyDriveRecyclerViewAdapter(mListener);

        // FAB
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AddDriveActivity.class);
                startActivityForResult(i, ADD_DRIVE_REQUEST);
            }
        });

        // REFRESH
        Calendar calFrom = Calendar.getInstance();
        calFrom.add(Calendar.DATE, -7);
        Calendar calTo = Calendar.getInstance();

        EditText dateFrom = getActivity().findViewById(R.id.dateFrom);
        EditText dateTo = getActivity().findViewById(R.id.dateTo);

        dateFrom.setText(calFrom.get(Calendar.DAY_OF_MONTH) + "." + calFrom.get(Calendar.MONTH) + 1 + "." + calFrom.get(Calendar.YEAR));
        dateTo.setText(calTo.get(Calendar.DAY_OF_MONTH) + "." + calTo.get(Calendar.MONTH) + 1 + "." + calTo.get(Calendar.YEAR));

        //refresh();

        getActivity().findViewById(R.id.button_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        View view = inflater.inflate(R.layout.fragment_drive_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(myDriveRecyclerViewAdapter);
        }
        return view;
    }

    public void refresh() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("d.M.yyyy HH:mm");

        final EditText dateFrom = (EditText) getActivity().findViewById(R.id.dateFrom);
        final EditText dateTo = (EditText) getActivity().findViewById(R.id.dateTo);
        final TextView totalDistance = (TextView) getActivity().findViewById(R.id.text_total_distance);
        final TextView totalPrice = (TextView) getActivity().findViewById(R.id.text_total_price);
        final GraphView graph = (GraphView) getActivity().findViewById(R.id.graph);

        try {
            Date from = dateFormat.parse(dateFrom.getText().toString() + " 00:00");
            Date to = dateFormat.parse(dateTo.getText().toString() + " 23:59");

            if (from.compareTo(to) > 0) {
                throw new Exception("Počáteční datum musí být menší než konečné");
            }

            myDriveRecyclerViewAdapter.fillByDate(from, to);

            // Graph
            if (graph == null) {
                return;
            }

            List<DrivesContent.DriveItem> data = myDriveRecyclerViewAdapter.getmValues();
            double totalDistanceVal = 0;
            double totalPriceVal = 0;
            DataPoint[] dataPoints = new DataPoint[data.size()];

            int i = 0;
            for (DrivesContent.DriveItem item : data) {
                totalDistanceVal += item.distance;
                totalPriceVal += item.price;
                dataPoints[i] = new DataPoint(i, item.distance);
                i++;
            }

            totalDistance.setText(totalDistanceVal + "");
            totalPrice.setText(totalPriceVal + "");

            LineGraphSeries<DataPoint> series = new LineGraphSeries <> (dataPoints);
            graph.removeAllSeries();
            graph.addSeries(series);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Vyberte prosím rozmezí", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check that it is the AddDriveActivity with an OK result
        if (requestCode == ADD_DRIVE_REQUEST) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

                Date fromDate = new Date();
                Date toDate = new Date();
                try {
                    fromDate = dateFormat.parse(data.getStringExtra("fromDate"));
                    toDate = dateFormat.parse(data.getStringExtra("toDate"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String from = data.getStringExtra("from");
                String to = data.getStringExtra("to");
                double distance = Double.parseDouble(data.getStringExtra("distance"));
                double price = Double.parseDouble(data.getStringExtra("price")) * distance;

                DrivesContent.DriveItem drive = new DrivesContent.DriveItem(null, from, to, distance, price, fromDate, toDate);

                myDriveRecyclerViewAdapter.addItem(drive);

                Toast.makeText(getContext(), "Jízda byla přidána", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DrivesContent.DriveItem item);
    }
}
