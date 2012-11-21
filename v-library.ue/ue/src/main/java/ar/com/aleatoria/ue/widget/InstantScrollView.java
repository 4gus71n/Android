package ar.com.aleatoria.ue.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class InstantScrollView extends HorizontalScrollView {

	public InstantScrollView(Context context) {
		super(context);
	}

	public InstantScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InstantScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// From
	// http://stackoverflow.com/questions/4720469/horizontalscrollview-auto-scroll-to-end-when-new-views-are-added
	private OnLayoutListener mListener;

	private interface OnLayoutListener {
		void onLayout();
	}

	public void fullScrollOnLayout(final int direction) {
		mListener = new OnLayoutListener() {
			@Override
			public void onLayout() {
				fullScroll(direction);
				mListener = null;
			}
		};
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (mListener != null)
			mListener.onLayout();
	}
}
