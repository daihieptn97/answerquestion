package com.k14bktpm.daihieptn97.answerquestions.DangNhap;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import es.dmoral.toasty.Toasty;

public class CreateAccount extends AppCompatActivity {
    private EditText edtUsename, edtPassword, edtRepassword;
    private Button btnCreateAccount;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Database_Account datbase_account;

    private TextInputLayout tipUsename, tipPassword, tipRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        anhxa();

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                if (ConnectInternet.isOnline()) {
                    if (!isEmpty() && checkFormatTetxt()) { // neu khong trong thi dang nhaop
                        if (checkPassOrRePass()) { // neu trung nhau thi chay
                            createAccount();
                        }
                    }
                } else
                    Toasty.error(CreateAccount.this, getString(R.string.warningNoConnect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAccount() {
        mDatabase.child("NguoiDung").child(edtUsename.getText().toString().trim()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue().toString().length() > 0) {
                        tipUsename.setError(getResources().getString(R.string.accountExits));
                        Toast.makeText(CreateAccount.this, getResources().getString(R.string.accountExits), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {

                    tipUsename.setError("");
                    Toasty.success(CreateAccount.this, getResources().getString(R.string.successCrateLogin), Toast.LENGTH_SHORT).show();
                    inertDatabase();
                    mDatabase.child("NguoiDung").child(edtUsename.getText().toString().trim()).setValue(edtPassword.getText().toString().trim());
                    Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                    intent.putExtra("Username", edtUsename.getText().toString().trim());
                    startActivity(intent);
                    finish(); // ket thuc activity
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inertDatabase() {

        if (!datbase_account.isExits(edtUsename.getText().toString().trim())) { // kiem tra email da ton tai trong databse chua, chua thi luu lai, con co roi thi thoi
            Account account = new Account();
            account.setMail(edtUsename.getText().toString().trim());
            account.setPassword(edtPassword.getText().toString().trim());
            datbase_account.Insert_Datbase_Account(account);
        }
    }


    private void anhxa() {
        edtUsename = (EditText) findViewById(R.id.edtUsernameCreate);
        edtRepassword = (EditText) findViewById(R.id.edtConfirmYourPasswordCreate);
        edtPassword = (EditText) findViewById(R.id.edtPasswordCreate);
        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        datbase_account = Database_Account.getDatbase_account(this);
        datbase_account.Open_Database();


        tipPassword = (TextInputLayout) findViewById(R.id.tipPassword);
        tipRePassword = (TextInputLayout) findViewById(R.id.tipRepassword);
        tipUsename = (TextInputLayout) findViewById(R.id.tipUsername);
    }

    public boolean isEmpty() {
        if (edtUsename.getText().toString().trim().length() <= 0) {
            tipUsename.setError(getResources().getString(R.string.setErrorUsename));
            return true;
        } else {
            tipUsename.setError("");
        }

        if (edtPassword.getText().toString().trim().length() <= 0) {
            tipPassword.setError(getResources().getString(R.string.setErrorPassword));
            return true;
        } else {
            tipPassword.setError("");
        }


        if (edtRepassword.getText().toString().trim().length() <= 0) {
            tipRePassword.setError(getResources().getString(R.string.setErrorEmptyEditText));
            return true;
        } else {
            tipRePassword.setError("");
        }

        return false;
    }

    public boolean checkPassOrRePass() {
        if (!edtPassword.getText().toString().trim().equals(edtRepassword.getText().toString().trim())) {
            tipRePassword.setError(getResources().getString(R.string.setErrorRePassword));
            return false;
        }
        return true;

    }

    private boolean checkFormatTetxt() {
        String regexUsername = "^[a-zA-Z0-9._-]{0,}$";
        if (!edtUsename.getText().toString().trim().matches(regexUsername)
                || !edtPassword.getText().toString().trim().matches(regexUsername)) {
            tipUsename.setError(getString(R.string.warningInputText));
            return false;
        }

        if (edtUsename.getText().toString().trim().length() < 5 && edtUsename.getText().toString().trim().length() > 30) {
            tipUsename.setError(getString(R.string.warningLenghtText));
            return false;
        }
        if (edtPassword.getText().toString().trim().length() < 5 && edtPassword.getText().toString().trim().length() > 30) {
            tipUsename.setError(getString(R.string.warningLenghtText));
            return false;
        }


        return true;
    }
}
