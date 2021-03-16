package com.example.browser.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.browser.R;
import com.example.browser.model.extra;

import org.w3c.dom.Text;

import java.util.ArrayList;

import io.realm.Realm;

public class PrivateFolderPasswordEnter extends AppCompatActivity implements View.OnClickListener {

    ArrayList<ImageView> lst = new ArrayList<>();

    String saved = "";
    int state;
    TextView textView;
    int nextPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_folder);
        textView = ((TextView) findViewById(R.id.enterPasswordText));

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                extra obj = realm.where(extra.class).findFirst();
                saved = obj.getPrivateFolderPass();
            }
        });



        findViewById(R.id.backEnterPinPrivateFolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(extra.class).findFirst().setPrivateFolderPass("");
                    }
                });
            }
        });
        if (saved.equals("")) {
            state = 1;
            textView.setText("Enter New PIN");
        } else if (!saved.equals("")) {
            state = 2;
            textView.setText("Enter Password PIN");
        }

        lst.add(findViewById(R.id.i1));
        lst.add(findViewById(R.id.i2));
        lst.add(findViewById(R.id.i3));
        lst.add(findViewById(R.id.i4));

        initViews();
    }

    private void initViews() {
        findViewById(R.id.t9_key_0).setOnClickListener(this);
        findViewById(R.id.t9_key_1).setOnClickListener(this);
        findViewById(R.id.t9_key_2).setOnClickListener(this);
        findViewById(R.id.t9_key_3).setOnClickListener(this);
        findViewById(R.id.t9_key_4).setOnClickListener(this);
        findViewById(R.id.t9_key_5).setOnClickListener(this);
        findViewById(R.id.t9_key_6).setOnClickListener(this);
        findViewById(R.id.t9_key_7).setOnClickListener(this);
        findViewById(R.id.t9_key_8).setOnClickListener(this);
        findViewById(R.id.t9_key_9).setOnClickListener(this);
        findViewById(R.id.t9_key_clear).setOnClickListener(this);
        findViewById(R.id.t9_key_backspace).setOnClickListener(this);
    }

    String pass = "";

    @Override
    public void onClick(View v) {
        String txt = ((TextView) v).getText().toString();

        if (txt.equals("Clear")) {
            for (ImageView imageView : lst) {
                pass = "";
                nextPos=0;
                imageView.setBackground(getDrawable(R.drawable.selected_item_bg));
            }
        } else if (txt.equals("Back")) {
            pass = pass.substring(0, pass.length() - 1);
            lst.get(--nextPos).setBackground(getDrawable(R.drawable.selected_item_bg));
        } else {
            pass += txt;
            lst.get(nextPos++).setBackground(getDrawable(R.drawable.white));
        }
        if (pass.length() == 4) {
            enteredPassword();
        }

    }

    String fpass;

    private void enteredPassword() {
        if (state == 1) {
            fpass = pass;
            pass =  "";
            nextPos = 0;
            textView.setText("ReEnter PIN");
            for (ImageView i : lst) {
                i.setBackground(getDrawable(R.drawable.selected_item_bg));
            }
            state = 3;
        } else if (state == 3) {
            if (fpass.equals(pass)){
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(extra.class).findFirst().setPrivateFolderPass(pass);
                    }
                });
                gotoPage();
            }else{
                pass = "";
                fpass ="";
                state = 1;
                textView.setText("Password Dont Match !");
                for (ImageView i : lst) {
                    i.setBackground(getDrawable(R.drawable.selected_item_bg));
                }
                nextPos=0;
                Toast.makeText(this, "Password dont match !", Toast.LENGTH_SHORT).show();
            }
        } else if (state == 2) {
            if (pass.equals(saved)) {
                gotoPage();
            } else {
                nextPos = 0;
                textView.setText("Incorrect Password !");
                pass = "";
                fpass ="";
                for (ImageView i : lst) {
                    i.setBackground(getDrawable(R.drawable.selected_item_bg));
                }
            }
        }
    }

    private void gotoPage() {
        startActivity(new Intent(this,PrivateFolderMain.class));
    }
}