package com.androidworks.nikhil.familytreeadmin.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.androidworks.nikhil.familytreeadmin.R;
import com.androidworks.nikhil.familytreeadmin.data.DataStore;
import com.androidworks.nikhil.familytreeadmin.data.model.Member;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "NIK";
    StorageReference storageRef;
    DatabaseReference myRef;
    FirebaseStorage storage;
    FirebaseDatabase database;
    @BindView(R.id.et_birth_year)
    EditText birth;
    @BindView(R.id.et_death)
    EditText death;
    @BindView(R.id.et_generation)
    EditText generation;
    @BindView(R.id.et_location)
    EditText location;
    @BindView(R.id.et_name)
    EditText name;
    @BindView(R.id.et_nick_name)
    EditText nickName;
    @BindView(R.id.bt_add)
    Button add;
    @BindView(R.id.ck_is_dead)
    CheckBox isDead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Member member = new Member();
                member.setBirthYear(Integer.parseInt(birth.getText().toString()));
                member.setDead(isDead.isChecked());
                member.setGeneration(Integer.parseInt(generation.getText().toString()));
                member.setDeathYear(Integer.parseInt(death.getText().toString()));
                member.setName(name.getText().toString());
                member.setNickName(nickName.getText().toString());

                DataStore.getInstance(MainActivity.this).storeMembers(member);
                myRef.setValue(DataStore.getInstance(MainActivity.this).getMembersJSON());
            }
        });

    }

    private void init() {
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://nivezzle.appspot.com/");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("FAMILY");
//        progressBar.setMax(100);
//        progressBar.setProgress(0);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                DataStore.getInstance(MainActivity.this).setMembersJSON(value);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
