package javaexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.DatatypeConverter;
import org.json.JSONObject;

public class JavaExample {

    public static JSONObject get_smhi_data(
            String customerId, String subscriptionId, String password,
            String place, String product, String typeCase, String date)
            throws NoSuchAlgorithmException, MalformedURLException, IOException {

        // Create the headers
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update((today + ":" + password).getBytes());
        String key = customerId + ":" + subscriptionId + ":" +
                DatatypeConverter.printHexBinary(md5.digest()).toLowerCase();

        String accept = "application/json";

        // Create the URL
        String surl = "http://fastighetsstyrning.smhi.se/ws/enloss/" + place + "/" + product;

        if (typeCase != null) {
            surl += "/" + typeCase;
        }

        if (date != null) {
            surl += "/" + date;
        }

        URL url = new URL(surl);

        // Perform request to API
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("accept", accept);
        conn.setRequestProperty("key", key);
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            System.err.println(conn.getErrorStream());
            return null;
        } else {
            String inputLine;
            StringBuilder body = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            while ((inputLine = in.readLine()) != null) {
                body.append(inputLine);
            }

            JSONObject json = new JSONObject(body.toString());
            return json;
        }
    }
}
