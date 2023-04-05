/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.toolbar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import cz.muni.fi.diagram.ui.view.ClassDiagram;
import cz.muni.fi.diagram.ui.view.ClassDiagramCanvas;

/**
 * Action for erasing the whole class diagram.
 * 
 * @author Veronika Lenková
 */
public class EraseDiagramAction extends Action {
	
	ClassDiagramCanvas canvas;
	
	public EraseDiagramAction(ClassDiagramCanvas canvas) {
        super("Erase");
        this.canvas = canvas;
        setToolTipText("Erase diagram");
	}

	@Override
    public void run() {
		canvas.clear();
    }

}
