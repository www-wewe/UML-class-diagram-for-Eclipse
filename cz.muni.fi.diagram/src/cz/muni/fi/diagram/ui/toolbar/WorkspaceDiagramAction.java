/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.toolbar;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.Action;

import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.parser.ClassModelParser;
import cz.muni.fi.diagram.ui.view.ClassDiagram;
import cz.muni.fi.diagram.ui.view.ClassDiagramCanvas;

/**
 * Creates class diagram from workspace.
 * 
 * @author Veronika Lenková
 *
 */
public class WorkspaceDiagramAction extends Action {
	
	ClassDiagramCanvas classDiagramCanvas;
	ClassDiagram classDiagram;
	/** Nature for java project */
	private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";
    
    public WorkspaceDiagramAction(ClassDiagramCanvas classDiagramCanvas) {
        setText("Workspace");
        setToolTipText("Generate class diagram from workspace");
        this.classDiagramCanvas = classDiagramCanvas;
    }

	
	@Override
	   public void run() {
		    classDiagram = new ClassDiagram();
			createClassDiagramFromWorkspace();
			classDiagramCanvas.setClassDiagram(classDiagram);
	   }

	private void createClassDiagramFromWorkspace() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		// Get all projects in the workspace
		IProject[] projects = root.getProjects();
		/* Loop through the project's resources and identify the classes
		   by checking their nature. */
		for (IProject project : projects) {
			try {
				if (project.isNatureEnabled(JDT_NATURE)) {
					analyseClasses(project);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private void analyseClasses(IProject project) throws JavaModelException {
		IPackageFragment[] packages = JavaCore.create(project).getPackageFragments();
		for (IPackageFragment mypackage : packages) {
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				createAST(mypackage);
			}

		}
	}

	private void createAST(IPackageFragment mypackage) throws JavaModelException {
		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
			// now create the AST for the ICompilationUnits
			ClassModel classModel = ClassModelParser.createClassModel(unit);
			classDiagram.addClass(classModel);
		}
	}

}
