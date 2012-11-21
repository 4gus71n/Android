package ar.com.aleatoria.ue.stuff;

import java.util.concurrent.atomic.AtomicInteger;

// Comes from: http://stackoverflow.com/questions/6790623/programmatic-views-how-to-set-unique-ids/
public class ViewId {

	private static ViewId INSTANCE = new ViewId();

	private AtomicInteger seq;

	private ViewId() {
		seq = new AtomicInteger(Integer.MAX_VALUE);
	}

	public int getUniqueId() {
		return seq.decrementAndGet();
	}

	public static ViewId getInstance() {
		return INSTANCE;
	}
}
