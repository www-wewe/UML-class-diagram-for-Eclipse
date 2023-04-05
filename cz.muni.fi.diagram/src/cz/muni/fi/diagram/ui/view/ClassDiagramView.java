/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.view;

import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import cz.muni.fi.diagram.ui.listeners.DiagramDropListener;
import cz.muni.fi.diagram.ui.toolbar.EraseDiagramAction;
import cz.muni.fi.diagram.ui.toolbar.ExportAction;
import cz.muni.fi.diagram.ui.toolbar.WorkspaceDiagramAction;

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
    	// set the size of the scrolled content - method 1
        final ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        final Composite mainComposite = new Composite(scrolledComposite, SWT.NONE);
        scrolledComposite.setContent(mainComposite);
        GridLayout layout = new GridLayout();
        mainComposite.setLayout(layout);
        Label label = new Label (mainComposite, SWT.NONE);
        label.setText("Drop a Java class to generate a class diagram.");
 
        classDiagramCanvas = new ClassDiagramCanvas(mainComposite, classDiagram);
        scrolledComposite.setMinSize(400, 750);
        scrolledComposite.setSize(400, 750);
        mainComposite.setSize(400, 750);
        classDiagramCanvas.setSize(400, 750);

        DropTarget dropTarget = new DropTarget(classDiagramCanvas, DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_DEFAULT);
        dropTarget.setTransfer(LocalSelectionTransfer.getTransfer()); 
        dropTarget.addDropListener(new DiagramDropListener(classDiagramCanvas));


        IContributionManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
        // TODO add icons
    	toolBarManager.add(new ExportAction(parent.getShell(), classDiagramCanvas));
    	toolBarManager.add(new EraseDiagramAction(classDiagramCanvas));
    	toolBarManager.add(new WorkspaceDiagramAction(classDiagramCanvas));
    }

	@Override
	public void setFocus() {
		classDiagramCanvas.setFocus();
	}

}
