package ko.alex.pestinvoice;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // Don't forget to add google-services.json for firebase capability!
    // Firebase tutorial youtube: https://www.youtube.com/watch?v=ImNs-z872ck

    private Button addLocationButton;
    private ListView locationListView;

    int RC_SIGN_IN = 123;

    /* This is EnigmaChat3's mainactivity, before entering the specific chat room
    * private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    *
    * add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put(room_name.getText().toString(),"");
                root.updateChildren(map);
            }
        });
    * */
    // "push" creates a unique java object, here in PestInvoice it is "locations" with a unique key
    // root = FirebaseDatabase.getInstance().getReference().child(room_name); EnigmaChat3 chatroom activity
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    private EditText name;
    private EditText address;
    private EditText phoneNumber;
    private EditText email;

    private location locationItemSelected; // this is for the long press on location item in listview

    private String locationNameAlertDialog;
    private String locationAddressAlertDialog;
    private String locationPhoneNumberAlertDialog;
    private String locationEmailAlertDialog;

    private FirebaseListAdapter<location> adapter;
    private TextView locationName;
    private TextView locationPhone;
    private TextView locationAddress;
    private TextView locationEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationListView = findViewById(R.id.locationListView);

        root = FirebaseDatabase.getInstance().getReference().getRoot();

        displayLocations();


        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                displayLocations();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                displayLocations();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    } // End onCreate method


    private void displayLocations(){
        ListView locationListView = findViewById(R.id.locationListView);

        // https://stackoverflow.com/questions/47690974/firebaselistadapter-not-working
        Query query = root;
        // https://stackoverflow.com/questions/46912532/android-using-firebaseui-with-firebaselistoptions-to-populate-a-listview-doesnt THE KEY WAS TO ADD .SETLIFECYCLEOWNER(THIS)!!!
        FirebaseListOptions<location> options = new FirebaseListOptions.Builder<location>().setQuery(query, location.class).setLayout(R.layout.location).setLifecycleOwner(this).build();

        adapter = new FirebaseListAdapter<location>(options){
            @Override
            protected void populateView(View v, location model, int position) {

                // Get references to the views of location.xml
                locationName = v.findViewById(R.id.location_name);
                locationPhone = v.findViewById(R.id.location_phone);
                locationAddress = v.findViewById(R.id.location_address);
                locationEmail = v.findViewById(R.id.location_email);

                // Set their text
                locationName.setText(model.getlocation_name());
                locationPhone.setText(model.getlocation_phone());
                locationAddress.setText(model.getlocation_address());
                locationEmail.setText(model.getlocation_email());
            }
        };
        locationListView.setAdapter(adapter);

        // If the listview is touched, move into the intent with room name and user name
        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),LocationDetails.class);
                //intent.putExtra("room_name",((TextView)view).getText().toString());
                //intent.putExtra("user_name",name); // not sure what this does
                startActivity(intent);
            }
        });

        // how do i long press to remove an entry from firebase? 5.28.2018
        locationListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // This will come in handy when I need to long press to remove a location...
                // temp_key = root.push().getKey();
                // DatabaseReference message_root = root.child(temp_key);
                locationItemSelected = adapter.getItem(position); // here in case i want to access location attributes such as location name, email, address, or phone

                // https://www.youtube.com/watch?v=2yepe4GYa90 youtube video on deleting an entry from firebase database
                //DatabaseReference datarefLocationDelete = FirebaseDatabase.getInstance().getReference().child(locationItemSelected);
                //datarefLocationDelete.removeValue();

                Toast.makeText(getApplicationContext(), "Item long pressed!", Toast.LENGTH_LONG).show();
                return false; // this is created automatically
            }
        });

    }






    // For the addLocationButton
    public void addLocation(View view){ // View view may not be necessary here

        // https://www.youtube.com/watch?v=plnLs6aST1M
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); // android.R.style.Theme_Black_NoTitleBar_Fullscreen or android.R.style.Theme_Light

        builder.setTitle("Enter location information below:");

        // Need subView because you are referencing the view in custom_alert_dialog.xml, not the main view
        View subView = getLayoutInflater().inflate(R.layout.custom_alert_dialog,null);
        final EditText nameEdit = subView.findViewById(R.id.nameEditText);
        final EditText addressEdit = subView.findViewById(R.id.addressEditText);
        final EditText phoneNumberEdit = subView.findViewById(R.id.phoneNumberEditText);
        final EditText emailEdit = subView.findViewById(R.id.emailEditText);

        builder.setPositiveButton("Sounds good!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                locationNameAlertDialog = nameEdit.getText().toString();
                locationAddressAlertDialog = addressEdit.getText().toString();
                locationPhoneNumberAlertDialog = phoneNumberEdit.getText().toString();
                locationEmailAlertDialog = emailEdit.getText().toString();

                Toast.makeText(getApplicationContext(), "Name is: "+locationNameAlertDialog+ "\nAddress is: "+locationAddressAlertDialog+"\nPhone number is: "+locationPhoneNumberAlertDialog+"\nEmail is: "+locationEmailAlertDialog, Toast.LENGTH_LONG).show();

                // https://stackoverflow.com/questions/42420997/firebase-push-data-on-same-child-using-hashmap "Push literally pushes a new node onto the database"
                // Push will create a new key: https://stackoverflow.com/questions/37483406/setting-custom-key-when-pushing-new-data-to-firebase-database
                root.push().setValue(new location(locationNameAlertDialog, locationPhoneNumberAlertDialog, locationAddressAlertDialog, locationEmailAlertDialog));
                adapter.notifyDataSetChanged(); // https://stackoverflow.com/questions/47500135/how-to-update-a-list-view-in-real-time-firebase-android

                Toast.makeText(getApplicationContext(),"BOOP!",Toast.LENGTH_LONG).show();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss(); // dialog.cancel() can also work
            }
        });

        // For custom alert dialog
        builder.setView(subView);
        AlertDialog dialog = builder.create();
        dialog.show();

    }



} // End MainActivity






















































