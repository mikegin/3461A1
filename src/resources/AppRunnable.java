package resources;

/**
 * 
 * This class encapsulates a runnable which can be placed upon the event
 * dispatching thread. The runnable makes use of the MVC architecture pattern.
 * The {@link #run()} method of this Runnable simply invokes the method
 * {@link #createAndShowGUI()}.
 * 
 * @author mb
 *
 */
public class AppRunnable implements Runnable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// we invoke the following static method as the single statement
		// of the body of this run method
		createAndShowGUI();
	}

	/**
	 * This method does the following:
	 * <ol>
	 * <li>instantiates this app's model (as encapsulated by
	 * {@link resources.Model})</li>
	 * 
	 * <li>instantiates this app's view (as encapsulated by
	 * {@link resources.ViewController}), passing to it the Model</li>
	 * 
	 * </ol>
	 * 
	 * The creation of the {@link resources.ViewController} instance creates an event
	 * dispatching component on the EDT which remains there until it is
	 * destroyed.
	 * 
	 * The view and model are set up in an Observer pattern. The View is an
	 * observer of the Model.
	 * 
	 * This example employs a pure interpretation of MVC. In this configuration,
	 * the View presents only information (and no interactive widgets). The
	 * app's JFrame is the UI, not the View.
	 * 
	 * In this example, there is no Controller and no way for the user to
	 * interact with this UI. The Model's state is being modified through the
	 * use of a sort-of kludgey technique. This is being done for illustrative
	 * purposes.
	 */
	public static void createAndShowGUI() {
		Model model = new Model();

		/*
		 * We create the view. The View encapsulates the visual depiction of the
		 * current state of the model. Nothing in the view affords
		 * interactivity.  
		 */
		ViewController viewControllerDelegate = new ViewController(model);

		/*
		 * Here we install the view as a listener on the model. This way, when
		 * the model changes, the view will be notified and the view will know to redraw
		 * itself. This statement actually should be invoked from within the
		 * view constructor, but has been exposed here for pedagogical purposes.
		 */
		model.addObserver(viewControllerDelegate);

	}
}
