package com.k14bktpm.daihieptn97.answerquestions;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.k14bktpm.daihieptn97.answerquestions.CauHoiKhac.OtherQuestion;
import com.k14bktpm.daihieptn97.answerquestions.DangNhap.Login;
import com.k14bktpm.daihieptn97.answerquestions.Database.Database_Account;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText edtQuestion;
    private Button btnSubmit;
    private TextView tvHistory, tvOthersQuestion, tvAnswer;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ImageView imgYes, imgNo;
    private Animation animHiddenLeft, animHiddenRight, aniMove, animDelay, animDelayUnder;
    private List<String> listLangues = new ArrayList<>();
    private String mUsername;
    private RelativeLayout layout;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogout:
                Intent intent = new Intent(MainActivity.this, Login.class);

                Database_Account database = Database_Account.getDatbase_account(this);
                database.Open_Database();
                database.Delete_Account();

                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincontent);
        getIntentdata();
        anhXa();
        setSpinner();
        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-8955613125144237/2876700855"); // quang cao dang bannner

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (!ConnectInternet.isOnline())
            Toasty.warning(MainActivity.this, getString(R.string.noConnectbtnSubmit), Toast.LENGTH_LONG).show();


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onSubmit();
                // checkQuestion();
                //  demo();
            }
        });

        tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectInternet.isOnline()) onHistory();
                else
                    Toasty.error(MainActivity.this, getString(R.string.warningNoConnect), Toast.LENGTH_SHORT).show();
            }
        });

        tvOthersQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectInternet.isOnline()) onOrtherQuestion();
                else
                    Toasty.error(MainActivity.this, getString(R.string.warningNoConnect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onOrtherQuestion() {

        Intent intent = new Intent(MainActivity.this, OtherQuestion.class);
        intent.putExtra("Email", mUsername);
        intent.putExtra("Language", listLangues.get(spinner.getSelectedItemPosition()));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    private void getIntentdata() {
        Intent intent = getIntent();
        mUsername = intent.getStringExtra("Username");
    }

    private void onHistory() {
        Intent intent = new Intent(MainActivity.this, History.class);
        intent.putExtra("Email", mUsername);
        intent.putExtra("Language", listLangues.get(spinner.getSelectedItemPosition()));
        startActivity(intent);
    }

    private void onSubmit() {
        if (isEmptyInput() == 0) { // kiem tra neu tat ca deu da nhap thi bat dau chay

            imgYes.setVisibility(View.VISIBLE);
            imgNo.setVisibility(View.VISIBLE);
            imgNo.startAnimation(animDelay);
            imgYes.startAnimation(animDelayUnder);


            Handler handler = new Handler();

            String cauHoiChuanhoa = edtQuestion.getText().toString().trim(); // chuan hoa chuoi va them dau hoi cuoi cau
            cauHoiChuanhoa = themDauHoi(cauHoiChuanhoa);
            edtQuestion.setText(cauHoiChuanhoa);


            if (ConnectInternet.isOnline()) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDatabase.child("NguoiDung").child(mUsername).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                upLoadData();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }, 4000);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        upLoadData();
                    }
                }, 4000);
            }
        }
    }

    private int isEmptyInput() {
        if (edtQuestion.getText().toString().trim().length() == 0) {
            edtQuestion.setError(getResources().getString(R.string.setErrorQuestion));
            return 1;
        }
        return 0;
    }

    private void upLoadData() {

        int ran = randomAnsers();
        Question question = new Question();
        question.setTheQuestion(edtQuestion.getText().toString().trim());

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String strDate = dateFormat.format(date);

        question.setTime(strDate);

        question.setYesOrNo(ran);

        if (ran == 1) { // kiem tra random va hieu ung an hien img
            imgYes.startAnimation(animHiddenLeft);
            imgNo.startAnimation(aniMove);
            imgYes.setVisibility(View.GONE);

        } else { // day la yes
            imgNo.startAnimation(animHiddenRight);
            imgYes.startAnimation(aniMove);
            imgNo.setVisibility(View.GONE);

        }
        if (ConnectInternet.isOnline()) {
            mDatabase.child("Question").child(listLangues.get(spinner.getSelectedItemPosition()) + "").child(mUsername).push().setValue(question);
        }
        edtQuestion.setHint(edtQuestion.getText().toString().trim());
        edtQuestion.setText("");
    }

    private String themDauHoi(String chuoi) {
        chuoi = chuoi.replaceAll("\\s+", " ");

        if (chuoi.charAt(chuoi.length() - 1) == '?')
            return chuoi;
        return chuoi + " ?";
    }

    private int randomAnsers() {
        Random random = new Random();
        return random.nextInt(2);
    }

    private void setSpinner() {

        listLangues.add("English");
        listLangues.add("Tiếng Việt");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listLangues);
        spinner.setAdapter(adapter);
        if (Locale.getDefault().getLanguage().equals("vi")) {
            spinner.setSelection(1);
        } else spinner.setSelection(0);
    }


    private void checkQuestion() {
//        imgYes.setVisibility(View.VISIBLE);
//        imgNo.setVisibility(View.VISIBLE);

        tvAnswer.setVisibility(View.GONE);
        //layout.setBackground(getResources().getDrawable(R.drawable.background_trang));
        imgNo.setAlpha(1);
        imgYes.setAlpha(1);
        imgNo.startAnimation(animDelay);
        imgYes.startAnimation(animDelayUnder);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatabase.child("cauhoi").child(edtQuestion.getText().toString().trim()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try { // cau hoi ton tai thi set cau hoi nen man hinh
                            String a = dataSnapshot.getValue().toString();
                            imgNo.setAlpha(0);
                            imgYes.setAlpha(0);
                            imgYes.setVisibility(View.GONE);
                            imgNo.setVisibility(View.GONE);

                            layout.setBackground(getResources().getDrawable(R.drawable.background_cautraloi));

                            // Toast.makeText(MainActivity.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                            tvAnswer.setVisibility(View.VISIBLE);
                            tvAnswer.setText(a);
                        } catch (Exception e) { //câu hỏi không tồn tại thì set ảnh như bình thường
                            onSubmit();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }, 4000);
    }

    private void anhXa() {
        spinner = (Spinner) findViewById(R.id.spLangues);
        edtQuestion = (EditText) findViewById(R.id.edtQuestion);
//        edtUsename = (EditText) findViewById(R.id.edtUsernameLogin);
//        edtPassword = (EditText) findViewById(R.id.edtPasswordLogin);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        tvHistory = (TextView) findViewById(R.id.tvHistory);
        tvOthersQuestion = (TextView) findViewById(R.id.tvOthersQuetion);
        imgYes = (ImageView) findViewById(R.id.imgYes);
        imgNo = (ImageView) findViewById(R.id.imgNo);

        animHiddenLeft = AnimationUtils.loadAnimation(this, R.anim.anim_hiddenleft);
        animHiddenRight = AnimationUtils.loadAnimation(this, R.anim.anim_hiddenright);
        aniMove = AnimationUtils.loadAnimation(this, R.anim.anim_move);
        animDelay = AnimationUtils.loadAnimation(this, R.anim.anim_delay);
        animDelayUnder = AnimationUtils.loadAnimation(this, R.anim.anim_delayunder);
        tvAnswer = (TextView) findViewById(R.id.tvAnswer);
        layout = (RelativeLayout) findViewById(R.id.layoutCauTraLoi);

    }


}
