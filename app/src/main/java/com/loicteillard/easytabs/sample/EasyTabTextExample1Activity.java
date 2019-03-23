package com.loicteillard.easytabs.sample;

import android.os.Bundle;

import com.loicteillard.easytabs.EasyTabs;

import androidx.viewpager.widget.ViewPager;

public class EasyTabTextExample1Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_tab_text_ex1);
    
        EasyTabs easyTabs = findViewById(R.id.easytabs);
        ViewPager viewpager = findViewById(R.id.viewpager);

        MyFragmentAdapter pagerAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);
        easyTabs.setViewPager(viewpager);
    }


}
