package com.a14roxgmail.prasanna.mobileapp.UI;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.DAO.CourseDAO;
import com.a14roxgmail.prasanna.mobileapp.Fragment.CourseFragment;
import com.a14roxgmail.prasanna.mobileapp.Fragment.GpaFragment;
import com.a14roxgmail.prasanna.mobileapp.Fragment.GpaSemFragment;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    TextView tvIndexNo;
    TextView tvFullName;
    CourseDAO course_dao;
    ArrayList<Course> course_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
/*
        GpaFragment gpaFragment = new GpaFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frmMain,gpaFragment);
        toolbar.setTitle("GPA SEM");
        transaction.commit();
*/
        course_dao = new CourseDAO(this);
        Bundle b = getIntent().getExtras();
        HashMap<String,String> map = (HashMap<String,String>) b.getSerializable("Values");
        course_list = course_dao.getAllCoursesByUserId(map.get("username").toUpperCase());
        int i = course_list.size();

        View header = navigationView.getHeaderView(0);
        tvIndexNo = (TextView) header.findViewById(R.id.tvIndexNo);
        tvFullName = (TextView) header.findViewById(R.id.tvFullName);
        tvIndexNo.setText(map.get("username").toUpperCase().toString());
        tvFullName.setText(map.get("fullname").toString());

        CourseFragment courseFragment = new CourseFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frmMain,courseFragment);
        courseFragment.setServerCourseList(course_list);
        toolbar.setTitle("My Courses");
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_MyCourses) {
            CourseFragment courseFragment = new CourseFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frmMain,courseFragment);
            courseFragment.setServerCourseList(course_list);
            toolbar.setTitle("My Courses");
            transaction.commit();

        } else if (id == R.id.nav_feedback) {
            toolbar.setTitle("Feedback");
        } else if (id == R.id.nav_GPA) {
            toolbar.setTitle("GPA");
        } else if (id == R.id.nav_signout) {
            //Signout Process
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
