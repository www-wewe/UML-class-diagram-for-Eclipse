/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.view;

import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import cz.muni.fi.diagram.ui.listeners.DiagramDropListener;
import cz.muni.fi.diagram.ui.toolbar.EraseDiagramAction;
import cz.muni.fi.diagram.ui.toolbar.ExportAction;
import cz.muni.fi.diagram.ui.toolbar.ManageDiagramAction;
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

    @Override
    public void createPartControl(Composite parent) {
        final ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        final Composite mainComposite = new Composite(scrolledComposite, SWT.NONE);
        scrolledComposite.setContent(mainComposite);
        GridLayout layout = new GridLayout();
        mainComposite.setLayout(layout);

        classDiagramCanvas = new ClassDiagramCanvas(mainComposite, classDiagram);
        GridData canvasGridData = new GridData();
        classDiagramCanvas.setLayoutData(canvasGridData);
        canvasGridData.exclude = true;

        System.out.print("TUUUU: ");
        System.out.print(parent.getDisplay().getClientArea().height);
        System.out.print(parent.getDisplay().getClientArea().width);
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
    	toolBarManager.add(new WorkspaceDiagramAction(classDiagramCanvas));
    	toolBarManager.add(new ManageDiagramAction(parent.getShell(), classDiagramCanvas));
    	// toolBarManager.add(new EraseDiagramAction(classDiagramCanvas));
    }

	@Override
	public void setFocus() {
		classDiagramCanvas.setFocus();
	}

}
