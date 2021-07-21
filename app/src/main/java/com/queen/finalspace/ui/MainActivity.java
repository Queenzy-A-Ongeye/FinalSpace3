package com.queen.finalspace.ui;

import androidx.annotation.NonNull;
import  androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.queen.finalspace.Constants;
import com.queen.finalspace.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
   // private SharedPreferences mSharedPreferences;
    //private SharedPreferences.Editor mEditor;
    private ValueEventListener mSearchedLocationReferenceListener;
    private DatabaseReference mSearchedLocationReference;

    @BindView(R.id.findseasonsButton) Button mFindSeasonButton;
    @BindView(R.id.findEditText) EditText mFindEditText;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSearchedLocationReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.FIREBASE_CHILD_SEARCHED_LOCATION);

        mSearchedLocationReferenceListener = mSearchedLocationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()){
                   Log.d("Locations updated", "location: " + locationSnapshot.getValue().toString());
               }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

       // mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      //  mEditor = mSharedPreferences.edit();

        mFindSeasonButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v == mFindSeasonButton) {
            String location = mFindEditText.getText().toString();
            saveLocationToFireBase(location);

            Toast.makeText(MainActivity.this, "Watch Out!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, FindActivity.class);
            intent.putExtra("location", location);
            startActivity(intent);
        }
    }

    public void saveLocationToFireBase(String  location){
        mSearchedLocationReference.setValue(location);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchedLocationReference.removeEventListener(mSearchedLocationReferenceListener);
    }
}