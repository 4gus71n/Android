package ar.com.aleatoria.ue.widget;

import org.joda.time.DateTime;
import org.joda.time.Hours;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.CheckBox;
import ar.com.aleatoria.ue.R;
import ar.com.aleatoria.ue.dao.Instant;

public class InstantWidget extends CheckBox {

	private static final int[] TMINUS4_STATE_SET = { R.attr.state_tminus4 };
	private static final int[] TMINUS3_STATE_SET = { R.attr.state_tminus3 };
	private static final int[] TMINUS2_STATE_SET = { R.attr.state_tminus2 };
	private static final int[] TMINUS1_STATE_SET = { R.attr.state_tminus1 };
	private static final int[] FAILED_STATE_SET = { R.attr.state_failed };
	private static final int[] CHECKED_STATE_SET = { R.attr._state_checked };

	public enum State {
		tm4, tm3, tm2, tm1, failed, checked;
	}

	private State currentState = State.tm4;
	private Instant boundInstant;

	public InstantWidget(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public InstantWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InstantWidget(Context context) {
		super(context);
	}
	
	public State getState() {
		return this.currentState;
	}

	public void setBoundInstant(Instant instant) {
		this.boundInstant = instant;
		this.updateCurrentState();
	}
	
	public Instant getBoundInstant() {
		return this.boundInstant;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		this.updateCurrentState();
		super.onDraw(canvas);
	}

	private void updateCurrentState() {
		if (boundInstant.getDate_registered() != null) {
		 	currentState = State.checked;
			return;
		}

		DateTime dateCreated = new DateTime(boundInstant.getDate_created());
		Hours hoursSinceCreated = Hours.hoursBetween(dateCreated,
				DateTime.now());
		
		if (hoursSinceCreated.equals(0))
			currentState = State.tm4;
		else if (hoursSinceCreated.equals(Hours.hours(1)))
			currentState = State.tm3;
		else if (hoursSinceCreated.equals(Hours.hours(2)))
			currentState = State.tm2;
		else if (hoursSinceCreated.equals(Hours.hours(3)))
			currentState = State.tm1;
		else if (hoursSinceCreated.isGreaterThan(Hours.hours(3)))
			currentState = State.failed;
	}

	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
		if (currentState == null)
			return drawableState;
		
		switch (currentState) {
		case tm4:
			mergeDrawableStates(drawableState, TMINUS4_STATE_SET);
			break;
		case tm3:
			mergeDrawableStates(drawableState, TMINUS3_STATE_SET);
			break;
		case tm2:
			mergeDrawableStates(drawableState, TMINUS2_STATE_SET);
			break;
		case tm1:
			mergeDrawableStates(drawableState, TMINUS1_STATE_SET);
			break;
		case failed:
			mergeDrawableStates(drawableState, FAILED_STATE_SET);
			break;
		case checked:
			mergeDrawableStates(drawableState, CHECKED_STATE_SET);
			break;
		default:
			break;
		}
		return drawableState;
	}

}
