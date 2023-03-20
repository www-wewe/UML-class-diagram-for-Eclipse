package cz.muni.fi.diagram.view;

import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import cz.muni.fi.diagram.actions.EraseDiagramAction;
import cz.muni.fi.diagram.actions.ExportAction;
import cz.muni.fi.diagram.actions.WorkspaceDiagramAction;
import cz.muni.fi.diagram.listeners.DiagramDropListener;

/**
 * View with canvas where class diagram is displayed.
 * Contains ToolBar where actions as "Export", "Erase class diagram"
 * and "Create class diagram from workspace" are located.
 * 
 * Class diagram is created after you drop class from workspace to the view.
 * 
 * @author Veronika Lenková
 *
 */
public class ClassDiagramView extends ViewPart {

	/** Class diagram **/
	private ClassDiagram classDiagram = new ClassDiagram();
	/** Canvas with class diagram **/
	private ClassDiagramCanvas classDiagramCanvas;
	Composite parent;
	

    @Override
    public void createPartControl(Composite parent) {
    	this.parent = parent;
    	classDiagramCanvas = new ClassDiagramCanvas(parent, classDiagram);
    	// TODO make it scrollable

        DropTarget dropTarget = new DropTarget(classDiagramCanvas, DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_DEFAULT);
        dropTarget.setTransfer(LocalSelectionTransfer.getTransfer()); 
        dropTarget.addDropListener(new DiagramDropListener(classDiagramCanvas));

        IContributionManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
        // TODO add icons
    	toolBarManager.add(new ExportAction(classDiagramCanvas));
    	toolBarManager.add(new EraseDiagramAction(classDiagramCanvas));
    	toolBarManager.add(new WorkspaceDiagramAction(classDiagramCanvas));
    	addLabel();
    }

    /**
     * TODO Delete ?
     * make it prettier
     */
	private void addLabel() {
		Color whiteColor = new Color(parent.getDisplay(), 255, 0, 255);
        Label label = new Label(classDiagramCanvas, SWT.NONE);
        label.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, true, 4, 1));
        GC gc = new GC(label);
        gc.setBackground(whiteColor);
        gc.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
        label.setSize(250, 20);
        label.setText("Drop a Java class to generate a class diagram.");
	}

	@Override
	public void setFocus() {
		classDiagramCanvas.setFocus();
	}

}
