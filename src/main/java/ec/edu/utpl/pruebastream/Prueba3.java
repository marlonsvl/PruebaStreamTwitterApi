/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.utpl.pruebastream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author utpl
 */
public class Prueba3 extends Thread{
     static String STORAGE_DIR = "/tmp";
    static long BYTES_PER_FILE = 64 * 1024 * 1024;
    //
    public long Messages = 0;
    public long Bytes = 0;
    public long Timestamp = 0;

    private String accessToken = "";
    private String accessSecret = "";
    private String consumerKey = "";
    private String consumerSecret = ""; 
    File file = null;
    FileWriter fw = null;
    private String feedUrl;
    private String filePrefix;
    boolean isRunning = true;
    long bytesWritten = 0;

//    public static void main(String[] args) {
//        Prueba3 t = new Prueba3(
//            "157071645-WR4AxDWYw41Du7YujYD8tcyFzvuZmDSFO9rxNGya", 
//            "sHXZDSuxG3MOHYofkdWk8D9M8WqAiVZoxf4mYSrTdTP6Y",
//            "M5TVM4VOBMqoUR40JwQQQEy7w",
//            "ba9POy5FpiY8eVln4f2GFDpaaswuCQ3vVm3L66d5IoSRWntqaL",
//            "http://stream.twitter.com/1.1/statuses/sample.json", "ecuador");
//        t.start();
//    }

    public Prueba3(String accessToken, String accessSecret, String consumerKey, String consumerSecret, String url, String prefix) {
        this.accessToken = accessToken;
        this.accessSecret = accessSecret;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        feedUrl = url;
        filePrefix = prefix;
        Timestamp = System.currentTimeMillis();
    }

    /**
     * @throws IOException
     */
    private void rotateFile() throws IOException {
        // Handle the existing file
        if (fw != null)
            fw.close();
        // Create the next file
        file = new File(STORAGE_DIR, filePrefix + "-"
                + System.currentTimeMillis() + ".json");
        bytesWritten = 0;
        fw = new FileWriter(file);
        System.out.println("Writing to " + file.getAbsolutePath());
    }

    /**
     * @see java.lang.Thread#run()
     */
    public void run() {
        // Open the initial file
        try { rotateFile(); } catch (IOException e) { e.printStackTrace(); return; }
        // Run loop
        while (isRunning) {
            try {

                OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
                consumer.setTokenWithSecret(accessToken, accessSecret);
                HttpGet request = new HttpGet(feedUrl);
                consumer.sign(request);

                HttpClient client = HttpClientBuilder.create().build();
                HttpResponse response = client.execute(request);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
                while (true) {
                    String line = reader.readLine();
                    if (line == null)
                        break;
                    if (line.length() > 0) {
                        if (bytesWritten + line.length() + 1 > BYTES_PER_FILE)
                            rotateFile();
                        fw.write(line + "\n");
                        bytesWritten += line.length() + 1;
                        Messages++;
                        Bytes += line.length() + 1;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Sleeping before reconnect...");
            try { Thread.sleep(1500); } catch (Exception e) { }
        }
    }

}
