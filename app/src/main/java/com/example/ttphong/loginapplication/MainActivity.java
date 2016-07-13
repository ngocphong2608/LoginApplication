package com.example.ttphong.loginapplication;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class MainActivity extends ActionBarActivity {

    String Username = "thanhphong";
    String Password = "123456";
    boolean Status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadResource();

        if (isUserLogin()) {
            setContentView(R.layout.activity_profile);

            // display username
            TextView tv = (TextView)findViewById(R.id.profile_tv);
            tv.setText("Wellcome, " + Username);
        } else {
            setContentView(R.layout.activity_login);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        saveResource();
    }

    private void saveResource() {
        File file = getFilesDir();

        String path = file.getAbsolutePath();

        File resourceFile = new File(path + "/my_resource.dat");

        try {
            ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(resourceFile));
            MyResource res = new MyResource();

            res.username = Username;
            res.password = Password;
            res.status = Status;

            obj.writeObject(res);

            obj.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        saveResource();
    }

    private void loadResource() {
        File file = getFilesDir();

        String path = file.getAbsolutePath();

        File resourceFile = new File(path + "/my_resource.dat");

        if (resourceFile.exists()) {

            try {
                ObjectInputStream obj = new ObjectInputStream(new FileInputStream(resourceFile));
                MyResource res = (MyResource)obj.readObject();

                Username = res.username;
                Password = res.password;
                Status = res.status;

                obj.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUserLogin() {
        return Status;
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onLogin(View view) {
        String username = ((EditText)findViewById(R.id.username_et)).getText().toString().toLowerCase();
        String password = ((EditText)findViewById(R.id.password_et)).getText().toString();

        if (username.contentEquals("") || password.contentEquals("")) {
            Toast.makeText(this, "Please fill in username and password", Toast.LENGTH_SHORT).show();
        } else if (matchPassword(username, password)) {
            // update user status
            updateUserStatus(true);

            setContentView(R.layout.activity_profile);

            // display username
            TextView tv = (TextView)findViewById(R.id.profile_tv);
            tv.setText("Wellcome, " + Username);
        } else {
            Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserStatus(boolean status) {
        Status = status;
    }

    private boolean matchPassword(String username, String password) {
        return Username.contentEquals(username) && Password.contentEquals(password);
    }

    public void onLogout(View view) {
        // update user status
        updateUserStatus(false);

        setContentView(R.layout.activity_login);
    }
}
