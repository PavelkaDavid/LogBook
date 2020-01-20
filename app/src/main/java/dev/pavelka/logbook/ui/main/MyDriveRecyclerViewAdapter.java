package dev.pavelka.logbook.ui.main;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dev.pavelka.logbook.MainActivity;
import dev.pavelka.logbook.R;
import dev.pavelka.logbook.database.Drive;
import dev.pavelka.logbook.ui.main.DrivesFragment.OnListFragmentInteractionListener;
import dev.pavelka.logbook.ui.main.drives.DrivesContent;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DrivesContent.DriveItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDriveRecyclerViewAdapter extends RecyclerView.Adapter<MyDriveRecyclerViewAdapter.ViewHolder> {

    private final List<DrivesContent.DriveItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyDriveRecyclerViewAdapter(OnListFragmentInteractionListener listener) {
        mValues = new ArrayList<>();
        mListener = listener;
    }

    public void addItem(DrivesContent.DriveItem item) {
        DrivesContent.addItem(item);
        mValues.add(item);
        Collections.sort(mValues);
        Collections.reverse(mValues);
        notifyDataSetChanged();
    }

    public void fillByDate(Date from, Date to) {
        mValues.clear();

        for(DrivesContent.DriveItem item : DrivesContent.ITEMS) {
            if (item.from_datetime.compareTo(from) >= 0 && item.from_datetime.compareTo(to) <= 0) {
                mValues.add(item);
            }
        }

        notifyDataSetChanged();
    }

    public void clearList() {
        mValues.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_drive, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d.M. HH:mm");

        holder.mItem = mValues.get(position);
        holder.mFromDateView.setText(dateFormat.format(mValues.get(position).from_datetime));
        holder.mToDateView.setText(dateFormat.format(mValues.get(position).to_datetime));
        holder.mFromView.setText(mValues.get(position).from);
        holder.mToView.setText(mValues.get(position).to);
        holder.mDistanceView.setText(new DecimalFormat("#.##").format(mValues.get(position).distance) + " km");
        holder.mPriceView.setText(new DecimalFormat("#.##").format(mValues.get(position).price) + " Kč");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Ask to remove
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Smazat záznam");
                alert.setMessage("Opravdu chcete smazat tento záznam?");
                alert.setPositiveButton("ANO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DrivesContent.removeItem(mValues.get(position));
                        mValues.remove(mValues.get(position));
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mValues.size());
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("NE", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CardView mCardView;
        public final TextView mFromDateView;
        public final TextView mToDateView;
        public final TextView mFromView;
        public final TextView mToView;
        public final TextView mDistanceView;
        public final TextView mPriceView;
        public DrivesContent.DriveItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFromDateView = (TextView) view.findViewById(R.id.label_from_datetime);
            mToDateView = (TextView) view.findViewById(R.id.label_to_datetime);
            mFromView = (TextView) view.findViewById(R.id.label_from_value);
            mToView = (TextView) view.findViewById(R.id.label_to_value);
            mDistanceView = (TextView) view.findViewById(R.id.label_distance_value);
            mPriceView = (TextView) view.findViewById(R.id.label_price_value);
            mCardView = (CardView) view.findViewById(R.id.card_drive);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFromView.getText() + "'";
        }
    }
}
