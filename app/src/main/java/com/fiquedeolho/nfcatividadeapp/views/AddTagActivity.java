package com.fiquedeolho.nfcatividadeapp.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.fragments.addTag.FragmentAddTagCheck;
import com.fiquedeolho.nfcatividadeapp.fragments.addTag.FragmentAddTagInf;
import com.fiquedeolho.nfcatividadeapp.pager.addTag.PagerAddTagAdapter;

public class AddTagActivity extends AppCompatActivity {

    private ViewHolderAddTag mViewHolderAddTag = new ViewHolderAddTag();
    private PagerAddTagAdapter pagerAdapter;
    private Context context;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = this;
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        this.mViewHolderAddTag.mViewPagerAddTag = (ViewPager) findViewById(R.id.view_pager_add_tag);
        this.mViewHolderAddTag.mViewDotsLayout = (LinearLayout) findViewById(R.id.layoutDotsAddTag);
        this.mViewHolderAddTag.mViewBtnSkip = (Button) findViewById(R.id.btn_skip_add_tag);
        this.mViewHolderAddTag.mViewBtnNext = (Button) findViewById(R.id.btn_next_add_tag);


        this.mViewHolderAddTag.mViewBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addAtividadeDB = false;
                //launchHomeScreen();
            }
        });

        this.mViewHolderAddTag.mViewBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < 2) {
                    // move to next screen
                    mViewHolderAddTag.mViewPagerAddTag.setCurrentItem(current);
                } else {
                    //Tarefa tarefa = new Tarefa();
                    //addTarefa(tarefa);
                }
            }
        });
        FragmentAddTagInf fragInf = new FragmentAddTagInf();
        FragmentAddTagCheck fragCheck = new FragmentAddTagCheck();
        pagerAdapter = new PagerAddTagAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(fragInf);
        pagerAdapter.addFragment(fragCheck);
        this.mViewHolderAddTag.mViewPagerAddTag.setAdapter(pagerAdapter);
        this.mViewHolderAddTag.mViewPagerAddTag.addOnPageChangeListener(viewPagerPageChangeListener);

    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == 1) {
                // last page. make button text to GOT IT
                mViewHolderAddTag.mViewBtnNext.setText(getString(R.string.concluido));
                mViewHolderAddTag.mViewBtnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                mViewHolderAddTag.mViewBtnNext.setText(getString(R.string.proximo));
                mViewHolderAddTag.mViewBtnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void addBottomDots(int currentPage) {
        dots = new TextView[2];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        this.mViewHolderAddTag.mViewDotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[0]);
            this.mViewHolderAddTag.mViewDotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[0]);
    }

    private int getItem(int i) {
        return this.mViewHolderAddTag.mViewPagerAddTag.getCurrentItem() + i;
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderAddTag {

        private LinearLayout mViewDotsLayout;
        private ViewPager mViewPagerAddTag;
        private Button mViewBtnNext;
        private Button mViewBtnSkip;
    }
}
