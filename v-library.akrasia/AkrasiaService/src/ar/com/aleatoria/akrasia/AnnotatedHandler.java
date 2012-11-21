package ar.com.aleatoria.akrasia;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @author Carlitos Tevez
 * This class is responsible for hiding the management process of the incoming messages 
 * from the service to the client application. If you know that the service can 
 * send you a message with his "what" equals "foo" then all you have to do is create 
 * a method  called fooHandler(Message message) that this annotated as 
 * @ExecuteWhen(what = YourService.FOO ) where YourService.FOO is a static field 
 * of type int. You only must implement a handler that extends the AnnotatedHandler
 * and put these methods inside him.
 */
public class AnnotatedHandler extends Handler {
	
	protected HashMap<Integer, Method> magicSwitch = new HashMap<Integer, Method>();
	
	public AnnotatedHandler() {
		try {
			for (Method method : this.getClass().getMethods() ) {
				if (method.isAnnotationPresent(ExecutesWhen.class)) {
					try {
						ExecutesWhen a = method
								.getAnnotation(ExecutesWhen.class);
						magicSwitch.put(a.what(), method);
						Log.d("AnnotatedHandler","AnnotatedHandler now knows how handle a "+method.getName()+" and id"+a.what());
					} catch (Throwable ex) {
						ex.printStackTrace();
					}
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleMessage(Message msg) {
    	try {
    		Log.d("AnnotatedHandler","The service is going to manage a message from the client with what="+msg.what);
			Method met = magicSwitch.get(msg.what);
			if (met == null) {
				throw new NonExistingWhatException();
			} else {
				met.invoke(AnnotatedHandler.this, msg);
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
