package com.k14bktpm.daihieptn97.answerquestions.DangNhap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.k14bktpm.daihieptn97.answerquestions.ConnectInternet;
import com.k14bktpm.daihieptn97.answerquestions.Database.Account;
import com.k14bktpm.daihieptn97.answerquestions.Database.Database_Account;
import com.k14bktpm.daihieptn97.answerquestions.MainActivity;
import com.k14bktpm.daihieptn97.answerquestions.R;


import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class Login extends AppCompatActivity {
    private Button btnLogin, btnCreateAcount;
    private EditText edtUsername, edtPassword;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String mUsername, mPassword;
    private Database_Account datbase_account;
    private SpotsDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();

        khoiChay();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                mPassword = edtPassword.getText().toString().trim();
                mUsername = edtUsername.getText().toString().trim();
                if (ConnectInternet.isOnline()) DangNhap();
                else
                    Toasty.error(Login.this, getString(R.string.warningNoConnect), Toast.LENGTH_SHORT).show();
            }
        });

        btnCreateAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Intent intent = new Intent(Login.this, CreateAccount.class);
                startActivity(intent);
            }
        });


    }

    private void khoiChay() {
        if (!datbase_account.isEmpty()) {
            dialog.show();
            Account account = datbase_account.LoadAccount();
            autoDangNhap(account.getMail());
        }
    }

    public void DangNhap() {
        if (isEmptyInput() == 0) { // kiem tra neu tat ca deu da nhap thi bat dau chay
            dialog.show();
            mPassword = edtPassword.getText().toString().trim();
            mUsername = edtUsername.getText().toString().trim().toLowerCase();
            mDatabase.child("NguoiDung").child(mUsername).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue().toString().equals(mPassword)) { // neu ten ton tai thi chay
                            inertDatabase();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.putExtra("Username", mUsername);
                            startActivity(intent);
                            finish(); // ket thuc activity

                        } else {
                            Toasty.error(Login.this, getResources().getString(R.string.errorLogin), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    } catch (Exception e) { // con khong ton tai thi tao tenh moi
                        Toasty.error(Login.this, getResources().getString(R.string.errorLogin), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public void autoDangNhap(String mUsername) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.putExtra("Username", mUsername);
        startActivity(intent);
        dialog.dismiss();
        finish(); // ket thuc activity
    }

    private void inertDatabase() {
        if (!datbase_account.isExits(edtUsername.getText().toString())) { // kiem tra email da ton tai trong databse chua, chua thi luu lai, con co roi thi thoi
            Account account = new Account();
            account.setMail(edtUsername.getText().toString().trim());
            account.setPassword(edtPassword.getText().toString().trim());
            datbase_account.Insert_Datbase_Account(account);
        }
    }

    public int isEmptyInput() {

        if (edtUsername.getText().toString().trim().length() == 0) {
            edtUsername.setError(getResources().getString(R.string.setErrorUsename));
            return 1;
        }

        if (edtPassword.getText().toString().trim().length() == 0) {
            edtPassword.setError(getResources().getString(R.string.setErrorPassword));
            return 1;
        }

        return 0;
    }


    private void anhXa() {
        datbase_account = Database_Account.getDatbase_account(this);
        datbase_account.Open_Database();
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCreateAcount = (Button) findViewById(R.id.btnCreateAccount);
        edtPassword = (EditText) findViewById(R.id.edtPasswordLogin);
        edtUsername = (EditText) findViewById(R.id.edtUsernameLogin);
        dialog = new SpotsDialog(this, R.style.CustomDialog);
    }
}
