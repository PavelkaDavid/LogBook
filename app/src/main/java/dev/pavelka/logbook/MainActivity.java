package dev.pavelka.logbook;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dev.pavelka.logbook.database.Drive;
import dev.pavelka.logbook.ui.main.DrivesFragment;
import dev.pavelka.logbook.ui.main.MyDriveRecyclerViewAdapter;
import dev.pavelka.logbook.ui.main.SectionsPagerAdapter;
import dev.pavelka.logbook.ui.main.drives.DrivesContent;

public class MainActivity extends AppCompatActivity
    implements DrivesFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        DrivesContent.CONTEXT = this;
        DrivesContent.getAllItems();
    }

    @Override
    public void onListFragmentInteraction(DrivesContent.DriveItem item) {

    }
}