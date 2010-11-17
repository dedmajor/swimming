package ru.swimmasters;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import ru.swimmasters.domain.*;
import ru.swimmasters.format.MeetFormatter;
import ru.swimmasters.format.VelocityMeetFormatter;
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

    private MeetRegister meetRegister;

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
                    loadMeetRegister(loadFileChooser.getSelectedFile());
                    buildButton.setEnabled(true);
                }
            }
        });

        safeFileChooser = new JFileChooser();
        safeFileChooser.setFileFilter(new FileNameExtensionFilter("HTML Document", "html"));
        
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

    private void loadMeetRegister(File selectedFile) {
        Unmarshaller um = new ContextHolder().createUnmarshaller();
        try {
            meetRegister = (MeetRegister) um.unmarshal(selectedFile);
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void buildHeats(File selectedFile) {
        assert meetRegister != null;

        // TODO: FIXME: extract builder
        for (Event event : meetRegister.getMeet().getEvents()) {
            StartList startList = new StartList(event);
            startList.addHeats(event.buildHeats(3));
            meetRegister.addStartList(startList);
        }

        Velocity.setProperty("resource.loader", "class");
        Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try {
            Velocity.init();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        Template template;
        try {
            template = Velocity.getTemplate("ru/swimmasters/format/html.vm"); // TODO: guess formatter from file name
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(selectedFile);
            MeetFormatter formatter = new VelocityMeetFormatter(template);

            formatter.format(meetRegister, pw);

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    private void formatMeet(PrintWriter pw) {
    }
}
