package com.example.stockinginventory;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new BorrowItem());

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_draw_open,R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        if(savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new BorrowItem()).commit();
            navigationView.setCheckedItem(R.id.BorrowItems);
        }

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            String destination = extras.getString("destination");
            if(destination!=null) {
                if (destination.equals("DepartmentList"))
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container, new DepartmentList()).commit();
            }
            String deptIdFromDepartmentInfo = extras.getString("deptIdItemList");
            if (deptIdFromDepartmentInfo!=null){
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container, new ItemList(deptIdFromDepartmentInfo)).commit();
            }
            String deptIdFromDepartmentList = extras.getString("deptIdDepartmentInfo");
            if (deptIdFromDepartmentList!=null)
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container, new Department_Info(deptIdFromDepartmentList)).commit();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.BorrowItems:
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new BorrowItem()).commit();
                break;
            case R.id.InsertDelete:
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new InsertDeleteActivity()).commit();
                break;
            case R.id.AddDepartment:
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new DepartmentActivity()).commit();
                break;
            case R.id.AddDepartmentList:
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new DepartmentList()).commit();
                break;
            case R.id.UpdateItems:
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new UpdateItems()).commit();
                break;
            case R.id.item_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new ItemList("")).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

}