
package ar.com.aleatoria.akrasia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import ar.com.aleatoria.jerseyclient.JerseyClient;

/**
 * @author astinx The concrete implementation of our service.
 */
public class AkrasiaService extends ServiceManager {
    /** Jersey client stuff */
    public JerseyClient client;

    /** End of jersey client stuff */

    /** Tracker app stuff */
    public final static int TRACKER_APP_BACKUP_REFRESH = 0;

    public final static int TRACKER_APP_BACKUP_RETRIVE = 1;

    public final static int TRACKER_APP_DEPRECATED = 2;

    public final static int TRACKER_APP_STUDYTIMES = 3;

    public Bundle mTrackerBackupBundle;

    public Bundle mTrackerStudyTimes;

    /** End of the Tracker app stuff */

    /** AkrasiaManager app stuff */
    public final static int AKRASIA_MANAGER_EXPORT = 4;

    public final static int REDIRECT_TO_URL = 5;

    /** End of the AkrasiaManager app stuff */
    public AkrasiaService() {
        super();
    }

    /**
     * This are the handlers of the request from the client application. The
     * annotation ExecuteWhen specifies which method must handle one determined
     * request. It uses the "what" annotation attribute like discriminative.
     */

    @ExecutesWhen(what = AkrasiaService.TRACKER_APP_BACKUP_REFRESH)
    public void trackerBundleRefresh(Message msg) {
        mTrackerBackupBundle = msg.getData();
    }

    @ExecutesWhen(what = AkrasiaService.TRACKER_APP_BACKUP_RETRIVE)
    public void trackerBundleRetrive(Message msg) {
        sendMessageToClient(msg.replyTo, AkrasiaService.TRACKER_APP_BACKUP_RETRIVE,
                mTrackerBackupBundle);
    }

    @ExecutesWhen(what = AkrasiaService.AKRASIA_MANAGER_EXPORT)
    public void testRestServer(Message msg) {
        client = JerseyClient.getInstance();
        client.sendMessage();
    }

    @ExecutesWhen(what = 123)
    public void exportStudytimes(Message msg) {
        // This url must be of the service that will take from, for example,
        // $_POST['json_x'],
        // $_POST['taglist_x'], where x is equals to 0 until $_POST['max-id']
        String urlRequest = "http://10.0.2.2/";
        Log.d("AkrasiaService", "Create a new HttpClient and Post Header");
        // Create a new HttpClient and Post Header
        String downloadedString = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(urlRequest);
        Bundle messageBundle = msg.getData();
        int maxIds = messageBundle.getInt("max-id");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>((maxIds * 2) + 1);
        try {
            for (int id = 0; id < maxIds; id++) {
                String json = messageBundle.getString("json_" + id);
                Log.d("AkrasiaService", "Adding json: " + json + " as POST variable");
                String tagList = messageBundle.getString("taglist_" + id);
                Log.d("AkrasiaService", "Adding tagList: " + tagList + " as POST variable");
                nameValuePairs.add(new BasicNameValuePair("json_" + id, json));
                nameValuePairs.add(new BasicNameValuePair("tagList_" + id, tagList));
            }
            Log.d("AkrasiaService", "Adding maxIds: " + maxIds + " as POST variable");
            nameValuePairs.add(new BasicNameValuePair("max-id", "" + maxIds));
            // add data
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // Execute HTTP Post Request
            Log.d("AkrasiaService", "Executing request2");
            HttpResponse response = httpclient.execute(httppost);
            InputStream in = response.getEntity().getContent();
            StringBuilder stringbuilder = new StringBuilder();
            BufferedReader bfrd = new BufferedReader(new InputStreamReader(in), 1024);
            String line;
            while ((line = bfrd.readLine()) != null)
                stringbuilder.append(line);
            downloadedString = stringbuilder.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return downloadedString;
        // sendMessageToClient(msg.replyTo, AkrasiaService.REDIRECT_TO_URL,
        // bundle);
    }

}
