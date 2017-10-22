package com.k14bktpm.daihieptn97.answerquestions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class History extends AppCompatActivity {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private final String keyQuestion = "Question";
    private String keyEmail, keyLanguage, keyTitle;
    private ArrayList<Question> listQuestion = new ArrayList<>();
    private adpterListView adpterListView;
    private ListView lvHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getIntenData();
        if(keyTitle != null) this.setTitle(keyTitle);
        else this.setTitle(getResources().getString(R.string.titleHistory));

        lvHistory = (ListView) findViewById(R.id.lvHistory);

        LoadDataFirebase();

    }

    private void LoadDataFirebase() {
        mDatabase.child(keyQuestion).child(keyLanguage).child(keyEmail).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Question question = dataSnapshot.getValue(Question.class);
                listQuestion.add(question);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

        mDatabase.child(keyQuestion).child(Locale.getDefault().getLanguage()).child(keyEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (listQuestion.size() == 0)
                    Toasty.warning(History.this, getResources().getString(R.string.emptyDataHistory), Toast.LENGTH_SHORT).show();
                Collections.reverse(listQuestion);
                adpterListView = new adpterListView(listQuestion, History.this, R.layout.adapter_history);
                lvHistory.setAdapter(adpterListView);
                adpterListView.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getIntenData() {
        Intent intent = getIntent();
        keyEmail = intent.getStringExtra("Email");
        keyLanguage = intent.getStringExtra("Language");
        keyTitle = intent.getStringExtra("Title");
    }
}
