/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.toolbar;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;

import cz.muni.fi.diagram.ui.view.ClassDiagramCanvas;

/**
 * Action for manage classes of the class diagram.
 * 
 * @author Veronika Lenková
 */
public class ManageDiagramAction extends Action {
	/** Main window */
	private Shell shell;
	/** Canvas with class diagram */
	private ClassDiagramCanvas canvas;

	/**
	 * Constructor.
	 * @param shell main window
	 * @param canvas with class diagram
	 */
	public ManageDiagramAction(Shell shell, ClassDiagramCanvas canvas) {
        super("Manage");
        this.canvas = canvas;
        this.shell = shell;
        setToolTipText("Manage diagram");
        try {
			setImageDescriptor(ImageDescriptor.createFromURL(
					new URL("platform:/plugin/cz.muni.fi.diagram/icons/manageDialog.png")));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
    public void run() {
		ManageDiagramDialog manageDiagramDialog = new ManageDiagramDialog(shell, canvas);
		manageDiagramDialog.open();
    }

}
