package cz.muni.fi.diagram.listeners;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;

import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.view.ClassDiagramCanvas;
import cz.muni.fi.diagram.visitors.ClassModelFactory;

/**
 * OR nested classDiagramView class? TODO
 * 
 * @author nxf92568
 */
public class DiagramDropListener implements DropTargetListener {
	
	ClassDiagramCanvas classDiagramCanvas;
	
	public DiagramDropListener(ClassDiagramCanvas classDiagramCanvas) {
		this.classDiagramCanvas = classDiagramCanvas;
	}

	@Override
	public void drop(DropTargetEvent event) {
		if (event.data instanceof IStructuredSelection) {
		    IStructuredSelection selection = (IStructuredSelection) event.data;
		    ICompilationUnit compilationUnit = null;
		    Object firstElement = selection.getFirstElement();
		    if (firstElement instanceof ICompilationUnit) {
		        // The dropped object is an ICompilationUnit
		        compilationUnit = (ICompilationUnit) firstElement;
			    assert compilationUnit != null; // delete assert
		        ClassModel classModel = ClassModelFactory.createClassModel(compilationUnit);
		        classDiagramCanvas.addClass(classModel);
		    }
		}
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		// Empty
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
		// Empty
	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {
		// Empty
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		// Empty
	}

	@Override
	public void dropAccept(DropTargetEvent event) {
		// Empty
	}

}
