package com.osam.bodyprotector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.ArrayList;

import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

class ViewAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();

    public ViewAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    void addItem(Fragment fragment){
        fragments.add(fragment);
    }
}

public class ExerciseActivity extends AppCompatActivity {

    String[] partname = {"어깨","등","팔","가슴","복근","하체"};

    private ScrollerViewPager viewpager;
    private SpringIndicator springIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        viewpager = (ScrollerViewPager)findViewById(R.id.viewpager);
        springIndicator = (SpringIndicator)findViewById(R.id.indicator);
        ViewAdapter adapter = new ViewAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.fixScrollSpeed();
        for(String s : partname){
            ChestFragment cf = new ChestFragment();
            Bundle args = new Bundle(1);
            args.putString("partname",s);
            cf.setArguments(args);
            adapter.addItem(cf);
        }
        adapter.notifyDataSetChanged();


        springIndicator.setViewPager(viewpager);
    }
}