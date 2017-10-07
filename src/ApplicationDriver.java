import resources.AppRunnable;

/**
 * This class encapsulates a basic app, which is a very simple newsfeed
 * displayer.
 * 
 * @author mb
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
