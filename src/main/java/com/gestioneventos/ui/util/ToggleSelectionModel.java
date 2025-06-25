package com.gestioneventos.ui.util;

import javax.swing.DefaultListSelectionModel;

/**
 * Modelo de selección que implementa el comportamiento de "toggle" en clic
 * único,
 * sin necesidad de Ctrl, y soporta selección múltiple en rangos.
 */
public class ToggleSelectionModel extends DefaultListSelectionModel {
    public ToggleSelectionModel() {
        super();
        // Permitir selección múltiple
        setSelectionMode(MULTIPLE_INTERVAL_SELECTION);
    }

    @Override
    public void addSelectionInterval(int index0, int index1) {
        // Toggle para clic simple
        if (index0 == index1) {
            if (isSelectedIndex(index0)) {
                super.removeSelectionInterval(index0, index1);
            } else {
                super.addSelectionInterval(index0, index1);
            }
        } else {
            // Rangos (shift-click) mantienen comportamiento
            super.addSelectionInterval(index0, index1);
        }
    }

    @Override
    public void setSelectionInterval(int index0, int index1) {
        // Redirigir todo a addSelectionInterval para evitar clear
        addSelectionInterval(index0, index1);
    }
}
