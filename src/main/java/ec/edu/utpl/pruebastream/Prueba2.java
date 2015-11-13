/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.utpl.pruebastream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

/**
 *
 * @author utpl
 */
public class Prueba2 {

    private static Logger logger = Logger.getLogger(
            Prueba2.class.getName());
    private static final String API_PROTOCOL = "http";
    private static final String API_DOMAIN = "stream.twitter.com";
    private static final String API_URL = "1/";
    private static final String FILTER_URL = API_URL + "statuses/filter.json";

    public static void readFrom(HttpRequestBase requestMethod) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(requestMethod);
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new IOException("No entity");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(
                entity.getContent(), "UTF-8"));
        while (true) {
            String line = br.readLine();
            if (line != null && !line.isEmpty()) {
                System.out.println(line);
            }
        }
    }

    public static HttpRequestBase getConnection()
            throws OAuthMessageSignerException,
            OAuthExpectationFailedException, OAuthCommunicationException,
            IllegalStateException, IOException {
        String base = FILTER_URL;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // No spaces
//        params.add(new BasicNameValuePair("track", "will"));
        // Spaces
        params.add(new BasicNameValuePair("track", "ecuador"));

        HttpPost method = new HttpPost(API_PROTOCOL + "://" + API_DOMAIN + "/"
                + base);
        logger.info("" + method.getURI());

        UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(params,
                HTTP.UTF_8);
        method.setEntity(postEntity);

        String consumerKey = "M5TVM4VOBMqoUR40JwQQQEy7w";
        String consumerSecret = "ba9POy5FpiY8eVln4f2GFDpaaswuCQ3vVm3L66d5IoSRWntqaL";
        CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(
                consumerKey, consumerSecret);
        String token = "157071645-WR4AxDWYw41Du7YujYD8tcyFzvuZmDSFO9rxNGya";
        String tokenSecret = "sHXZDSuxG3MOHYofkdWk8D9M8WqAiVZoxf4mYSrTdTP6Y";
        consumer.setTokenWithSecret(token, tokenSecret);
        consumer.sign(method);

        return method;
    }

//    public static void main(String[] args) throws Exception {
//        readFrom(getConnection());
//    }
}
