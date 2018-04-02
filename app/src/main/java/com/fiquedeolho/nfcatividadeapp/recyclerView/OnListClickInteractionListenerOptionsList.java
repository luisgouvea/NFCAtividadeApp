package com.fiquedeolho.nfcatividadeapp.recyclerView;

import android.view.View;

/**
 * Interface utilizada para lidar com eventos na listagem de algum elemento
 * No caso do recyclerview das listagens, um TextView
 * Em outros casos, verificar como os elementos serao criados
 */
public interface OnListClickInteractionListenerOptionsList {

    /**
     * Trata evento de click na lista para as opcoes
     */
    void onClick(View v);

}
