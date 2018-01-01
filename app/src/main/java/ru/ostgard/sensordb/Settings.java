package ru.ostgard.sensordb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private String temp_IP, temp_DB, temp_Login,temp_Pass;

    EditText set_et_ip_address,set_et_db_name, set_et_user_login, set_et_user_password;
    Button set_btn_check_connection, set_btn_save_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        set_et_ip_address = (EditText) findViewById(R.id.set_ip_adress);
        set_et_db_name = (EditText) findViewById(R.id.set_db_name);
        set_et_user_login = (EditText) findViewById(R.id.set_login);
        set_et_user_password = (EditText) findViewById(R.id.set_password);

        set_btn_check_connection = (Button) findViewById(R.id.btn_check_connection);
        set_btn_save_settings = (Button) findViewById(R.id.btn_save_settings);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
             temp_IP = prefs.getString("pref_ip_address", "192.168.110.70");//"No name defined" is the default value.
             temp_DB = prefs.getString("pref_db_name", "db_production");
             temp_Login = prefs.getString("pref_user_login", "serviceman");
             temp_Pass = prefs.getString("pref_user_password", "12321");
        }else
        {
             temp_IP = "192.168.110.40";//"No name defined" is the default value.
             temp_DB = "db_production";
             temp_Login = "serviceman";
             temp_Pass = "12321";

        }

        set_et_ip_address.setText(temp_IP);
        set_et_db_name.setText(temp_DB);
        set_et_user_login.setText(temp_Login);
        set_et_user_password.setText(temp_Pass);


        set_btn_save_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("text","exist");
                editor.putString("pref_ip_address",set_et_ip_address.getText().toString());
                editor.putString("pref_db_name",set_et_db_name.getText().toString());
                editor.putString("pref_user_login",set_et_user_login.getText().toString());
                editor.putString("pref_user_password",set_et_user_password.getText().toString());
                editor.apply();

                Toast.makeText(Settings.this,"Settings saved!",Toast.LENGTH_SHORT).show();
                finish();

            }
        });



    }

}
