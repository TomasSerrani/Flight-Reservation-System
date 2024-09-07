package com.finalprogramacion.sistemaDeVuelos.gui;
import com.finalprogramacion.sistemaDeVuelos.models.dtos.FlightDTO;

import javax.swing.*;
import java.awt.*;

public class FlightListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof FlightDTO) {
            FlightDTO flight = (FlightDTO) value;
            String displayText = "<html>" +
                    "Flight Number: " + flight.getFlightNum() + "           " +
                    "Departure Date: " + flight.getDepartureDate() + " "+ flight.getDepartureTime() + "             "+
                    "Price: $" + flight.getPrice() +
                    "</html>";
            label.setText(displayText);
        }

        return label;
    }
}