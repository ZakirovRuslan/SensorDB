package ru.ostgard.sensordb;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    public String valueDepartement, valueSubdepartement, valueMachine, valueNewNumber, valueOldNumber;
    public String valueLocalNumber, valueSensorType, valueUnits, valueMinRange, valueMaxRange;
    public String valueSensorVendor, valueSensorModel,valueSensorSerialNumber, valueSensorLocationDescription, valueUserName;

    ConnectionClass connectionClass;
    Spinner spnrDepartement,spnrSubDepartement, spnrSensorType, spnrSensorUnits;
    EditText edtNewNumber,edtOldNumber, edtMachineName;
    EditText edtSchemeNumber,edtMinRange,edtMaxRange,edtVendor,edtModel,edtSerialNumber, edtSensorLocation;
    Button btnInsertSensor;
    ProgressBar pbbar;


    private String temp_IP, temp_DB, temp_Login,temp_Pass;

    public void showInforDialog(Context cnt, String mes, String titleText, boolean okButton, boolean cancelButton, boolean neutralButton)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(cnt);


        builder.setTitle(titleText);


        builder.setMessage(mes);


        //Button One : Yes
        if(okButton)
        {
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                 //   Toast.makeText(MainActivity.this, "Yes button Clicked!", Toast.LENGTH_LONG).show();
                }
            });
        }

        if(cancelButton)
        {
            //Button Two : No
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                 //   Toast.makeText(MainActivity.this, "No button Clicked!", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }
            });
        }
        if(neutralButton)
        {
            //Button Three : Neutral
            builder.setNeutralButton("I don't care", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                 //   Toast.makeText(MainActivity.this, "Neutral button Clicked!", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }
            });
        }

        AlertDialog diag = builder.create();
        diag.show();
    }

    public ArrayAdapter<String> setSubSpinner1(String[] arrStrings)
    {
        ArrayAdapter<String> adaptStrings = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,arrStrings);
        adaptStrings.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adaptStrings;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        valueDepartement = "default";
        valueSubdepartement = "default";
        valueMachine= "";
        valueNewNumber= "";
        valueOldNumber= "";
        valueLocalNumber= "";
        valueSensorType= "default";
        valueUnits= "default";
        valueMinRange= "";
        valueMaxRange= "";
        valueSensorVendor= "";
        valueSensorModel= "";
        valueSensorSerialNumber= "";
        valueSensorLocationDescription= "";
        valueUserName = "";

        connectionClass = new ConnectionClass();

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
        //All objects on the activity
        connectionClass.setConnectionSettings(temp_IP,temp_DB,temp_Login,temp_Pass);

        spnrDepartement = findViewById(R.id.spinner_department);
        spnrSubDepartement = findViewById(R.id.spinner_subdepartment);
        spnrSensorType = findViewById(R.id.spinner_sensortype);
        spnrSensorUnits =  findViewById(R.id.spinner_rangetype);

        edtMachineName =findViewById(R.id.edtmachine);
        edtNewNumber = findViewById(R.id.edtnewnumber);
        edtOldNumber =  findViewById(R.id.edtoldnumber);
        edtSchemeNumber =  findViewById(R.id.edtschemenumber);
        edtMinRange =  findViewById(R.id.edtminrange);
        edtMaxRange =  findViewById(R.id.edtmaxrange);
        edtVendor = findViewById(R.id.edtvendor);
        edtModel = findViewById(R.id.edtmodel);
        edtSerialNumber =  findViewById(R.id.edtserial);
        edtSensorLocation = findViewById(R.id.edtdescription);

        btnInsertSensor =  findViewById(R.id.btnInsertSensor);

        pbbar =  findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);

        btnInsertSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String restoredText = prefs.getString("text", null);
                if (restoredText != null) {
                    temp_IP = prefs.getString("pref_ip_address", "192.168.110.70");//"No name defined" is the default value.
                    temp_DB = prefs.getString("pref_db_name", "db_production");
                    temp_Login = prefs.getString("pref_user_login", "serviceman");
                    temp_Pass = prefs.getString("pref_user_password", "12321");
                }
                else
                {
                    temp_IP = "192.168.110.40";
                    temp_DB = "db_production";
                    temp_Login = "serviceman";
                    temp_Pass = "12321";

                }

                connectionClass.setConnectionSettings(temp_IP,temp_DB,temp_Login,temp_Pass);
                InsertSensor doLogin = new InsertSensor();

                valueDepartement = spnrDepartement.getSelectedItem().toString();
                valueSubdepartement = spnrSubDepartement.getSelectedItem().toString();
                valueMachine = edtMachineName.getText().toString();
                valueNewNumber = edtNewNumber.getText().toString();
                valueOldNumber = edtOldNumber.getText().toString();
                valueSensorType = spnrSensorType.getSelectedItem().toString();
                valueLocalNumber =  edtSchemeNumber.getText().toString();
                valueUnits = spnrSensorUnits.getSelectedItem().toString();
                valueMinRange = edtMinRange.getText().toString();
                valueMaxRange = edtMaxRange.getText().toString();
                valueSensorVendor = edtVendor.getText().toString();
                valueSensorModel = edtModel.getText().toString();
                valueSensorSerialNumber = edtSerialNumber.getText().toString();
                valueSensorLocationDescription = edtSensorLocation.getText().toString();
                valueUserName = temp_Login;

                String sqlRequestInsert;

                sqlRequestInsert = "INSERT INTO["+temp_DB+"].[dbo].[SENSORS]";
                sqlRequestInsert = sqlRequestInsert + "([sensor_new_number]";
                sqlRequestInsert = sqlRequestInsert + ",[sensor_old_number]";
                sqlRequestInsert = sqlRequestInsert + ",[location_department]";
                sqlRequestInsert = sqlRequestInsert + ",[location_subdepartement]";
                sqlRequestInsert = sqlRequestInsert + ",[location_machine]";
                sqlRequestInsert = sqlRequestInsert + ",[location_description]";
                sqlRequestInsert = sqlRequestInsert + ",[name_local_on_machine]";
                sqlRequestInsert = sqlRequestInsert + ",[name_object_vendor]";
                sqlRequestInsert = sqlRequestInsert + ",[name_object_model]";
                sqlRequestInsert = sqlRequestInsert + ",[serial_number]";
                sqlRequestInsert = sqlRequestInsert + ",[object_type]";
                sqlRequestInsert = sqlRequestInsert + ",[measurement_units]";
                sqlRequestInsert = sqlRequestInsert + ",[measurement_range_min]";
                sqlRequestInsert = sqlRequestInsert + ",[measurement_range_max]";
                sqlRequestInsert = sqlRequestInsert + ",[user_who_added]";
                sqlRequestInsert = sqlRequestInsert + ")VALUES";
                sqlRequestInsert = sqlRequestInsert + "('"+valueNewNumber+"'";
                sqlRequestInsert = sqlRequestInsert + ",N'"+valueOldNumber+"'";
                sqlRequestInsert = sqlRequestInsert + ",N'"+valueDepartement+"'";
                sqlRequestInsert = sqlRequestInsert + ",N'"+valueSubdepartement+"'";
                sqlRequestInsert = sqlRequestInsert + ",N'"+valueMachine+"'";
                sqlRequestInsert = sqlRequestInsert + ",N'"+valueSensorLocationDescription+"'";
                sqlRequestInsert = sqlRequestInsert + ",N'"+valueLocalNumber+"'";
                sqlRequestInsert = sqlRequestInsert + ",N'"+valueSensorVendor+"'";
                sqlRequestInsert = sqlRequestInsert + ",N'"+valueSensorModel+"'";
                sqlRequestInsert = sqlRequestInsert + ",N'"+valueSensorSerialNumber+"'";
                sqlRequestInsert = sqlRequestInsert + ",N'"+valueSensorType+"'";
                sqlRequestInsert = sqlRequestInsert + ",'"+valueUnits+"'";
                sqlRequestInsert = sqlRequestInsert + ",'"+valueMinRange+"'";
                sqlRequestInsert = sqlRequestInsert + ",'"+valueMaxRange+"'";
                sqlRequestInsert = sqlRequestInsert + ",'"+valueUserName+"')";

                try
                        {
                         doLogin.execute(sqlRequestInsert, valueNewNumber);
                        }
                catch (Exception Ex)
                    {
                        Log.e("ERRO", Ex.getMessage());
                    }

            }
        });

        takeSpinnerDepartementData();
        takeSpinnerSensorTypeData();

        spnrSubDepartement.setPrompt("Title");
        spnrSubDepartement.setSelection(0);

        spnrDepartement.setPrompt("Title");
        spnrDepartement.setSelection(0);

        spnrDepartement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                takeSpinnerSubdepartementData(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {



            }
        });

        spnrSensorType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                takeSpinnerUnitsData(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {



            }
        });

    }

    public boolean UpdateAllSpinners()
    {
        UpdateSoftware getNewData = new UpdateSoftware();

        String sqlRequestSelect = "SELECT spinner, dep, subdep01, subdep02, subdep03, subdep04, subdep05, subdep06, subdep07, subdep08, subdep09, subdep10, subdep11, subdep12, subdep13, subdep14, subdep15, subdep16, subdep17, subdep18, subdep19, subdep20  FROM spinners ORDER BY order_id ASC";

        try
        {
            getNewData.execute(sqlRequestSelect);
            return  true;
        }
        catch (Exception Ex)
        {
            Log.e("ERRO", Ex.getMessage());
            return  false;
        }

    }

    public void takeSpinnerDepartementData()
    {
        Log.e("ERRO", "takeSpinnerDepartementData");
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        if(!prefs.contains("DepStringSet"))
        {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("DepStringSet","object 1,object 2,object 3,object 4");
            edit.apply();
        }


        String set = prefs.getString("DepStringSet", "object 1,object 2,object 3,object 4");
        if(set.equals(""))
        {
            Log.e("ERRO", "Set Departement is empty");
            set = "object 1,object 2,object 3,object 4";
        }

        String[] tempDataForSpinner = set.split(",");

        ArrayAdapter<String> adapterDepartement = setSubSpinner1(tempDataForSpinner);
        spnrDepartement.setAdapter(adapterDepartement);
    }

    public void takeSpinnerSubdepartementData(int pos)
    {
        Log.e("ERRO", "takeSpinnerSubdepartementData");
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        if(!prefs.contains("subDepStringSet"))
        {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("subDepStringSet","object 1,object 2,object 3,object 4");
            edit.apply();
        }


        String set = prefs.getString("subDepStringSet" + String.valueOf(pos), "object 1,object 2,object 3,object 4");

        if(set.equals(""))
        {
            Log.e("ERRO", "Set Departement is empty");
            set = "object 1,object 2,object 3,object 4";
        }

        String[] tempDataForSpinner = set.split(",");

        ArrayAdapter<String> adapterSubDepartement = setSubSpinner1(tempDataForSpinner);
        spnrSubDepartement.setAdapter(adapterSubDepartement);

    }

    public void takeSpinnerSensorTypeData()
    {
        Log.e("ERRO", "takeSpinnerSensorTypeData");
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        if(!prefs.contains("SensorTypeStringSet"))
        {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("SensorTypeStringSet","object 1,object 2,object 3,object 4");
            edit.apply();
        }


        String set = prefs.getString("SensorTypeStringSet", "object 1,object 2,object 3,object 4");
        if(set.equals(""))
        {
            Log.e("ERRO", "Set SensorType is empty");
            set = "object 1,object 2,object 3,object 4";
        }

        String[] tempDataForSpinner = set.split(",");

        ArrayAdapter<String> adapterDepartement = setSubSpinner1(tempDataForSpinner);
        spnrSensorType.setAdapter(adapterDepartement);
    }

    public void takeSpinnerUnitsData(int pos)
    {
        Log.e("ERRO", "takeSpinnerUnitsData");
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        if(!prefs.contains("unitsStringSet"))
        {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("unitsStringSet","object 1,object 2,object 3,object 4");
            edit.apply();
        }


        String set = prefs.getString("unitsStringSet" + String.valueOf(pos), "object_1,object_2,object_3,object_4");

        if(set.equals(""))
        {
            Log.e("ERRO", "Set Units is empty");
            set = "object_1,object_2,object_3,object_4";
        }

        String[] tempDataForSpinner = set.split(",");

        ArrayAdapter<String> adapterSubDepartement = setSubSpinner1(tempDataForSpinner);
        spnrSensorUnits.setAdapter(adapterSubDepartement);

    }

    public class InsertSensor extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
        //    Toast.makeText(MainActivity.this,r,Toast.LENGTH_SHORT).show();
            showInforDialog(MainActivity.this,r,"Добавление датчика",true,false,false);

        }

        @Override
        protected String doInBackground(String... params) {

                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        String tStr = connectionClass.actSettings +"\n";
                        if(!Objects.equals(connectionClass.actExceptionCLASS, ""))
                        {tStr = tStr + "\nClass Exception " + connectionClass.actExceptionCLASS;}
                        if(!Objects.equals(connectionClass.actExceptionSQL, ""))
                        {tStr = tStr + "\nSQL Exception " + connectionClass.actExceptionSQL;}
                        if(!Objects.equals(connectionClass.actExceptionE, ""))
                        {tStr = tStr + "\nException " + connectionClass.actExceptionSQL;}
                        z = tStr;

                    } else {
                        String query = params[0];
                        String checkNumber = "";

                        if(params[1]!=null)
                        {
                            checkNumber = params[1];
                        }
                        String strOut = query.substring(0,7);
                        Statement stmt = con.createStatement();
                        if (strOut.matches("(?i:.*SELECT.*)"))
                        {

                            ResultSet rs = stmt.executeQuery(query);

                            if(rs.next())
                            {

                                z = "Successfull";
                                isSuccess=true;
                            }
                            else
                            {
                                z = "Invalid Credentials";
                                isSuccess = false;
                            }
                        }else
                        {
                            ResultSet checkResult = stmt.executeQuery("SELECT * FROM SENSORS WHERE [sensor_new_number] = '"+checkNumber+"'");

                            int rowCount = 0;
                            while ( checkResult.next() )
                            {
                                rowCount++;
                            }

                            if(rowCount == 0)
                            {
                                stmt.executeUpdate(query);
                                z = "Датчик " + params[1] +
                                        "\nдобавлен в базу данных";
                            }
                            else
                            {
                                z = "Датчик с номером " + params[1] +"\nуже существует в базе\nдобавьте под другим номером";
                            }

                        }

                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    String tStr = connectionClass.actSettings;
                    z = tStr + "\nExceptions: " + ex.getMessage();
                    Log.e("ERRO", ex.getMessage());

                }
            return z;
        }
    }

    public class UpdateSoftware extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            pbbar.setVisibility(View.GONE);
        //    Toast.makeText(MainActivity.this,r,Toast.LENGTH_SHORT).show();
            showInforDialog(MainActivity.this,r,"Обновление",true,false,false);
            takeSpinnerDepartementData();
            takeSpinnerSensorTypeData();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    String tStr = connectionClass.actSettings +"\n";
                    if(!Objects.equals(connectionClass.actExceptionCLASS, ""))
                    {tStr = tStr + "\nClass Exception " + connectionClass.actExceptionCLASS;}
                    if(!Objects.equals(connectionClass.actExceptionSQL, ""))
                    {tStr = tStr + "\nSQL Exception " + connectionClass.actExceptionSQL;}
                    if(!Objects.equals(connectionClass.actExceptionE, ""))
                    {tStr = tStr + "\nException " + connectionClass.actExceptionSQL;}
                    z = tStr;

                } else {
                    String query = params[0];
                    Statement stmt = con.createStatement();


                        ResultSet rs = stmt.executeQuery(query);

                        if (rs != null) {
                            Log.e("ERRO", "RS not null");


                        int columnCount = rs.getMetaData().getColumnCount();
                        int j = 0;
                        int k = 0;

                            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                            String tmpPrefDepartementStringArray = "";
                            String tmpPrefSubdepartementStringArray = "";
                            String tmpPrefTypeStringArray = "";
                            String tmpPrefUnitStringArray = "";

                            SharedPreferences.Editor edit = prefs.edit();

                            String tmpStr = "";
                            String spnrName = "";

                        while (rs.next()) {


                            for (int i = 1; i <= columnCount; i++) {

                                tmpStr = rs.getString(i);
                                if(i == 1)
                                {
                                    spnrName = rs.getString(i);
                                    Log.e("ERRO", rs.getMetaData().getColumnName(i).toString() + " = " + spnrName);

                                }

                                if(i == 2)
                                {
                                    if(spnrName.equals("departement"))
                                    {
                                        if(!tmpStr.equals("")&&!tmpStr.equals(" "))
                                        {
                                            if(tmpPrefDepartementStringArray.equals(""))
                                            {
                                                tmpPrefDepartementStringArray = tmpStr;
                                            }
                                            else
                                            {
                                                tmpPrefDepartementStringArray = tmpPrefDepartementStringArray + "," + tmpStr;
                                            }

                                        }
                                        Log.e("ERRO", rs.getMetaData().getColumnName(i).toString() + " = " + spnrName);
                                    }

                                    if(spnrName.equals("type"))
                                    {
                                        if(!tmpStr.equals("")&&!tmpStr.equals(" "))
                                        {
                                            if(tmpPrefTypeStringArray.equals(""))
                                            {
                                                tmpPrefTypeStringArray = tmpStr;
                                            }
                                            else
                                            {
                                                tmpPrefTypeStringArray = tmpPrefTypeStringArray + "," + tmpStr;
                                            }

                                        }
                                    }

                                }
                                if(i>2)
                                {
                                    if(spnrName.equals("departement"))
                                    {
                                        if(!tmpStr.equals("")&&!tmpStr.equals(" "))

                                        {
                                            if(tmpPrefSubdepartementStringArray.equals(""))
                                            {
                                                tmpPrefSubdepartementStringArray = tmpStr;
                                            }
                                            else
                                            {
                                                tmpPrefSubdepartementStringArray = tmpPrefSubdepartementStringArray + "," + tmpStr;
                                            }
                                            Log.e("ERRO", rs.getMetaData().getColumnName(i).toString() + " = " + tmpStr);
                                        }
                                    }

                                    if(spnrName.equals("type"))
                                    {
                                        if(!tmpStr.equals("")&&!tmpStr.equals(" "))

                                        {
                                            if(tmpPrefUnitStringArray.equals(""))
                                            {
                                                tmpPrefUnitStringArray = tmpStr;
                                            }
                                            else
                                            {
                                                tmpPrefUnitStringArray = tmpPrefUnitStringArray + "," + tmpStr;
                                            }
                                            Log.e("ERRO", rs.getMetaData().getColumnName(i).toString() + " = " + tmpStr);
                                        }
                                    }
                                }
                            }
                            if(spnrName.equals("departement"))
                            {
                                edit.putString("subDepStringSet"+String.valueOf(j), tmpPrefSubdepartementStringArray);
                                j++;
                            }

                            if(spnrName.equals("type"))
                            {
                                edit.putString("unitsStringSet"+String.valueOf(k), tmpPrefUnitStringArray);
                                k++;
                            }
                            edit.commit();

                            tmpPrefSubdepartementStringArray = "";
                            tmpPrefUnitStringArray = "";


                        }

                            edit.putString("DepStringSet", tmpPrefDepartementStringArray);
                            edit.putString("SensorTypeStringSet", tmpPrefTypeStringArray);
                            edit.commit();

                            tmpPrefDepartementStringArray = "";
                            tmpPrefTypeStringArray = "";
                            Log.e("ERRO", prefs.getAll().toString());
                            z = "Программа обновлена";
                        }

                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
                String tStr = connectionClass.actSettings;
                z = tStr + "\nExceptions: " + ex.getMessage();
                Log.e("ERRO", ex.getMessage());

            }
            return z;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, Settings.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_check_connection) {
            InsertSensor CheckConnection = new InsertSensor();

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
            connectionClass.setConnectionSettings(temp_IP,temp_DB,temp_Login,temp_Pass);
            CheckConnection.execute("select * from test where test=147", "halt!!!");

            return true;
        }

        if (id == R.id.action_update) {
            boolean tmpBoolValue;
            tmpBoolValue = UpdateAllSpinners();
            return  tmpBoolValue;
        }

        return super.onOptionsItemSelected(item);
    }


}
