package cz.muni.fi.diagram.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
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
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.view.ClassDiagram;
import cz.muni.fi.diagram.view.ClassObject;
import cz.muni.fi.diagram.visitors.ClassVisitor;

/**
 * Used only for debugging
 */
public class GetInfo extends AbstractHandler {

	private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";
	private ClassDiagram classDiagram = new ClassDiagram();
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		// Get all projects in the workspace
		IProject[] projects = root.getProjects();
		// Loop over all projects
		/* Loop through the project's resources and identify the classes
		   by checking their file extensions. */
		for (IProject project : projects) {
			try {
				if (project.isNatureEnabled(JDT_NATURE)) {
					System.out.print("Working in project " + project.getName());
					analyseClasses(project);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return null;
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
			CompilationUnit parse = parse(unit);
			ClassVisitor visitor = new ClassVisitor();
			parse.accept(visitor);

			ClassModel classModel = visitor.getClassModel();
			System.out.print("TU SOOOOOOOOOOM");
			System.out.print(classModel);
			
			ClassObject classObject = new ClassObject(classModel, 50, 100, 250, 250);
			classDiagram.addClass(classObject);
		}
	}

	/**
	 * Reads a ICompilationUnit and creates the AST DOM for manipulating the Java source file
	 * @param unit
	 * @return
	 */
	private CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}
}