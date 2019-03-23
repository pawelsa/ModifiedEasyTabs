package com.loicteillard.easytabs.sample;

import android.os.Bundle;

import com.loicteillard.easytabs.EasyTabs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class EasyTabIconActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_tab_icon);
    
        EasyTabs easyTabs = findViewById(R.id.easytabs);
        ViewPager viewpager = findViewById(R.id.viewpager);

        MyFragmentAdapter pagerAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);
        easyTabs.setViewPager(viewpager);
    }
}
