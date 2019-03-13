package com.example.BillReceipt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import static android.widget.Toast.LENGTH_LONG;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final int PERMISSION_REQUEST_KEY_EXTERNAL_STORAGE = 192;
    String name;
    String money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkFirstRun();
        name = setName();
        money = setMoney();

        if (name == null){
            Intent intent = new Intent(this, IntroductionActivity.class);
            startActivity(intent);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this,R.string.accessExternal,LENGTH_LONG).show();
            }else{
                ActivityCompat.requestPermissions(this,new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_KEY_EXTERNAL_STORAGE);
            }
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCam();
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {

            @Override
            public void onDrawerStateChanged(int newState) {
                if( newState == DrawerLayout.STATE_DRAGGING && !drawer.isDrawerOpen(GravityCompat.START)) {
                    TextView name_text = (TextView) findViewById(R.id.name_text);
                    TextView money_text = (TextView) findViewById(R.id.money_text);

                    name = setName();
                    if(name != null) {
                        name_text.setText("Hi " + name + "!");
                    }
                    money = setMoney();
                    if(money != null) {
                        money_text.setText(money);
                    }
                }
            }};
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SpannableString s = new SpannableString("$napney");
        s.setSpan(new TypefaceSpan(this, "Playball.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Update the action bar title with the TypefaceSpan instance
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(s);

        Fragment frag = new MainFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, frag).commit();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case PERMISSION_REQUEST_KEY_EXTERNAL_STORAGE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else {
                    Toast.makeText(this,R.string.accessExternal,LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Toast.makeText(this,R.string.use_menu,Toast.LENGTH_LONG).show();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Fragment fragment = null;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fragment = new SettingsFragment();

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, fragment).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_camera) {
            openCam();

        } else if (id == R.id.nav_gallery) {
            openGallery();


        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();

        } else if (id == R.id.nav_main) {
            fragment = new MainFragment();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void checkFirstRun() {

        final String PREFS_NAME = "GET_VERSION_CODE";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;


        // Gets current version code
        int currentVersionCode = 0;
        try {
            currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // handles exception
            e.printStackTrace();
            return;
        }

        // Gets saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Checks for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {

            Intent intent = new Intent(this, IntroductionActivity.class);
            startActivity(intent);

        } else if (currentVersionCode > savedVersionCode) {

            // TODO Message showing new features

        }

        // Updates the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();

    }

    public String setName(){
        final String PREFS_NAME = "SN_PREFS";
        final String PREFS_KEY = "SN_PREFS_NAME";

        SharedPreferences settings;
        String text;
        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(PREFS_KEY,null);

        return text;


    }
    public String setMoney(){
        final String PREFS_NAME = "SN_PREFS";
        final String PREFS_KEY = "SN_PREFS_MONEY";
        CharSequence d = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a", new java.util.Date());
        String date = d.toString();

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String month  = prefs.getString("SN_PREFS_DATE", "13");

        String[] parts = date.split("\\-");

        if (parts[1].contentEquals(month)){
            String text;
            text = String.format("%.2f",prefs.getFloat(PREFS_KEY, 0));
            return text;

        }else {
            month = parts[1];

            prefs.edit().putString("SN_PREFS_DATE", month).commit();
            prefs.edit().putFloat("SN_PREFS_MONEY_LM",prefs.getFloat(PREFS_KEY,0));
            prefs.edit().putFloat(PREFS_KEY, 0).commit();
            return "0.0";

        }

    }

    public void openCam(){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    public void openGallery(){
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }
}
