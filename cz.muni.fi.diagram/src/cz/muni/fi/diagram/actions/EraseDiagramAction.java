package cz.muni.fi.diagram.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import cz.muni.fi.diagram.view.ClassDiagram;
import cz.muni.fi.diagram.view.ClassDiagramCanvas;

public class EraseDiagramAction extends Action {
	
	ClassDiagramCanvas canvas;
	
	public EraseDiagramAction(ClassDiagramCanvas canvas) {
        super("Erase");
        this.canvas = canvas;
        setToolTipText("Erase diagram");
	}

	@Override
    public void run() {
		canvas.getClassDiagram().clear();
    }

}
