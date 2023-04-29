/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.listeners;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
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
		    Object firstElement = selection.getFirstElement();
		    if (firstElement instanceof ICompilationUnit) {
		        // The dropped object is an CompilationUnit
		    	ICompilationUnit compilationUnit = (ICompilationUnit) firstElement;
			    createClassDiagram(compilationUnit);
		    } else if (firstElement instanceof IPackageFragment) {
		    	// The dropped object is a Package
		    	IPackageFragment mypackage = (IPackageFragment) firstElement;
		    	try {
					for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
						createClassDiagram(unit);
					}
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
		    } else if (firstElement instanceof IJavaProject) {
		    	// The dropped object is an JavaProject
		    	IJavaProject project = (IJavaProject) firstElement;
		    	IPackageFragment[] packages;
				try {
					packages = project.getPackageFragments();
					for (IPackageFragment mypackage : packages) {
						for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
							createClassDiagram(unit);
						}
					}
				} catch (JavaModelException e) {
					e.printStackTrace();
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

	/**
	 * Creates class diagram by creating class models of compilation unit and its subclasses.
	 * 
	 * @param compilationUnit from which is diagram created
	 */
	private void createClassDiagram(ICompilationUnit compilationUnit) {
		assert compilationUnit != null; // delete assert ?
		ClassModel classModel = ClassModel.create(compilationUnit);
		classDiagramCanvas.addClass(classModel);

		if (classDiagramCanvas.getClassDiagram().isHideChildren()) {
			return;
		}
		// add subclasses
		if (classDiagramCanvas.getClassDiagram().isHideChildren()) {
			return;
		}
		IType type = compilationUnit.findPrimaryType();
		if (type != null) {
			ITypeHierarchy hierarchy;
			try {
				hierarchy = type.newTypeHierarchy(null);
				IType[] subclasses = hierarchy.getSubtypes(type);
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
