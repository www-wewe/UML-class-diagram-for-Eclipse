package cz.muni.fi.diagram.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.graphics.Image;

import cz.muni.fi.diagram.view.ClassDiagramCanvas;

public class ExportAction extends Action {
	
	ClassDiagramCanvas classDiagramCanvas;
    
    public ExportAction(ClassDiagramCanvas classDiagramCanvas) {
        setText("Export");
        setToolTipText("Export diagram");
        this.classDiagramCanvas = classDiagramCanvas;
    }

    @Override
    public void run() {
    	Image image = classDiagramCanvas.getImage();
        System.out.print("Export Action");
    }
}
