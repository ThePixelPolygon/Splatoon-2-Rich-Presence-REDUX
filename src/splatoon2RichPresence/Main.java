package splatoon2RichPresence;

import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.net.*;
import splat2ink.schedules.*;
import club.minnced.discord.rpc.*;
import com.sun.jna.*;

public class Main {
    public static DiscordRPC rpc = DiscordRPC.INSTANCE;
    //public static DiscordEventHandlers handlers = new DiscordEventHandlers();
    public static void initDiscord(){
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> System.out.println("Discord Ready!");
        rpc.Discord_Initialize("459809227339202570", handlers,true,null);
    }
    public static void main (String[] args) throws java.lang.ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException, java.io.IOException
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {

        }
        MainForm mf = new MainForm();

        mf.setVisible(true);

        rootObject root = getData(true);
        initDiscord();
    }

    private static String rd = "";

    public static rootObject getData(boolean reload) throws java.io.IOException
    {
        //Reloads if boolean was true in constructor
        if (reload)
        {
            //Declare and initialize URL going to the actual data from the website.
            //Data source is https://splatoon2.ink
            URL url = new URL("https://splatoon2.ink/data/schedules.json");
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();

            //Imitates a browser. Required to access that sweet, sweet data.
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");

            //Converts the data into an input stream
            InputStream data = null;
            try
            {
                data = con.getInputStream();
            } catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Failed to update stage and mode data.\nPlease check your internet connection","Refresh Failed",JOptionPane.ERROR_MESSAGE);
                return null;
            }

            //Declare and initialize reader
            BufferedReader reader = new BufferedReader(new InputStreamReader(data));

            //Reads the data from the reader.
            rd = reader.readLine();
            data.close();
        }
        Gson gson = new Gson();

        //Converts the JSON data and maps it to the class.
        rootObject root = gson.fromJson(rd,rootObject.class);
        return root;
    }


    public void discordClose() {
        rpc.Discord_Shutdown();
    }


    public void updatePresence(String state) {
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.largeImageKey = "cover";
        presence.state = state;
        rpc.Discord_UpdatePresence(presence);
    }
    public void updatePresence(String state, String details, long startTime, long endTime, String stage ,String mode)
    {
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.details = details;
        presence.state = state;
        presence.startTimestamp = startTime;
        presence.endTimestamp = endTime;

        String largeImageKey = stage.replaceAll("\\s","_");
        largeImageKey = largeImageKey.toLowerCase();

        presence.largeImageKey = largeImageKey;
        presence.largeImageText = stage;

        String smallImageKey = mode.substring(0,mode.indexOf(" Battle"));
        smallImageKey = smallImageKey.toLowerCase();

        presence.smallImageKey = smallImageKey;
        presence.smallImageText = mode;

        rpc.Discord_UpdatePresence(presence);
    }
}
