package ru.ostgard.sensordb;

        import android.annotation.SuppressLint;
        import android.content.SharedPreferences;
        import android.os.StrictMode;
        import android.util.Log;
        import android.widget.Toast;

        import java.sql.SQLException;
        import java.sql.Connection;
        import java.sql.DriverManager;


/**
 * Created by h-pc on 16-Oct-15.
 */
public class ConnectionClass {
    public ConnectionClass connectionClass;

    private String ip = "192.168.1.1";
    private String classs = "net.sourceforge.jtds.jdbc.Driver";
    private String db = "db";
    private String un = "un";
    private String password = "pass";

    public  String actExceptionSQL = "";
    public  String actExceptionCLASS = "";
    public  String actExceptionE = "";
    public  String actSettings = "";



    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            actExceptionSQL = "";
            actExceptionCLASS = "";
            actExceptionCLASS = "";

            Log.e("ERRO", ip+" "+db+" "+un+" "+password);
            actSettings = ip+" "+db+" "+un+" "+password;
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
            actExceptionSQL = se.getMessage();
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
            actExceptionCLASS = e.getMessage();
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
            actExceptionE = e.getMessage();
        }
        return conn;
    }

    public void setConnectionSettings(String strIP, String strDB,String strLogin,String strPass)
    {
        if(strIP != null || strIP != "")
        {
            ip = strIP;
        }else
        {
            ip = "192.168.1.1";
        }
        if(strDB != null || strDB != "")
        {
            db = strDB;
        }else
        {
            db = "db";
        }
        if(strLogin != null || strLogin != "")
        {
            un = strLogin;
        }else
        {
            un = "un";
        }
        if(strPass != null || strPass != "")
        {
            password = strPass;
        }else
        {
            password = "pass";
        }
    }
}

