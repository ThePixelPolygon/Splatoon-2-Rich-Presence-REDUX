package splatoon2RichPresence;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import splat2ink.schedules.*;

public class MainForm extends JFrame {
    private JComboBox modeBox;
    private JRadioButton stageARadioButton;
    private JRadioButton stageBRadioButton;
    private JPanel panel;

    Main main = new Main();

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
    }

    public void initialize()
    {
        modeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                rootObject root;
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
