package com.gestioneventos.ui.view;

import com.gestioneventos.domain.Asistente;
import com.gestioneventos.domain.Evento;
import com.gestioneventos.domain.Organizador;
import com.gestioneventos.ui.component.RoundedButton;
import com.gestioneventos.ui.component.RoundedList;
import com.gestioneventos.ui.component.RoundedSpinner;
import com.gestioneventos.ui.component.RoundedTextArea;
import com.gestioneventos.ui.component.RoundedTextField;
import com.gestioneventos.ui.util.ToggleSelectionModel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Vista para creación de un nuevo evento.
 */
public class CrearEventoView extends JPanel {
    private final RoundedTextField tituloField = new RoundedTextField(20);
    private final RoundedTextField ubicacionField = new RoundedTextField(20);
    private final JSpinner fechaSpinner;
    private final RoundedTextArea descripcionArea = new RoundedTextArea(5, 20);
    private final RoundedList<Organizador> organizadoresList = new RoundedList<>(new DefaultListModel<>());
    private final RoundedList<Asistente> asistentesList = new RoundedList<>(new DefaultListModel<>());
    private final RoundedButton guardarButton = new RoundedButton("Guardar Evento");
    private final RoundedButton cancelarButton = new RoundedButton("Cancelar");

    public CrearEventoView() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        fechaSpinner = new RoundedSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(fechaSpinner, "yyyy-MM-dd");
        fechaSpinner.setEditor(editor);

        JScrollPane descScroll = new JScrollPane(descripcionArea);
        descScroll.setBorder(BorderFactory.createEmptyBorder());

        // Configurar selección múltiple
        organizadoresList.setSelectionModel(new ToggleSelectionModel());
        JScrollPane orgScroll = new JScrollPane(organizadoresList);
        orgScroll.setPreferredSize(new Dimension(200, 100));

        asistentesList.setSelectionModel(new ToggleSelectionModel());
        JScrollPane asisScroll = new JScrollPane(asistentesList);
        asisScroll.setPreferredSize(new Dimension(200, 100));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        form.add(tituloField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        form.add(new JLabel("Ubicación:"), gbc);
        gbc.gridx = 1;
        form.add(ubicacionField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        form.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        form.add(fechaSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        form.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        form.add(descScroll, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        form.add(new JLabel("Organizadores:"), gbc);
        gbc.gridx = 1;
        form.add(orgScroll, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        form.add(new JLabel("Asistentes:"), gbc);
        gbc.gridx = 1;
        form.add(asisScroll, gbc);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botones.add(cancelarButton);
        botones.add(guardarButton);

        add(form, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    public void setOrganizadores(List<Organizador> organizadores) {
        DefaultListModel<Organizador> model = new DefaultListModel<>();
        organizadores.forEach(model::addElement);
        organizadoresList.setModel(model);
    }

    public void setAsistentes(List<Asistente> asistentes) {
        DefaultListModel<Asistente> model = new DefaultListModel<>();
        asistentes.forEach(model::addElement);
        asistentesList.setModel(model);
    }

    public void loadEvento(Evento e) {
        tituloField.setText(e.getTitulo());
        ubicacionField.setText(e.getUbicacion());
        fechaSpinner.setValue(Date.from(e.getFecha()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()));
        descripcionArea.setText(e.getDescripcion());
        seleccionarItemsEnLista(organizadoresList, e.getOrganizadores());
        seleccionarItemsEnLista(asistentesList, e.getAsistentes());
    }

    // Selecciona en la lista aquellos elementos que aparezcan en la colección proporcionada
    private <T> void seleccionarItemsEnLista(JList<T> lista, List<T> items) {
        ListModel<T> model = lista.getModel();
        ListSelectionModel selModel = lista.getSelectionModel();
        for (int i = 0; i < model.getSize(); i++) {
            T element = model.getElementAt(i);
            if (items.contains(element)) {
                selModel.addSelectionInterval(i, i);
            }
        }
    }

    public String getTitulo() {
        return tituloField.getText().trim();
    }

    public String getUbicacion() {
        return ubicacionField.getText().trim();
    }

    public LocalDate getFecha() {
        Date d = (Date) fechaSpinner.getValue();
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public String getDescripcion() {
        return descripcionArea.getText().trim();
    }

    public List<Organizador> getSelectedOrganizadores() {
        return organizadoresList.getSelectedValuesList();
    }

    public List<Asistente> getSelectedAsistentes() {
        return asistentesList.getSelectedValuesList();
    }

    public void onSave(ActionListener l) {
        guardarButton.addActionListener(l);
    }

    public void onCancel(ActionListener l) {
        cancelarButton.addActionListener(l);
    }
}
