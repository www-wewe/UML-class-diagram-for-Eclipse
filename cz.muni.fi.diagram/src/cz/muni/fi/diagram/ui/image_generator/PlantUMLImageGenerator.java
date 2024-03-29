/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.image_generator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.model.ClassType;
import cz.muni.fi.diagram.model.FieldModel;
import cz.muni.fi.diagram.model.MethodModel;
import cz.muni.fi.diagram.model.Visibility;
import cz.muni.fi.diagram.ui.view.ClassDiagram;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

/**
 * Class which reads Class Diagram data and generate Image using PlantUML.
 * @author Veronika Lenková
 *
 */
public final class PlantUMLImageGenerator implements IClassDiagramImageGenerator {

	/** Class diagram **/
	private ClassDiagram classDiagram;
	/** Newline character */
	private static final String NEWLINE = "\n";
	/** Constant indicating start of PlantUML code */
	private static final String START_PLANT_UML = "@startuml" + NEWLINE;
	/** Constant indicating end of PlantUML code */
	private static final String END_PLANT_UML = "@enduml" + NEWLINE;
	/** Zoom scale */
	private double scale = 1.0;
	/** PlantUML source code from which is generated image */
	private String diagramPlantUMLSource;

	public PlantUMLImageGenerator() {
		// Intentionally empty
	}

	@Override
	public Image getImage(ClassDiagram classDiagram) {
		this.classDiagram = classDiagram;
		return generateImage(classDiagram);
	}

	@Override
	public Image getImage() {
		return generateImageFromSourceString(diagramPlantUMLSource);
	}

	/**
	 * Generates image from class diagram.
	 * New PlantUML source code is generated.
	 * The developer is responsible for disposing the generated image.
	 * 
	 * @param classDiagram from which is generated image
	 * @return generated Image
	 */
	public Image generateImage(ClassDiagram classDiagram) {
		setClassDiagram(classDiagram);
		String newDiagramSource = null;
		try {
			newDiagramSource = getPlantUMLSource(classDiagram);
		} catch (OutOfMemoryError e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", "This amount of data cannot be processed. Try adding fewer classes.");
			classDiagram.clear();
			return null;
		}
		diagramPlantUMLSource = newDiagramSource;
	    return generateImageFromSourceString(diagramPlantUMLSource);		
	}

	/**
	 * Generates image from {@code plantUMLSource} code.
	 * The developer is responsible for disposing the generated image.
	 * 
	 * @param plantUMLSource from which is generated image
	 * @return generated Image
	 */
	@SuppressWarnings("deprecation")
	public Image generateImageFromSourceString(String plantUMLSource) {
		diagramPlantUMLSource = plantUMLSource;
		SourceStringReader reader = new SourceStringReader(plantUMLSource);
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    try {
			reader.generateImage(out, new FileFormatOption(FileFormat.PNG));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return new Image(Display.getDefault(), new ByteArrayInputStream(out.toByteArray()));
	}

    /**
     * Gets PlantUML source code which will be used for generating image from it.
     * See https://plantuml.com/class-diagram for more about PlantUML class diagram syntax.
     * 
     * @param classDiagram from which is PlantUML text generated
     * @return PlantUML text
     */
	private String getPlantUMLSource(ClassDiagram classDiagram) {
		StringBuilder source = new StringBuilder(START_PLANT_UML);
		source.append("scale " + scale + NEWLINE);
		StringBuilder relationshipsString = new StringBuilder();
		for (ClassModel classModel : classDiagram.getClasses()) {
			addClassToPlantUML(source, relationshipsString, classModel);
		}
		source.append(relationshipsString);
		source.append(END_PLANT_UML);
		return source.toString();
	}

	/**
	 * Adds class model to {@code source} and relationships in this class to {@code relationshipsString}.
	 * 
	 * @param source PlantUML code
	 * @param relationshipsString string of relationships in the whole class diagram
	 * @param classModel to be added to PlantUML source
	 */
	private void addClassToPlantUML(StringBuilder source, StringBuilder relationshipsString, ClassModel classModel) {
		if (!shouldAddClass(classModel.getType())) return;

		boolean showPackage = !classDiagram.isHidePackage();
		if (showPackage) {
			source.append("package ").append(classModel.getPackageName()).append(" {").append(NEWLINE);
		}

		StringBuilder oneClass =  new StringBuilder();
		oneClass.append(classModel.getType().character).append(" ");
		oneClass.append(classModel.getName()).append(" {").append(NEWLINE);

		if (!classDiagram.isHideFields()) {
			for (FieldModel fieldModel : classModel.getFields() ) {
				if (shouldAddElement(fieldModel.getVisibility())) {
					oneClass.append("{field}").append(fieldModel.getPlantUMLString()).append(NEWLINE);
				}
			}
		}

		if (!classDiagram.isHideMethods()) {
			for (MethodModel methodModel : classModel.getMethods() ) {
				if (shouldAddElement(methodModel.getVisibility())) {
					oneClass.append("{method}").append(methodModel.getPlantUMLString()).append(NEWLINE);
				}
			}
		}

		addRelationships(source, relationshipsString, classModel);
		oneClass.append("}").append(NEWLINE);
		if (showPackage) {
			source.append(oneClass).append("}").append(NEWLINE);
		} else {
			source.append(oneClass);
		}
	}

	/**
	 * @param type of class
	 * @return true if class can be added, false otherwise
	 */
	private boolean shouldAddClass(ClassType type) {
	    switch (type) {
	        case INTERFACE: return !classDiagram.isHideInterface();
	        case ENUM: return !classDiagram.isHideEnum();
	        default: return true;
	    }
	}

	/**
	 * @param visibility of element (method or field)
	 * @return true if element can be added, false otherwise
	 */
	private boolean shouldAddElement(Visibility visibility) {
	    return !(classDiagram.isHidePrivate() && visibility == Visibility.PRIVATE) && !(classDiagram.isHidePublic() && visibility == Visibility.PUBLIC);
	}

	/**
	 * Adds relationships of class model to the source of PlantUML code.
	 * @param source PlantUML code
	 * @param relationshipsString - string with relationships
	 * @param classModel one class
	 */
	private void addRelationships(StringBuilder source, StringBuilder relationshipsString, ClassModel classModel) {
		if (!classDiagram.isHideParent()) {
			String parent = classModel.getParentName();
			if (parent != null) {
				relationshipsString.append(classModel.getName()).append(" --|> ").append(parent).append(NEWLINE);
			}
		}
		if (!classDiagram.isHideInterface()) {
			for (String interfaceClass : classModel.getInterfaces()) {
				source.append("interface " + interfaceClass + NEWLINE);
				relationshipsString.append(classModel.getName()).append(" ..|> ").append(interfaceClass).append(NEWLINE);
			}
		}
		if (!classDiagram.isHideNestedClasses()) {
			for (ClassModel nestedClass : classModel.getNestedClasses()) {
				relationshipsString.append(classModel.getName()).append(" +-- ").append(nestedClass.getName()).append(NEWLINE);
				addClassToPlantUML(source, relationshipsString, nestedClass);
			}
		}
	}

	public ClassDiagram getClassDiagram() {
		return classDiagram;
	}

	public void setClassDiagram(ClassDiagram classDiagram) {
		this.classDiagram = classDiagram;
	}

	public String getDiagramPlantUMLSource() {
		return diagramPlantUMLSource;
	}

	public void setDiagramPlantUMLSource(String diagramPlantUMLSource) {
		this.diagramPlantUMLSource = diagramPlantUMLSource;
	}

	public double getScale() {
		return scale;
	}

	@Override
	public void setScale(double scale) {
		this.scale = scale;
	}
}
