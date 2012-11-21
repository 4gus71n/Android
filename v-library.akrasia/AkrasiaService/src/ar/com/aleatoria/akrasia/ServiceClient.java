
package ar.com.aleatoria.akrasia;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * @author astinx This is the API of the <code>ServiceManager</code>. If you
 *         want your application to communicate with the service, you just have
 *         to have an instance of this API in your Activity.
 */
public class ServiceClient {
    /** Messenger for communicating with service. */
    Messenger mServiceConnection = null;

    ServiceSingleCallback mAsSoonIsConnectedCallback;

    /** Flag indicating whether we have called bind on the service. */
    private boolean mIsBound = false;

    private Messenger mMessageDispatcher;

    /**
     * Class for interacting with the main interface of the service. This
     * callback takes care of setup <code>mServiceConnection</code> and
     * therefore, start to talk with the service.
     */
    private final ServiceConnection mConnectionCallback = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mServiceConnection = new Messenger(service);
            if (mAsSoonIsConnectedCallback != null)
                mAsSoonIsConnectedCallback.run();
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mServiceConnection = null;
        }
    };

    /**
     * This method makes the binding between the service and the client
     * application.
     * 
     * @param intent: An intent of the concrete implementation of the
     *            <code>ServiceManager</code>
     * @param activity: The Activity thats want communicate with the service.
     */
    public void doBindService(Intent intent, Activity activity) {
        Log.d("AkrasiaService", "The application is trying to bind to the service...");
        activity.getApplicationContext().startService(intent);
        activity.bindService(intent, mConnectionCallback, Context.BIND_AUTO_CREATE);
    }

    public void doUnbindService(Activity activity) {
        if (mIsBound) {
            activity.unbindService(mConnectionCallback);
            mIsBound = false;
        }
    }

    /**
     * This method sends a single message to the service.
     * 
     * @param what: The what (subject) of the message.
     */
    public void sendMessage(int what) {
        Message msg = Message.obtain();
        try {
            Bundle bundle = new Bundle();
            msg.setData(bundle);
            msg.what = what;
            msg.replyTo = mMessageDispatcher;
            Log.d("AkrasiaService", "The application is going to send a message to the service");
            mServiceConnection.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sends a message to the service with a bundle attached.
     * 
     * @param what: The what (subject) of the message.
     * @param bundle: The data bundle attached to the message.
     */
    public void sendMessage(int what, Bundle bundle) {
        Message msg = Message.obtain();
        try {
            msg.setData(bundle);
            msg.what = what;
            msg.replyTo = mMessageDispatcher;
            mServiceConnection.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setIncomingHandler(Handler handler) {
        mMessageDispatcher = new Messenger(handler);
    }

    public boolean isConnected() {
        return mIsBound;
    }

    /**
     * With this method you can specify a method that will be executed ass soon
     * the connection is established.
     * 
     * @param service: A instance of the callback.
     */
    public void setAsSoonMethod(ServiceSingleCallback service) {
        mAsSoonIsConnectedCallback = service;
    }

}
