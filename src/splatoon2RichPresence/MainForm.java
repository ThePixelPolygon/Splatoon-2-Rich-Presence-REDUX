package splatoon2RichPresence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;
import com.thizzer.jtouchbar.*;
import splat2ink.coop_schedules.coopRootObject;
import splat2ink.schedules.*;
import splat2ink.festivals.*;

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
    private JSpinner waveSpinner;
    private JButton changeRegionButton;
    private JRadioButton shiftyStationRadioButton;

    Preferences usrPref = Preferences.userRoot().node("/user/splat2rp/root");
    Main main = new Main();
    rootObject root;
    coopRootObject coopRoot;
    festivalsRootObject festivalsRoot;
    private void setStages(String stagea, String stageb) {
        //Sets stages in radio buttons
        this.stageARadioButton.setText(stagea);
        this.stageBRadioButton.setText(stageb);
    }



    public MainForm(){
        add(panel);
        setTitle("Splatoon 2 Rich Presence");
        setSize(450,400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        boolean salmonRunOpen = getSalmonRunOpen();

        //Checks if region has been set
        if ((usrPref.get("REGION","not_set")) == "not_set") {
            JOptionPane.showConfirmDialog(rootPane,"Welcome to the Splatoon 2 Rich Presence!\nBefore you begin, please set your region.","Set Region",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
            setRegion();
        }
        if (splatfestDetected(usrPref.get("REGION", "not_set")))
        {
            modeBox.removeAllItems();
            modeBox.addItem("Splatfest Battle (Regular)");
            modeBox.addItem("Splatfest Battle (Pro)");
            shiftyStationRadioButton.setVisible(true);
        }
        else if (!(splatfestDetected(usrPref.get("REGION","not_set"))) && modeBox.getItemAt(0) == "Splatfest Battle (Regular") {
            modeBox.removeAllItems();
            modeBox.addItem("Regular Battle");
            modeBox.addItem("Ranked Battle");
            modeBox.addItem("League Battle");
            shiftyStationRadioButton.setVisible(false);
        }
        if (salmonRunOpen) {
            modeBox.addItem("Salmon Run");

        }


        waveSpinner.setValue(1);
        modeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //Gets data from splat2ink package without updating it again.
                try
                {
                    root = main.getData(false);
                    coopRoot = main.getCoopData(false);
                    festivalsRoot = main.getFestivalsData(false);

                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(rootPane,"The database may not have been (re)loaded.","Could not get database",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //Gets current mode selected in combo box
                String currentMode = modeBox.getSelectedItem().toString();
                stageBRadioButton.setEnabled(true);
                String stagea = "Stage A";
                String stageb = "Stage B";

                //Changes stages according to combo box selection
                if (currentMode == "Regular Battle" || currentMode == "Splatfest Battle (Pro)" || currentMode == "Splatfest Battle (Regular)")
                {
                    stagea = root.regular.get(0).stage_a.name;
                    stageb = root.regular.get(0).stage_b.name;
                }
                else if (currentMode == "Ranked Battle")
                {
                    stagea = root.gachi.get(0).stage_a.name;
                    stageb = root.gachi.get(0).stage_b.name;
                }
                else if (currentMode == "League Battle")
                {
                    stagea = root.league.get(0).stage_a.name;
                    stageb = root.league.get(0).stage_b.name;
                }
                else if (currentMode == "Salmon Run")
                {
                    stagea = coopRoot.details.get(0).stage.name;
                    stageb = "";
                    stageBRadioButton.setEnabled(false);
                }
                //Actually does the changing of stage names in the radio buttons
                setStages(stagea,stageb);
            }
        });
        refreshStagesAndModesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm_refresh = JOptionPane.showConfirmDialog(rootPane, "Stages and modes only need to be refreshed\nonce every 2 hours.\n\nAre you sure you want to refresh?", "Confirm refresh", JOptionPane.YES_NO_OPTION);

                if (confirm_refresh == 0)
                {
                    try
                    {
                        //Reloads the stages
                        root = main.getData(true);
                        coopRoot = main.getCoopData(true);
                        festivalsRoot = main.getFestivalsData(true);
                        if (root != null && coopRoot != null)
                        {
                            JOptionPane.showMessageDialog(panel,"Successfully updated database.");
                        }
                    }
                    catch (Exception ex){

                    }
                }
                boolean salmonRunOpen = getSalmonRunOpen();

                if (!salmonRunOpen)
                {
                    try {
                        modeBox.removeItem("Salmon Run");
                    }
                    catch (Exception ex)
                    {

                    }
                }
                else {
                    if (!salmonRunVisible()) {
                        modeBox.addItem("Salmon Run");
                    }
                }
            }
        });

        //Match Start event
        ActionListener matchStart = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePresence();
            }
        };
        //Adds listener to F1 key
        getRootPane().registerKeyboardAction(matchStart,KeyStroke.getKeyStroke("F1"),JComponent.WHEN_IN_FOCUSED_WINDOW);
        //Adds listener to start button
        startbtn.addActionListener(matchStart);


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
        //Assigns Viewing Results to F2 on keyboard
        getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.updatePresence("Viewing Results");
            }
        },KeyStroke.getKeyStroke("F2"),JComponent.WHEN_IN_FOCUSED_WINDOW);
        idleInLobbyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                main.updatePresence("In Lobby");
            }
        });
        changeRegionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setRegion();
            }
        });
    }

    public void initialize()
    {
        boolean salmonRunOpen = getSalmonRunOpen();
        if (salmonRunOpen) {
            modeBox.addItem("Salmon Run");
        }
    }

    public void updatePresence() {
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
        if (currentMode == "Select mode...")
        {
            //Errors out if combo box finds that you haven't selected a mode
            JOptionPane.showMessageDialog(panel,"Please select a mode.","Could not update presence",JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if (currentMode == "Regular Battle")
        {
            //Add 3 minutes in epoch seconds to start time to get endTime
            endTime = startTime + TimeUnit.MINUTES.toSeconds(3) + 1L;
            rule = "Turf War";
        }
        else if (currentMode == "Ranked Battle" || currentMode == "League Battle")
        {
            //Add 5 minutes in epoch seconds to start time to get endTime
            endTime = startTime + TimeUnit.MINUTES.toSeconds(5) + 1L;

            //Gets rule based on the mode selected in the combo box
            if (currentMode == "Ranked Battle")
            {
                rule = root.gachi.get(0).rule.name;
            }
            else if (currentMode == "League Battle")
            {
                rule = root.league.get(0).rule.name;
            }
        }

        //Gets mode text based on combo box
        mode = currentMode;

        //Gets stage based on radio button selection
        if (stageARadioButton.isSelected()) {
            stage = stageARadioButton.getText();
        }
        else if (stageBRadioButton.isSelected()) {
            stage = stageBRadioButton.getText();
        }

        //Updates the presence with the options set.
        main.updatePresence("In Game",rule,startTime,endTime,stage,mode);
    }
    public boolean getSalmonRunOpen() {
        try {
            coopRoot = main.getCoopData(false);
        } catch (java.io.IOException e){
            return false;
        }

        long currentStart = coopRoot.schedules.get(0).start_time;
        long currentEnd = coopRoot.schedules.get(0).end_time;

        long currentTime = System.currentTimeMillis()/1000L;

        if (currentTime < currentEnd && currentTime >= currentStart)
        {
            return true;
        }
        else { return false; }
    }
    public boolean salmonRunVisible() {
        try {
            String mode = (String)modeBox.getItemAt(4);
            if (mode == "Salmon Run"){ return true; }
            else return false;
        } catch (java.lang.IndexOutOfBoundsException e) {
            return false;
        }
    }
    private void coopUpdatePresence() {
        String details = "Wave " + waveSpinner.getValue();
        String state = "Working Shift";
    }

    public void setRegion() {
        Object[] regions = {"NA/AU/NZ", "EU", "JP"};
        Object chosenRegion = JOptionPane.showInputDialog(rootPane, "Please select region", "Region Select", JOptionPane.INFORMATION_MESSAGE, null, regions, regions[0]);
        if (chosenRegion != null) usrPref.put("REGION",chosenRegion.toString());
    }

    public boolean splatfestDetected(String region) {
        long startTime = 0;
        long endTime = 0;
        long currentTime = System.currentTimeMillis() / 1000L;
        if (region == "NA/AU/NZ") {
            startTime = festivalsRoot.na.get(0).times.start;
            endTime = festivalsRoot.na.get(0).times.end;
        }
        else if (region == "EU") {
            startTime = festivalsRoot.eu.get(0).times.start;
            endTime = festivalsRoot.eu.get(0).times.end;
        }
        else if (region == "JP") {
            startTime = festivalsRoot.jp.get(0).times.start;
            endTime = festivalsRoot.jp.get(0).times.end;
        }
        if (currentTime >= startTime && currentTime < endTime) {
            return true;
        }
        else return false;
    }
}
