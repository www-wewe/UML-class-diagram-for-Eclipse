/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.toolbar;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import cz.muni.fi.diagram.ui.view.ClassDiagramCanvas;

/**
 * Action for zooming the image of class diagram out.
 * 
 * @author Veronika Lenková
 */
public class ZoomOutDiagramAction extends Action {

	/** Canvas with class diagram */
	private ClassDiagramCanvas canvas;

	/**
	 * Constructor.
	 * @param canvas with class diagram
	 */
	public ZoomOutDiagramAction(ClassDiagramCanvas canvas) {
		super("Zoom out");
		this.canvas = canvas;
		setToolTipText("Zoom out the class diagram");
		try {
			setImageDescriptor(ImageDescriptor.createFromURL(
					new URL("platform:/plugin/cz.muni.fi.diagram/icons/zoomOut.png")));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		setEnabled(false);
	}

	@Override
	public void run() {
		canvas.zoomOut();
	}

}

