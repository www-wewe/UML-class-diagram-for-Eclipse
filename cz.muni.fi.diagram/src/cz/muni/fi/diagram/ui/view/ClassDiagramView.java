/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
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
import cz.muni.fi.diagram.ui.toolbar.ExportAction;
import cz.muni.fi.diagram.ui.toolbar.LegendAction;
import cz.muni.fi.diagram.ui.toolbar.ManageDiagramAction;
import cz.muni.fi.diagram.ui.toolbar.ZoomInDiagramAction;
import cz.muni.fi.diagram.ui.toolbar.ZoomOutDiagramAction;

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

	/** Minimal width of view area */
	public static final int MIN_WIDTH = 400;
	/** Minimal height of view area */
	public static final int MIN_HEIGHT = 650;
	/** Class diagram **/
	private ClassDiagram classDiagram = new ClassDiagram();
	/** Canvas with class diagram **/
	private ClassDiagramCanvas classDiagramCanvas;
	/** Toolbar actions whose visibility is changing according to content of class diagram canvas */
	private List<IAction> toolbarActions = new ArrayList<>();

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

        scrolledComposite.setMinSize(MIN_WIDTH, MIN_HEIGHT);
        scrolledComposite.setSize(MIN_WIDTH, MIN_HEIGHT);
        mainComposite.setSize(MIN_WIDTH, MIN_HEIGHT);
        classDiagramCanvas.setSize(MIN_WIDTH, MIN_HEIGHT);

        DropTarget dropTarget = new DropTarget(classDiagramCanvas, DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_DEFAULT);
        dropTarget.setTransfer(LocalSelectionTransfer.getTransfer()); 
        dropTarget.addDropListener(new DiagramDropListener(classDiagramCanvas));

        createToolbar(parent);
    }

    /**
     * Creates the toolbar of Class diagram view.
     * @param parent composite
     */
	private void createToolbar(Composite parent) {
		ZoomInDiagramAction zoomIn = new ZoomInDiagramAction(classDiagramCanvas);
		ZoomOutDiagramAction zoomOut = new ZoomOutDiagramAction(classDiagramCanvas);
		ExportAction export = new ExportAction(parent.getShell(), classDiagramCanvas);
		ManageDiagramAction manageDiagram = new ManageDiagramAction(parent.getShell(), classDiagramCanvas);
		LegendAction legend = new LegendAction(parent.getShell());

		IContributionManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
		toolBarManager.add(zoomIn);
		toolBarManager.add(zoomOut);
		toolBarManager.add(export);
		toolBarManager.add(manageDiagram);
		toolBarManager.add(legend);

		toolbarActions.add(zoomIn);
		toolbarActions.add(zoomOut);
		toolbarActions.add(export);
    	classDiagramCanvas.addToolbarActions(toolbarActions);
	}

	@Override
	public void setFocus() {
		classDiagramCanvas.setFocus();
	}
}
