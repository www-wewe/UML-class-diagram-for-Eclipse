/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.toolbar;

import org.eclipse.jface.action.Action;
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
	}

	@Override
    public void run() {
		ManageDiagramDialog manageDiagramDialog = new ManageDiagramDialog(shell, canvas);
		manageDiagramDialog.open();
    }

}
