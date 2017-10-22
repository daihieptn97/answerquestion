package com.k14bktpm.daihieptn97.answerquestions.CauHoiKhac;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.k14bktpm.daihieptn97.answerquestions.History;
import com.k14bktpm.daihieptn97.answerquestions.R;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;


import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class OtherQuestion extends AppCompatActivity {
    private String keyLanguage;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private final String keyQuestion = "Question";
    private ArrayList<OjectOtherQuseston> listOtherQuestion = new ArrayList<>();
    private adpter_otherQuestion adpterListView;
    private ListView lvOtherQuestion;
    private SpotsDialog dialog;
    private SwipeRefreshLayout refreshLayout;
    // private ArrayList<OjectOtherQuseston> listOtherRandom = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_question);
        anhXa();
        getIntenData();
        this.setTitle(getString(R.string.tilteOtherQuestion));

        dialog.show();

        LoadDataFirebase();
        Load();


        refreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.BLACK);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Load();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        lvOtherQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(OtherQuestion.this, "position : " + i, Toast.LENGTH_SHORT).show();

                OjectOtherQuseston ojectOtherQuseston = listOtherQuestion.get(i);
                //Log.d("usename ", ojectOtherQuseston.getmUsename());

                Intent intent = new Intent(OtherQuestion.this, History.class);
                intent.putExtra("Email", ojectOtherQuseston.getmUsename());
                intent.putExtra("Language", keyLanguage);
                intent.putExtra("Title", getResources().getString(R.string.titleHistory) + " " + ojectOtherQuseston.getmUsename());

                startActivity(intent);
            }
        });
    }

    private void Load() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (listOtherQuestion.size() == 0)
                    Toasty.warning(OtherQuestion.this, getResources().getString(R.string.emptyDataHistory), Toast.LENGTH_SHORT).show();

                Collections.sort(listOtherQuestion, new Comparator<OjectOtherQuseston>() { // sap xep araylist
                    @Override
                    public int compare(OjectOtherQuseston o1, OjectOtherQuseston o2) {
                        //return o2.getThoiGianDang().compareTo(o1.getThoiGianDang());
                        Log.d("day le test", sortTime(o1.getThoiGianDang(), o2.getThoiGianDang()) + "");
                        return sortTime(o1.getThoiGianDang(), o2.getThoiGianDang()); // goi ham sep xep thoi gian
                    }
                });


                adpterListView = new adpter_otherQuestion(listOtherQuestion, OtherQuestion.this, R.layout.adapter_other_queston);
                lvOtherQuestion.setAdapter(adpterListView);
                adpterListView.notifyDataSetChanged();
                dialog.dismiss();
            }
        }, 3000);
    }

    private int sortTime(Date o1, Date o2) { // nhằm sắp xếp thời gian
        if (o1.before(o2)) return 1;
        return -1;
    }


    private void LoadDataFirebase() {
        // Log.d("key firebase ", keyLanguage);
        mDatabase.child(keyQuestion).child(keyLanguage).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //  Log.d("key firebase :", snapshot.getKey());

                    mDatabase.child(keyQuestion).child(keyLanguage).child(snapshot.getKey()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            OjectOtherQuseston ojectOtherQuseston = dataSnapshot.getValue(OjectOtherQuseston.class);
                            ojectOtherQuseston.setmUsename(snapshot.getKey());

                            Date startDate;
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                startDate = df.parse(ojectOtherQuseston.getTime());
                                ojectOtherQuseston.setThoiGianDang(startDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            listOtherQuestion.add(ojectOtherQuseston);


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


                }
                Log.d("key firebase", listOtherQuestion.size() + "");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void anhXa() {
        lvOtherQuestion = (ListView) findViewById(R.id.lvOtherQuestion);
    }

    private void getIntenData() {
        Intent intent = getIntent();
        keyLanguage = intent.getStringExtra("Language");
        dialog = new SpotsDialog(OtherQuestion.this, R.style.CustomDialog);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.reloadDataOtherQuestion);
    }
}


//    private void LoadRandom() {
//        listOtherRandom.clear();
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ListIterator<OjectOtherQuseston> iterator = listOtherQuestion.listIterator();
//                List<Integer> listLocation = new ArrayList<Integer>();
//                Random random = new Random();
//                int dem = 0;
//                int i = 0;
//
//                while (dem <= 35) {
//                    if (i == listOtherQuestion.size()) break;
//                    int temp = random.nextInt(listOtherQuestion.size());
//                    if (!listLocation.contains(temp)) {
//                        listLocation.add(temp);
//                        dem++;
//                    }
//                    i++;
//                }
//
//                int j = 0;
//                while (iterator.hasNext()) {
//                    if (j == 34) break;
//                    if (j == listLocation.size() - 1) break;
//
//                    int a = listLocation.get(j);
//                    OjectOtherQuseston b = listOtherQuestion.get(a);
//                    listOtherRandom.add(b);
//                    j++;
//                }
//
//                if (listOtherQuestion.size() == 0)
//                    Toasty.warning(OtherQuestion.this, getResources().getString(R.string.emptyDataHistory), Toast.LENGTH_SHORT).show();
//                adpterListView = new adpter_otherQuestion(listOtherRandom, OtherQuestion.this, R.layout.adapter_other_queston);
//                lvOtherQuestion.setAdapter(adpterListView);
//                adpterListView.notifyDataSetChanged();
//                dialog.dismiss();
//            }
//        }, 1500);
//    }
