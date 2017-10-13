package resources;

import java.io.PrintStream;
import java.util.Observable;

/**
 * The model has three possible states: init state (has an associated
 * sting containing initial instructions), elicitation state 1(the first experiment test), 
 * elicitation state 2 (the second experiment test), and end state (has an associated final message).
 * 
 * This Model is an Observable.
 * 
 * @author Mikhail Gindin
 *
 */
public class Model extends Observable {

	public static final int STATE_UNASSIGNED = 0;
	public static final int INIT_STATE = 1;
	public static final int ELICIT_STATE = 2;
	public static final int ELICIT_STATE2 = 3;
	public static final int END_STATE = 4;

	private PrintStream myOut = System.out;
	
	private String promptMsg;
	private int promptSize;
	private int promptCount;

	private String endMsg;
	private String initMsg;

	private int currentState;

	private long startTimestamp;
	private long stopTimestamp;
	
	private int errorCount = 0;

	/**
	 * Create an instance of this model.
	 */
	public Model() {
		promptSize = 6;
		promptCount = 1;
		promptMsg = "Press the button when it tells you to.";
		endMsg = "Task Complete.";
		initMsg = "Instructions go here.  Button will appear after a delay.  Press to continue.";
		this.setState(Model.STATE_UNASSIGNED);
	}

	private void modelNotify(Object o) {
		// print something to the console, for the sake of tracing program
		// control flow
		// myOut.println("Change happened to the data model");
		// indicate that the state of this Observable has changed
		setChanged();
		// System.out.println(hasChanged());
		// notify the observers that the state has changed
		notifyObservers(o);
	}

	/**
	 * @return the prompt that is current in the iteration.
	 */
	public String getCurrentPromptMessage() {
		return promptMsg;
	}
	
	/**
	 * Sets the prompt message.
	 * @param message
	 */
	public void setCurrentPromptMessage(String message) {
		promptMsg = message;
	}

	/**
	 * @return a string which indicates, via a stylized string, the position of
	 *         the prompt that is current in the iteration, relative to all
	 *         prompts. For example: "(Prompt 2/9): " The numerator of the
	 *         fraction will be less than or equal to the denominator.
	 */
	public String getPromptRelativePositionString() {
		return "(Prompt " + promptCount + "/" + promptSize + "): ";
	}

	/**
	 * Advances the iterator to the next prompt. Should ensure first that the
	 * iterator has not reached past the end of the set of prompts.
	 */
	public void setPromptToNext() {
		promptCount++;
	}

	/**
	 * @return whether the iterator has reached past the end of the set of
	 *         prompts.
	 */
	public boolean isPromptsRemaining() {
		return promptCount < promptSize;
	}

	/**
	 * Causes a message to printed to a PrintStream with current prompt and
	 * elapsed time.
	 * 
	 * Pre - the method recordStopTimeStamp() has been invoked after
	 * recordStartTimeStamp()
	 */
	public void recordDuration() {
		myOut.println(getCurrentPromptMessage() + "\tTime Elapsed (msec): " + (stopTimestamp - startTimestamp));
	}

	/**
	 * @param modelState
	 *            the state for the model. Passed parameter must be one of the
	 *            class fields.
	 * 
	 *            Mutate the current state of this model.
	 */
	public void setState(int modelState) {
		currentState = modelState;
		modelNotify(currentState);
	}

	/**
	 * @return the current state of this model (value will be one of the class
	 *         fields).
	 */
	public int getCurrentState() {
		return currentState;
	}

	/**
	 * @return the initialization message associated with the INIT_STATE of this
	 *         model.
	 */
	public String getInitMsg() {
		return initMsg;
	}

	/**
	 * Initiates the iterator over the prompts.
	 */
	public void setPromptToFirst() {
		promptCount = 1;
	}

	/**
	 * @return the finish message associated with the END_STATE of this model.
	 */
	public String getEndMsg() {
		return endMsg;
	}

	/**
	 * Tell this model to stop recording elapsed time.
	 */
	public void recordStopTimeStamp() {
		stopTimestamp = System.currentTimeMillis();
	}

	/**
	 * Tell this model to start recording elapsed time.
	 */
	public void recordStartTimeStamp() {
		startTimestamp = System.currentTimeMillis();
	}
	
	/**
	 * Tell this model to start recording elapsed time in 't' amount of time.
	 */
	public void recordStartTimeStamp(int t) {
		startTimestamp = t + System.currentTimeMillis();
	}
	
	/**
	 * @return the current number of errors
	 */
	public int getErrorCount() {
		return errorCount;
	}
	
	/**
	 * Increment error count
	 */
	public void incrementErrorCount() {
		errorCount++;
	}
	
	/**
	 * Reset the error count
	 */
	public void resetErrorCount() {
		errorCount = 0;
	}
}
