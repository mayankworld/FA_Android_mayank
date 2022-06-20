package com.firstapp.fa_mayanksanjeevnibhoria_c0854281_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    TabLayout tl;

    Fragment fragment;
    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tl=findViewById(R.id.tl);

        tl.addOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        if(tab.getPosition()==0)
        {
            fragment=new AddFragment();
        }
        else
        {
            fragment=new ListFragment();
        }

        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        ft.replace(R.id.frcont, fragment);
        ft.commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}