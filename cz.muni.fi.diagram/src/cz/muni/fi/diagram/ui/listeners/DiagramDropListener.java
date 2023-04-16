/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.listeners;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;

import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.ui.view.ClassDiagramCanvas;

/**
 * After detecting dropped class in ClassDiagramCanvas,
 * ClassModel is created and added to ClassDiagram.
 * 
 * @author Veronika Lenková
 */
public class DiagramDropListener implements DropTargetListener {

	/** Canvas with class diagram */
	private ClassDiagramCanvas classDiagramCanvas;

	/**
	 * @param classDiagramCanvas where the class is dropped
	 */
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
			    assert compilationUnit != null; // delete assert ?
		        ClassModel classModel = ClassModel.create(compilationUnit);
		        classDiagramCanvas.addClass(classModel);

		        // add subclasses
			    IType type = compilationUnit.findPrimaryType();
			    if (type != null) {
			    	ITypeHierarchy hierarchy;
					try {
						hierarchy = type.newTypeHierarchy(null);
						IType[] subclasses = hierarchy.getSubclasses(type);
						for (IType subclass : subclasses) {
							ICompilationUnit subclassCompilationUnit = subclass.getCompilationUnit();
							ClassModel subClassModel = ClassModel.create(subclassCompilationUnit);
							classDiagramCanvas.addClass(subClassModel);
						}
					} catch (JavaModelException e) {
						e.printStackTrace();
					}
			    }
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
