package cz.muni.fi.diagram.view;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.visitors.ClassVisitor;

/*
 * The drawing logic:
 * 	- Retrieve the current project by calling the IWorkspaceRoot.getProjects() method.
 * 	- Loop through the project's resources and identify the classes.
 * 	- Create a UML class diagram model by creating UMLClass objects 
 *    and setting their properties.
 * 	- Draw the class diagram on the Canvas.
 */


public class ClassDiagramView extends ViewPart {
	
	/** Nature for java project */
	private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";
	private ClassDiagram classDiagram = new ClassDiagram();
	ClassDiagramCanvas classDiagramCanvas;
	private Button button;

    @Override
    public void createPartControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new FillLayout());
        createClassDiagram();
        classDiagramCanvas = new ClassDiagramCanvas(composite, classDiagram);
        classDiagramCanvas.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
			}

        });
        button = new Button(classDiagramCanvas, SWT.PUSH);
        button.setBounds(50, 10, 250, 50);
        button.setText("Generate diagram from workspace");
        button.addSelectionListener(new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			classDiagram = new ClassDiagram();
			createClassDiagram();
			classDiagramCanvas.setClassDiagram(classDiagram);
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// ignore
		}});

    }

	/**
	 * Draws class diagram from workspace
	 */
	private void createClassDiagram() {
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
			CompilationUnit parse = parse(unit);
			ClassVisitor visitor = new ClassVisitor();
			parse.accept(visitor);
			ClassModel classModel = visitor.getClassModel();
			// potrebujem funkciu na určenie polohy
			// šírka rovnaká, len výška iná v závislosti od počtu položiek v triede?
			ClassObject classObject =  new ClassObject(classModel, 50, 100, 250, 250);
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
		return (CompilationUnit) parser.createAST(null);
	}

	@Override
	public void setFocus() {
		//...
	}
}
