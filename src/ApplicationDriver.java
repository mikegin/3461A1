import resources.AppRunnable;

/**
 * This class encapsulates a basic app, which is a simple experiment used to measure
 * user speed and accuracy of interaction with a mouse directly vs indirectly. The experiment
 * consists of two tests. The first one that involves the user pressing a button once text on 
 * the button changes. The other involves the user pressing a button once a separate colored
 * box changes color. Speed of the click and number of errors are recorded.
 * 
 * @author Mikhail Gindin
 *
 */
public class ApplicationDriver {

	/**
	 * This method launches the app. The app does not make use of command line
	 * arguments. The app never terminates on its own.
	 * 
	 * @param args
	 *            none
	 */
	public static void main(String[] args) {

		// instantiate a Runnable object which can then be passed to the method
		// invocation below
		Runnable theRunnable = new AppRunnable();
		// this invocation serves to launch the event dispatching thread and
		// to place the runnable on that thread
		javax.swing.SwingUtilities.invokeLater(theRunnable);

		/*
		 * [Note: The body of this main method could potentially be reduced to a
		 * single line by using of an anonymous inner class. However, the
		 * instance has been made explicit here for the sake of clarity]
		 */
	}

}
