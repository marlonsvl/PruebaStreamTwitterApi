package ec.edu.utpl.pruebastream;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Hello world!
 *
 */
public class App {

    private static final String accessToken = "157071645-WR4AxDWYw41Du7YujYD8tcyFzvuZmDSFO9rxNGya";
    private static final String accessSecret = "sHXZDSuxG3MOHYofkdWk8D9M8WqAiVZoxf4mYSrTdTP6Y";
    private static final String consumerKey = "M5TVM4VOBMqoUR40JwQQQEy7w";
    private static final String consumerSecret = "ba9POy5FpiY8eVln4f2GFDpaaswuCQ3vVm3L66d5IoSRWntqaL";
    
//    private static final String accessToken = "2359821270-ctQbGFd9CmQvoJBISswBwwpFUgmRs2UNKbdvIyz";
//    private static final String accessSecret = "D3Bn4Xrq3n5DZFYrJAoPhNpD8Xh4hJAmiA8jFZ6pVNV02";
//    private static final String consumerKey = "5VzfrntaDmgvlFhuYk89YVJ8l";
//    private static final String consumerSecret = " BdqBw5X5WW1ejRw34owycNSmxvIJVaga0A7tlMPtaqZLi3JQnJ";
    
    
    private static OAuthConsumer consumer;

    public static void main(String[] args) {
//        consumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
//        consumer.setTokenWithSecret(accessToken, accessSecret);
//        HttpURLConnection request = null;
//        URL url;
//        try {
//            url = new URL("https://stream.twitter.com/1.1/statuses/filter.json?stall_warnings=true&delimited=length&track=twitterapi");
//            request = (HttpURLConnection) url.openConnection();
//            consumer.sign(request);
//            request.connect();
//            String encoding = request.getContentEncoding();
//            encoding = encoding == null ? "UTF-8" : encoding;
//            //InputStream in = request.getInputStream();
//            //String body = IOUtils.toString(in, encoding);
//            BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream) request.getContent()));
//            
//            System.out.println(request.getResponseCode());
//            System.out.println(request.getResponseMessage());
//            //System.out.println(body);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//
//            //while(true){
//            do {
//                line = reader.readLine();
//                System.out.println(line);
//            } while (line.length() < 1);
//
//            //}
//
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (OAuthMessageSignerException ex) {
//            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (OAuthExpectationFailedException ex) {
//            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (OAuthCommunicationException ex) {
//            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            if (request != null) {
//                request.disconnect();
//            }
//        }
        
        getTweets();
        
        System.out.println("Hello World!");
    }

    public static void next() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpURLConnection request = null;
        HttpGet httpget = new HttpGet("https://stream.twitter.com/1.1/statuses/sample.json?stall_warnings=true&delimited=length&track=twitterapi");
        CloseableHttpResponse response = null;
        consumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(accessToken, accessSecret);
        try {
            consumer.sign(response);
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                int byteone = instream.read();
                int bytetwo = instream.read();
                System.out.println(byteone+" : "+bytetwo);
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                String line = null;
                StringBuffer buffer = new StringBuffer();
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                    if(buffer.length() > 20000) break;
                }
                System.out.println("buffer: "+buffer.toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthMessageSignerException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthExpectationFailedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthCommunicationException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public static void getTweets(){
        BlockingDeque<String> msgqueue = new LinkedBlockingDeque<String>(100000);
        BlockingDeque<Event> eventQueue = new LinkedBlockingDeque<Event>(1000);
        
        
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        List<String> terms = Lists.newArrayList("#startups");
        hosebirdEndpoint.trackTerms(terms);
        
        Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, accessToken, accessSecret);   
        
        ClientBuilder builder = new ClientBuilder()
                .name("Cliente utpl")
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgqueue))
                .eventMessageQueue(eventQueue);
        
        Client hosebirdClient = builder.build();
        System.out.println(Constants.CURRENT_API_VERSION);
        hosebirdClient.getEndpoint().setApiVersion(Constants.CURRENT_API_VERSION);
        hosebirdClient.connect();
        int c = 1;
        while(!hosebirdClient.isDone()){
            
            try {
                String msg = msgqueue.take();
                System.out.println("linea "+c+": "+msg);
            } catch (InterruptedException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
            c++;
            //hosebirdClient.stop();
        }
        
        
    }
    
}
