package splatoon2RichPresence;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;

import splat2ink.schedules.*;

public class MainForm extends JFrame {
    private JComboBox modeBox;
    private JRadioButton stageARadioButton;
    private JRadioButton stageBRadioButton;
    private JPanel panel;
    private JButton startbtn;
    private JButton results;
    private JButton searchingButton;
    private JButton idleInLobbyButton;
    private JButton refreshStagesAndModesButton;

    Main main = new Main();
    rootObject root;
    private void setStages(String stagea, String stageb) {

        this.stageARadioButton.setText(stagea);
        this.stageBRadioButton.setText(stageb);
    }

    public MainForm(){
        add(panel);
        setTitle("Splatoon 2 Rich Presence");
        setSize(400,400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initialize();
        refreshStagesAndModesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm_refresh = JOptionPane.showConfirmDialog(rootPane, "Stages and modes only need to be refreshed\nonce every 2 hours.\n\nAre you sure you want to refresh?", "Confirm refresh", JOptionPane.YES_NO_OPTION);

                if (confirm_refresh == 0)
                {
                    try
                    {
                        root = main.getData(true);
                    }
                    catch (Exception ex){}

                    JOptionPane.showMessageDialog(panel,"Successfully updated database.");
                }
            }
        });
        startbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Declare mode string
                String mode = "";
                //Declare Rule string
                String rule = "";
                //Declare stage string
                String stage = "";
                //Store current system time in a variable
                long startTime = System.currentTimeMillis() / 1000L;
                //Declare endTime variable
                long endTime = 0;
                //Get current mode selected in combobox
                String currentMode = modeBox.getSelectedItem().toString();
                if (currentMode == "Regular Battle")
                {
                    //Add 3 minutes in epoch seconds to start time to get endTime
                    endTime = startTime + TimeUnit.MINUTES.toSeconds(3);
                    rule = "Turf War";
                }
                else if (currentMode == "Ranked Battle" || currentMode == "League Battle")
                {
                    //Add 5 minutes in epoch seconds to start time to get endTime
                    endTime = startTime + TimeUnit.MINUTES.toSeconds(5);
                    if (currentMode == "Ranked Battle")
                    {
                        rule = root.gachi.get(0).rule.name;
                    }
                    else if (currentMode == "League Battle")
                    {
                        rule = root.league.get(0).rule.name;
                    }
                }
                mode = currentMode;

                if (stageARadioButton.isSelected()) {
                    stage = stageARadioButton.getText();
                }
                else if (stageBRadioButton.isSelected()) {
                    stage = stageBRadioButton.getText();
                }

                main.updatePresence("In Game",rule,startTime,endTime,stage,mode);
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                main.discordClose();
//                super.windowClosing(windowEvent);
            }
        });
        searchingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                main.updatePresence("Searching...");
            }
        });
        results.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                main.updatePresence("Viewing Results");
            }
        });
        idleInLobbyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                main.updatePresence("In Lobby");
            }
        });
    }

    public void initialize()
    {
        modeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                try
                {
                    root = main.getData(false);

                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(rootPane,"The database may not have been (re)loaded.","Could not get database",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String currentMode = modeBox.getSelectedItem().toString();

                String stagea = "Stage A";
                String stageb = "Stage B";
                if (currentMode == "Regular Battle")
                {
                    stagea = root.regular.get(0).stage_a.name;
                    stageb = root.regular.get(0).stage_b.name;
                }
                else if (currentMode == "Ranked Battle")
                {
                    stagea = root.gachi.get(0).stage_a.name;
                    stageb = root.regular.get(0).stage_b.name;
                }
                else if (currentMode == "League Battle")
                {
                    stagea = root.league.get(0).stage_a.name;
                    stageb = root.league.get(0).stage_b.name;
                }
                setStages(stagea,stageb);
            }
        });
    }
}
