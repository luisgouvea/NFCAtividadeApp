package com.fiquedeolho.nfcatividadeapp.pager.addTarefa;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.util.Mask;

/**
 * View pager adapter
 */
public class PagerAddTarefaAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private int[] layouts;
    private Context mViewContext;

    public PagerAddTarefaAdapter(int [] layouts, Context context) {
        this.layouts = layouts;
        this.mViewContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) mViewContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(layouts[position], container, false);

        switch (position) {
            case 0:
                EditText dataFinalizaElement = view.findViewById(R.id.input_data_finalizacao_tarefa);
                dataFinalizaElement.addTextChangedListener(Mask.insert("##/##/####", dataFinalizaElement));
                break;
            case 1:
                //Algo dos layouts
                break;
        }
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
