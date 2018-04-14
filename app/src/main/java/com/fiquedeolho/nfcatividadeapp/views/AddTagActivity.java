package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.fragments.addTag.FragmentAddTagCheck;
import com.fiquedeolho.nfcatividadeapp.fragments.addTag.FragmentAddTagInf;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.TagRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.pager.addTag.PagerAddTagAdapter;

import retrofit2.Call;
import retrofit2.Callback;

public class AddTagActivity extends AppCompatActivity {

    private ViewHolderAddTag mViewHolderAddTag = new ViewHolderAddTag();
    private PagerAddTagAdapter pagerAdapter;
    private TextView[] dots;
    private FragmentAddTagCheck fragCheckNFC;
    private FragmentAddTagInf fragInf;
    private String calledActivity;
    private ProgressDialog pDialog;
    private int idTagRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            calledActivity = extras.getString("calledActivity");
        }

        Long tsLong = System.currentTimeMillis()/1000;
        idTagRandom = tsLong.intValue();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
                    LinearLayout linear = mViewHolderAddTag.mViewPagerAddTag.findViewById(R.id.first_content_linear_layout_add_tag);
                    EditText nomeTagEle = linear.findViewById(R.id.input_nomeTag);
                    String nomeTagInput = nomeTagEle.getText().toString();
                    TAG tag = new TAG();
                    tag.setNome(nomeTagInput);
                    tag.setId(idTagRandom);
                    addTAG(tag);
                }
            }
        });
        // adding bottom dots
        addBottomDots(0);

        fragInf = new FragmentAddTagInf();
        fragCheckNFC = new FragmentAddTagCheck();
        pagerAdapter = new PagerAddTagAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(fragInf);
        pagerAdapter.addFragment(fragCheckNFC);
        this.mViewHolderAddTag.mViewPagerAddTag.setAdapter(pagerAdapter);
        this.mViewHolderAddTag.mViewPagerAddTag.addOnPageChangeListener(viewPagerPageChangeListener);

    }


    private void addTAG(final TAG tag) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setTitle(getString(R.string.title_progress_tag_add));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.show();
        TagRetrofit tagInterface = BaseUrlRetrofit.retrofit.create(TagRetrofit.class);
        final Call<Boolean> call = tagInterface.addTag(tag);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                Boolean result = response.body();
                backToActivityCall();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }

    /**
     Click no botao voltar da activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToActivityCall();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void backToActivityCall() {
        Intent resultIntent = null;
        switch(calledActivity) {
            case "infTags":
                 resultIntent = new Intent(this, InfTagsActivity.class);
                break;

            case "addTarefa":
                resultIntent = new Intent(this, AddTarefaActivity.class);
                break;
        }
        startActivity(resultIntent);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        fragCheckNFC.intentNFCTag(intent, idTagRandom);

        /**
         * TODO : CRIAR UMA MANEIRA DE VALIDAR QUE O USUARIO APROXIMOU A TAG AO CELULAR.
         * Por que se nao, o usuario pode finalizar a criacao da TAG e nao vinculou a TAG em si
         */
    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == 1) {
                // last page. make button text to GOT IT
                fragCheckNFC.checkNFCAtivo();
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
            dots[i].setTextSize(45);
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
