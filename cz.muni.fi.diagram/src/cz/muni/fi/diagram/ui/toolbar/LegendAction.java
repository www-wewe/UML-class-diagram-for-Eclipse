/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.toolbar;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * Action for showing the legend of class diagram.
 * 
 * @author Veronika Lenková
 */
public class LegendAction extends Action {

	/** Main window */
	private Shell shell;

	/**
	 * Constructor.
	 * @param shell - main window
	 */
	public LegendAction(Shell shell) {
		this.shell = shell;
		setToolTipText("Show legend");
		try {
			setImageDescriptor(ImageDescriptor.createFromURL(
					new URL("platform:/plugin/cz.muni.fi.diagram/icons/legend.png")));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			Image legend = ImageDescriptor.createFromURL(
					new URL("platform:/plugin/cz.muni.fi.diagram/icons/legendImage.png")).createImage();
			LegendDialog legendDialog = new LegendDialog(shell, legend);
			legendDialog.open();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
