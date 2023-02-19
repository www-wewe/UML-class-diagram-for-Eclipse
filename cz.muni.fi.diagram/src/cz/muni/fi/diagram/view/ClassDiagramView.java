package cz.muni.fi.diagram.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.plantuml.SourceStringReader;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import cz.muni.fi.diagram.imagecontrol.ImageControl;

public class ClassDiagramView extends ViewPart {
	private Button generateDiagramButton;
	private Canvas canvas;

	String classDiagramExample = "@startuml\n"
			+ "class Car {\n"
			+ "  +make : String\n"
			+ "  +model : String\n"
			+ "  +year : int\n"
			+ "  +drive() : void\n"
			+ "}\n"
			+ "class Person {\n"
			+ "  +name : String\n"
			+ "  +age : int\n"
			+ "  +email : String\n"
			+ "  +getAge() : int\n"
			+ "}\n"
			+ "class CarRental {\n"
			+ "  -cars : List<Car>\n"
			+ "  -renters : List<Person>\n"
			+ "  +addCar(car : Car) : void\n"
			+ "  +removeCar(car : Car) : void\n"
			+ "  +rentCar(car : Car, renter : Person) : void\n"
			+ "}\r\n"
			+ "CarRental -> Car\n"
			+ "CarRental -> Person\n"
			+ "@enduml\n";

	@Override
	public void createPartControl(Composite parent) {
		/*
		 * the PlantUML code for the class diagram is stored in the classDiagram string,
		 * and the SourceStringReader class is used to generate a PNG image of the diagram,
		 * which is then saved to a file.
		 * 
		 * it will load the image of the PlantUML diagram from the mydiagram.png
		 * file and draw it onto the gc of the event.
		 */

		Composite composite = new Composite(parent, SWT.NONE);
		ByteArrayOutputStream pngStream = new ByteArrayOutputStream();
		SourceStringReader reader = new SourceStringReader(classDiagramExample);
		try (FileOutputStream fos = new FileOutputStream("mydiagram.png")) {
			reader.generateImage(pngStream);
			fos.write(pngStream.toByteArray());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		composite.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				Image image = new Image(Display.getDefault(), "mydiagram.png");
				e.gc.drawImage(image, 0, 0);
				image.dispose();
			}
		});

		/*
		canvas = new ImageControl(parent);
		generateDiagramButton = new Button(parent, SWT.BORDER);
		generateDiagramButton.setText("Generate diagram");	

		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Image img = new Image(Display.getDefault(), canvas.getBounds());
				GC gc = new GC(img);
				//drawClassDiagram(gc);
				renderClassDiagram(gc, classDiagram);
				gc.dispose();
				e.gc.drawImage(img, 0, 0);
				img.dispose();
			}
		});*/
	}

	/**
	 * Draws class diagram from actually selected project
	 * @param gc
	 */
	private void drawClassDiagram(GC gc) {
		//IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
		IStructuredSelection structuredSelection = (IStructuredSelection) selectionService.getSelection();
		if (structuredSelection instanceof TreeSelection) {
			Object firstElement = structuredSelection.getFirstElement();
			if (firstElement instanceof IResource) {
				IResource selectedResource = (IResource) firstElement;
				IProject selectedProject = selectedResource.getProject();
				// všetké triedy from the selected project
				List<File> classFiles = new ArrayList<>();
				try {
					Files.walk(selectedProject.getLocation().toFile().toPath())
						.filter(path -> path.toString().endsWith(".java"))
						.forEach(path -> classFiles.add(path.toFile()));
					String classDiagram = generateClassDiagram(classFiles); //plantUML code
					renderClassDiagram(gc, classDiagram);
				} catch (IOException /*| CoreException*/ e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * In this implementation, the `generateClassDiagram` method creates a PlantUML diagram 
	 * based on the list of Java files in the selected project
	 * 
	 * @param classFiles
	 * @return PlantUML string
	 * @throws IOException
	 */
	private String generateClassDiagram(List<File> classFiles) {
		StringBuilder builder = new StringBuilder();
		for (File classFile : classFiles) {
			String className = classFile.getName().replace(".java", "");
			builder.append("class ").append(className).append(" {}\n"); 
			// in {} should be methods and attributes
		}
		// at the end of the file should be associations
		return "@startuml\n" + builder.toString() + "@enduml\n";
	}

	/**
	 * Method uses the Graphviz `dot` command to render the diagram to a GIF image 
	 * and display it in the Eclipse view.
	 * 
	 * @param gc
	 * @param classDiagram
	 */
	private void renderClassDiagram(GC gc, String classDiagram) {
		File dotFile = new File("diagram.dot");
		try {
			Files.write(dotFile.toPath(), classDiagram.getBytes());
			Process process = Runtime.getRuntime().exec("dot -Tgif diagram.dot");
			int exitCode = process.waitFor();
			if (exitCode == 0) {
				File imageFile = new File("diagram.gif");
				Image image = new Image(Display.getDefault(), imageFile.getAbsolutePath());
				gc.drawImage(image, 0, 0);
				image.dispose();
				imageFile.delete();
				dotFile.delete();
			} else {
				System.err.println("Error: dot command failed with exit code " + exitCode);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * The drawing logic:
	 * 	- Retrieve the current project by calling the IWorkspaceRoot.getProjects() method.
	 * 	- Loop through the project's resources and identify the classes 
	 * 	  by checking their file extensions.
	 * 	- Create a UML class diagram model by creating UMLClass objects 
	 *    and setting their properties.
	 * 	- Use a UML diagram drawing library, such as JHotDraw, 
	 * 	  to draw the class diagram on the Canvas.
	 */

	@Override
	public void setFocus() {
		canvas.setFocus();
		generateDiagramButton.setFocus();
	}
}
