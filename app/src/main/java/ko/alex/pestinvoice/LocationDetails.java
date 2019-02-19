package ko.alex.pestinvoice;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LocationDetails extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) { // removed @Nullable Bundle savedInstanceState
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_details);

        toolbar = findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(),"Item One");
        adapter.addFragment(new TwoFragment(),"Item Two");
        adapter.addFragment(new ThreeFragment(),"Item Three");

        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabLayout_id);
        tabLayout.setupWithViewPager(viewPager);
    }




} // End LocationDetails method






















