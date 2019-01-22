package splatoon2RichPresence;

import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;
import java.util.*;
import java.io.*;
import java.net.*;
import splat2ink.schedules.*;

public class Main {
    public static void main (String[] args) throws java.io.IOException
    {
        Gson gson = new Gson();

        //Declare and initialize URL going to the actual data from the website.
        //Data source is https://splatoon2.ink
//        URL url = new URL("https://splatoon2.ink/data/schedules.json");
//        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
//
//        //Imitates a browser. Required to access that sweet, sweet data.
//        con.setRequestMethod("GET");
//        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");;
//        InputStream data = con.getInputStream();
        BufferedReader reader = new BufferedReader(new FileReader("F:\\Downloads\\schedules.json"));
        String rd = reader.readLine();
        System.out.println(rd);
        rootObject ro = gson.fromJson(rd,rootObject.class);
    }
}
