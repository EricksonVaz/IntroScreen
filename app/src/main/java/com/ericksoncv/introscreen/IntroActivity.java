package com.ericksoncv.introscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0;
    Button btnGetStarted;
    Animation btnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //make the activity on full screen

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //when this activity is about to be launch we need to check if its openened before or not

        if(restorePrefData()){
            Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(mainActivity);
            finish();
        }

        setContentView(R.layout.activity_intro);

        //hide the action bar

        getSupportActionBar().hide();

        //ini views
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_getstarted);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);

        //fill List Screen

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Fresh Food","Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",R.drawable.img1));
        mList.add(new ScreenItem("Fast Delivery","Lorem ipsum dolor sit amet, " +
                "consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore " +
                "magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                "ullamco laboris nisi ut aliquip ex ea commodo consequat.",R.drawable.img2));
        mList.add(new ScreenItem("Easy payment","Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud " +
                "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",R.drawable.img3));

        //setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        //setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);

        //next button click List listener
        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                position = screenPager.getCurrentItem();
                if(position < mList.size()){

                    position++;
                    screenPager.setCurrentItem(position);
                }

                if(position == mList.size()-1){//when we rech to the last screen
                    //TODO : show the GETSTARTED Button and hide the indicator
                    
                    loadLastScreen();
                }
            }
        });

        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == mList.size()-1){
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Get Started button click listener
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Open main activity

                Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainActivity);

                //also ew nned to save a boolean value to storage so next time when teh user run the app
                // we could know that he is already checked the into screen activity
                //i'm going to use shared preferences that process

                savePrefsData();
                finish();

            }


        });
    }

    private boolean restorePrefData() {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = preferences.getBoolean("isIntroOpned",false);
        return isIntroActivityOpnendBefore;
    }

    private void savePrefsData() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isIntroOpned",true);
        editor.commit();

    }

    //show the GETSTARTED Button and hide the indicator
    private void loadLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        //TODO: ADD an animation the getstarted button
        //let create the button animation:
        btnGetStarted.setAnimation(btnAnim);
    }
}
