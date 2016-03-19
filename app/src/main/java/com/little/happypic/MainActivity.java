package com.little.happypic;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.little.happypic.business.BusinessId;
import com.little.happypic.business.pojo.PicClassTags;
import com.little.happypic.business.businesslayer.PicListBusinessLayer;
import com.little.framework.app.BaseBusinessActivity;
import com.little.framework.businessframework.business.BusinessResult;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class MainActivity extends BaseBusinessActivity {

    private ViewPager mViewPager;

    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }



    private void initView(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
    }


    /**
     *
     */
    private void initData(){
        PicListBusinessLayer.getInstance().getPicClass(this);
    }


    @Override
    protected void onBusinessResultImpl(BusinessResult result) {
        super.onBusinessResultImpl(result);
        switch (result.id){
            case BusinessId.Pic.ID_PIC_CLASSES:
                handleGetPicClass(result);
                break;
        }
    }

    private void handleGetPicClass(BusinessResult result){

        if (result.isSuccessful()){
            PicClassTags picClassTags = (PicClassTags)result.getData();
            if (picClassTags != null){
                setupViewPager(picClassTags);
            }
        }else{
            Exception exception = result.getException();
            handleException(exception);
        }
    }


    private void setupViewPager(PicClassTags picClassTags){

        if (picClassTags.tngou != null){
            Adapter adapter = new Adapter(getSupportFragmentManager());
            for (PicClassTags.PicClass  galleryclass : picClassTags.tngou){
                PicClassFragment fragment = new PicClassFragment();
                Bundle bundle  = new Bundle();
                bundle.putInt("id", galleryclass.id);
                fragment.setArguments(bundle);
                adapter.addFragment(fragment, galleryclass.title);

            }
            mViewPager.setAdapter(adapter);
            mTabLayout.setupWithViewPager(mViewPager);
        }

    }



    /**
     *
     */
    private void handleException(Exception exception){
        if (exception != null){
            Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     *
     */
    static class Adapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
