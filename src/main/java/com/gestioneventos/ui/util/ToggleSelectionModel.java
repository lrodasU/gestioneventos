package com.gestioneventos.ui.util;

import javax.swing.DefaultListSelectionModel;

// Modelo de seleccion que permite seleccionar varios elementos
// en una lista al mismo tiempo, sin la necesidad de usar ctrl + click.
// Permite usar shift + click para seleccionar un rango de elementos
public class ToggleSelectionModel extends DefaultListSelectionModel {
    public ToggleSelectionModel() {
        super();
        setSelectionMode(MULTIPLE_INTERVAL_SELECTION);
    }

    @Override
    public void addSelectionInterval(int index0, int index1) {
        if (index0 == index1) {
            if (isSelectedIndex(index0)) {
                super.removeSelectionInterval(index0, index1);
            } else {
                super.addSelectionInterval(index0, index1);
            }
        } else {
            super.addSelectionInterval(index0, index1);
        }
    }

    @Override
    public void setSelectionInterval(int index0, int index1) {
        // Redirigir todo a addSelectionInterval para evitar clear
        addSelectionInterval(index0, index1);
    }
}
