package ru.swimmasters;

import ru.swimmasters.domain.*;
import ru.swimmasters.jaxb.ContextHolder;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * User: dedmajor
 * Date: Jul 25, 2010
 */
public class HeatBuilderGui extends JFrame {
    private final JButton buildButton;
    private final JFileChooser loadFileChooser;
    private final JFileChooser safeFileChooser;

    private Meet meet;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HeatBuilderGui("Heat Builder - Swimmasters").setVisible(true);
            }
        });
    }

    public HeatBuilderGui(String title) throws HeadlessException {
        super(title);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        loadFileChooser = new JFileChooser();
        loadFileChooser.setFileFilter(new FileNameExtensionFilter("XML Documents", "xml"));

        JButton loadButton = new JButton("Load meet register...");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (loadFileChooser.showOpenDialog(HeatBuilderGui.this) == JFileChooser.APPROVE_OPTION) {
                    loadMeet(loadFileChooser.getSelectedFile());
                    buildButton.setEnabled(true);
                }
            }
        });

        safeFileChooser = new JFileChooser();
        safeFileChooser.setFileFilter(new FileNameExtensionFilter("Text Document", "txt"));
        
        buildButton = new JButton("Build heats...");
        buildButton.setEnabled(false);
        buildButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (safeFileChooser.showSaveDialog(HeatBuilderGui.this) == JFileChooser.APPROVE_OPTION) {
                    buildHeats(safeFileChooser.getSelectedFile());
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(buildButton);

        getContentPane().add(buttonPanel);

        pack();
    }

    private void loadMeet(File selectedFile) {
        Unmarshaller um = new ContextHolder().createUnmarshaller();
        try {
            MeetRegister meetRegister = (MeetRegister) um.unmarshal(selectedFile);
            meet = meetRegister.getMeet();
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void buildHeats(File selectedFile) {
        assert meet != null;

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(selectedFile);
            formatMeet(pw);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    private void formatMeet(PrintWriter pw) {
        pw.println("MEET: " + meet);
        for (Event event : meet.getEvents()) {
            formatEvent(event, pw);
        }
    }

    private void formatEvent(Event event, PrintWriter pw) {
        // TODO: FIXME: extract formatter
        pw.println("EVENT: " + event);

        int heatNumber = 0;
        for (Heat heat : event.buildHeats(3)) {
            heatNumber++;
            pw.println(String.format("HEAT #%d (size=%d):", heatNumber, heat.getApplications().size()));
            for (Application application : heat.getApplications()) {
                pw.println(application);
            }
        }
    }
}
