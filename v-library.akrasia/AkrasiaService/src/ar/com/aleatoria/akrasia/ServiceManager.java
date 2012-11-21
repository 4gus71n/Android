
package ar.com.aleatoria.akrasia;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * @author astinx This class mainly takes charge of using the @ ExecuteWhen
 *         (what = aWhat), which serves to simplify the process of message
 *         handling. Also responsible for implementing some common aspects of
 *         services.
 */
public class ServiceManager extends Service {
    private NotificationManager mNM;

    protected HashMap<Integer, Method> magicSwitch = new HashMap<Integer, Method>();

    public ServiceManager() {
        try {
            Log.d("AkrasiaService", "Vamos a buscar annotations en :"
                    + getClass().getCanonicalName());
            for (Method method : Class.forName(this.getClass().getCanonicalName()).getMethods()) {
                if (method.isAnnotationPresent(ExecutesWhen.class)) {
                    try {
                        ExecutesWhen a = method.getAnnotation(ExecutesWhen.class);
                        magicSwitch.put(a.what(), method);
                        Log.d("AkrasiaService",
                                "AkrasiaService now knows how handle a " + method.getName()
                                        + " with id=" + a.what());
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("AkrasiaService",
                    "The service is going to manage a message from the client with what="
                            + msg.what);
            try {
                Method met = magicSwitch.get(msg.what);
                if (met == null) {
                    throw new NonExistingWhatException();
                } else {
                    met.invoke(ServiceManager.this, msg);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessageInBox = new Messenger(new ServiceHandler());

    /**
     * Sends a message to the replyTo client.
     * 
     * @param replyTo: The <code>Messenger</code> to reply the message.
     * @param what: The what (subject).
     * @param bundle: A data bundle that will go attached to the message.
     */
    protected void sendMessageToClient(Messenger replyTo, int what, Bundle bundle) {
        try {
            Message msg = Message.obtain(null, what, 0, 0);
            msg.setData(bundle);
            replyTo.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessageInBox.getBinder();
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        showNotification();
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // Here we immplement the notification
    }
}
