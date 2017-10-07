package resources;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

/**
 * This class encapsulates a GUI, a component of which is a view-controller
 * delegate. This object is an observer of the Model.
 * 
 * This GUI presents views that are tailored to the current state of the Model.
 * Changes to the view are driven by state changes to the Model. View
 * initializes with an active button labeled "Press to start". This action
 * launches the first state change to the Model.
 * 
 * @author mb
 *
 */
public class ViewController extends JFrame implements Observer, ActionListener {

	private static final long serialVersionUID = 2L; // needed by serializers

	private PrintStream myOut = System.out;

	private int promptDisplayDuration = 500; // in msec

	private SwingWorker<Void, Void> displayWorker;

	/*
	 * here we maintain a reference to the model so that the view-controller
	 * delegate may query the model about its state, using the model's services
	 * for doing so.
	 */
	private Model theModel;

	private JPanel infoPanel;
	private JPanel widgetPanel;
	private JPanelWithSquare colorPanel;

	private JLabel theLabel;
	private JButton theButton;

	/**
	 * Creates the view-controller delegate
	 * 
	 * @param model
	 *            the Model
	 */
	public ViewController(Model model) {
		theModel = model;

		/*
		 * Set up some basic aspects of the frame
		 */
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension thisScreen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int) thisScreen.getWidth() / 2, (int) thisScreen.getHeight() / 2);
		this.setTitle(this.getClass().getName());
		this.setLocationByPlatform(true);
		// this methods asks the window manager to position the frame in the
		// centre of the screen
		this.setLocationRelativeTo(null);

		/*
		 * Here we set up the GUI. The first panel is for the prompt and the
		 * second panel is for the button.
		 */
		infoPanel = new JPanel();
		widgetPanel = new JPanel();
		infoPanel.setBackground(Color.WHITE);
		widgetPanel.setBackground(Color.WHITE);
		
		// the default Layout Manager for JPanel is FlowLayout. Make choice of
		// Layout Manager explicit here
		infoPanel.setLayout(new BorderLayout());
		widgetPanel.setLayout(new BorderLayout());
		
		this.getContentPane().setLayout(new BorderLayout());

		theLabel = new JLabel();
		theLabel.setText("");//

		infoPanel.add(theLabel, BorderLayout.CENTER);

		theButton = new JButton("Press to Start");
		theButton.setEnabled(true);
		widgetPanel.add(theButton, BorderLayout.CENTER);

		this.getContentPane().add(infoPanel, BorderLayout.NORTH);
		this.getContentPane().add(widgetPanel, BorderLayout.SOUTH);

		// here we install this object (ActionListener) on the button so that we
		// may detect user actions that may be dispatched from it.
		theButton.addActionListener(this);

		/*
		 * Here we install the view as a listener on the model. This way, when
		 * the model changes, the view will be notified and the view will know
		 * to redraw itself. This statement is presently commented out. We
		 * instead exposed this statement in the Runnable for pedagogical
		 * purposes.
		 */
		// model.addObserver(this);

		this.setVisible(true);

		// this method asks the frame layout manager to size the frame so that
		// all its contents are at or above their preferred sizes
		this.pack();
		// make this component visible (do not assume that it will be visible by
		// default)
		this.setVisible(true);
	}

	@Override
	public Dimension getPreferredSize() {
		// find the dimensions of the screen and a dimension that is derive one
		// quarter of the size
		Dimension thisScreen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension targetSize = new Dimension((int) thisScreen.getWidth() / 4, (int) thisScreen.getHeight() / 4);
		return targetSize;
	}

	public void update(Observable o, Object arg) {
		// the model's state has updated
		// myOut.println("model observer detected change: " +
		// theModel.getCurrentState());
		if (theModel.getCurrentState() == Model.INIT_STATE) {
			theLabel.setText(theModel.getInitMsg());
			theButton.setText("Understood");
			theButton.setEnabled(false);
			displayWorker = this.createWorkerDelayedEnabledButton(promptDisplayDuration);
			displayWorker.execute();
		} else if (theModel.getCurrentState() == Model.ELICIT_STATE) {
			theButton.setText("Button");
			theButton.setEnabled(false);
			
			theModel.recordStartTimeStamp(promptDisplayDuration);
			theLabel.setText(theModel.getPromptRelativePositionString() + theModel.getCurrentPromptMessage());
			displayWorker = this.createWorkerDelayedEnabledButton(promptDisplayDuration);
			displayWorker.execute();
		} else if (theModel.getCurrentState() == Model.ELICIT_STATE2) {
			theButton.setText("Button");
			theButton.setEnabled(true);
			theLabel.setText(theModel.getPromptRelativePositionString() + theModel.getCurrentPromptMessage());
			colorPanel.setSqColor(Color.BLUE);
			colorPanel.repaint();
			
			int delay = (int)Math.random()*3000 + 1000;//random delay between one and 3 seconds
			theModel.recordStartTimeStamp(delay);
			displayWorker = this.createWorkerDelayedColorChange(delay);
			displayWorker.execute();
		} else if (theModel.getCurrentState() == Model.END_STATE) {
			theLabel.setText(theModel.getEndMsg());
			theButton.setText("");
			theButton.setEnabled(false);
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(theButton)) {
			/*
			 * in response to button presses, we modify model attributes. We let
			 * this view's redraw of itself be delegated to when it is informed
			 * of the state change
			 */
			if (theModel.getCurrentState() == Model.STATE_UNASSIGNED) {
				theModel.setState(Model.INIT_STATE);
			} else if (theModel.getCurrentState() == Model.INIT_STATE) {
				theModel.setPromptToFirst();
				theModel.setState(Model.ELICIT_STATE);
			} else if (theModel.getCurrentState() == Model.ELICIT_STATE) {
				theModel.recordStopTimeStamp();
				theModel.recordDuration();
				if (theModel.isPromptsRemaining()) {
					theModel.setPromptToNext();
					theModel.setState(Model.ELICIT_STATE);
				} else {
					System.out.println("==================================");
					setupTest2();
					theModel.setPromptToFirst();
					theModel.setCurrentPromptMessage("Press the button on color change");
					theModel.setState(Model.ELICIT_STATE2);
				}
			} else if (theModel.getCurrentState() == Model.ELICIT_STATE2) {
				if(colorPanel.getSqColor().equals(Color.BLUE)){
					theModel.incrementErrorCount();
				}
				else{
					theModel.recordStopTimeStamp();
					theModel.recordDuration();
					if (theModel.isPromptsRemaining()) {
						theModel.setPromptToNext();
						theModel.setState(Model.ELICIT_STATE2);
					} else {
						System.out.println("Error count: " + theModel.getErrorCount());
						theModel.setState(Model.END_STATE);
					}
				}
			} else if (theModel.getCurrentState() == Model.END_STATE) {
				// control should never arrive here. When in end state, button
				// is not enabled
			}
			// myOut.println("Button press");
		}

	}
	
	private void setupTest2() {
		colorPanel = new JPanelWithSquare();
		colorPanel.setSqColor(Color.BLUE);
		colorPanel.setBackground(Color.GRAY);
		colorPanel.setLayout(new BorderLayout());
		this.getContentPane().add(colorPanel, BorderLayout.CENTER);
	}

	/**
	 * Launch thread to wait and then enable button
	 * 
	 * @return
	 */
	private SwingWorker<Void, Void> createWorkerDelayedEnabledButton(final int delayInMSec) {
		return new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				try {
					Thread.sleep(delayInMSec);
				} catch (InterruptedException e) {
					myOut.println("Error Occurred.");
				}
				theButton.setEnabled(true);
				return null;
			}
		};
	}
	
	/**
	 * Launch thread to wait and then change the color of the square in the color panel
	 * 
	 * @return
	 */
	private SwingWorker<Void, Void> createWorkerDelayedColorChange(final int delayInMSec) {
		return new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				try {
					Thread.sleep(delayInMSec);
				} catch (InterruptedException e) {
					myOut.println("Error Occurred.");
				}
				colorPanel.setSqColor(Color.RED);
				colorPanel.repaint();
				return null;
			}
		};
	}
	
	class JPanelWithSquare extends JPanel {
		private static final long serialVersionUID = 1L;
		int height = 30;//30 pixels high.
	    int width = 30;//30 pixels wide.
	    Color sqColor = Color.BLUE;
	    
	    public Color getSqColor() {
	    	return sqColor;
	    }
	    
	    public void setSqColor(Color color) {
	    	sqColor = color;
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	    	super.paintComponent(g);
	    	int verticalCenter = this.getHeight()/2;
	    	int horizontalCenter = this.getWidth()/2;
	    	
	    	int topLeftSquareCornerY = verticalCenter - (height/2);
	    	int topLeftSquareCornerX = horizontalCenter - (width/2);
	    	
	    	g.setColor(sqColor);
	    	g.fillRect(topLeftSquareCornerX, topLeftSquareCornerY, width, height);
	    	
	      
	    }
	}

}
