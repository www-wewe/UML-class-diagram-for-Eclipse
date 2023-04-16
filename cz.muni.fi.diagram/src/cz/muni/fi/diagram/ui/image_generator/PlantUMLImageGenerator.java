/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.image_generator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.model.FieldModel;
import cz.muni.fi.diagram.model.MethodModel;
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

	private ClassDiagram classDiagram;
	/** Constant indicating start of PlantUML code */
	private static final String START_PLANT_UML = "@startuml\n";
	/** Constant indicating end of PlantUML code */
	private static final String END_PLANT_UML = "@enduml\n";
	/** Newline character */
	private static final String NEWLINE = "\n";
	private static final String SPACE = " ";
	private static final String SCALE = "scale";
	private static final String WIDTH = "width";
	private static final String HEIGHT = "height";
	private static final String HIDE_FIELDS = "hide fields";
	private static final String HIDE_METHODS = "hide methods";
	private static final String HIDE_INTERFACE = "hide interface";
	private static final String HIDE_ENUM = "hide enum";

	public PlantUMLImageGenerator() {
		// Intentionally empty
	}

	@Override
	public Image getImage(ClassDiagram classDiagram) {
		this.classDiagram = classDiagram;
		return generateImage(classDiagram);
	}

	/**
	 * @param classDiagram
	 * @return generated Image
	 */
	@SuppressWarnings("deprecation")
	public Image generateImage(ClassDiagram classDiagram) {
		String diagramPlantUMLSource = getPlantUMLSource(classDiagram);
	    SourceStringReader reader = new SourceStringReader(diagramPlantUMLSource);
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
     * See {@link https://plantuml.com/class-diagram} for more about PlantUML class diagram syntax.
     * 
     * @return PlantUML text
     */
	private String getPlantUMLSource(ClassDiagram classDiagram) {
		boolean showPackage = !classDiagram.isHidePackage();
		StringBuilder source = new StringBuilder(START_PLANT_UML);
//		source.append("scale 5000 width" + NEWLINE);
		StringBuilder relationshipsString = new StringBuilder();
		for (ClassModel classModel : classDiagram.getClasses()) {
			if (showPackage) {
				source.append("package " + classModel.getPackageName() + " {" + NEWLINE);
			}
			addClassToPlantUML(source, relationshipsString, classModel);
			for (ClassModel nestedClass : classModel.getNestedClasses()) {
				addClassToPlantUML(source, relationshipsString, nestedClass);
			}
			if (showPackage) {				
				source.append("}" + NEWLINE);
			}
		}
		source.append(relationshipsString);
		setSettings(source);
		source.append(END_PLANT_UML);
		System.out.print(source);
		return source.toString();
	}

	private void setSettings(StringBuilder source) {
		if (classDiagram.isHideFields()) {
			source.append(HIDE_FIELDS + NEWLINE);
		}
		if (classDiagram.isHideMethods()) {
			source.append(HIDE_METHODS + NEWLINE);
		}
		if (classDiagram.isHideInterface()) {
			source.append(HIDE_INTERFACE + NEWLINE);
		}
		if (classDiagram.isHideEnum()) {
			source.append(HIDE_ENUM + NEWLINE);
		}
		int scaleWidth = classDiagram.getScaleWidth();
		if (scaleWidth != -1) {
			source.append(SCALE + SPACE + scaleWidth + SPACE + WIDTH + NEWLINE);
		}
		int scaleHeight = classDiagram.getScaleHeight();
		if (scaleHeight != -1) {
			source.append(SCALE + SPACE + scaleHeight + SPACE + HEIGHT + NEWLINE);
		}
	}

	/**
	 * Adds class model to {@code source} and relationships in this class to {@code relationshipsString}.
	 * 
	 * @param source PlantUML code
	 * @param relationshipsString string of relationships in the whole class diagram
	 * @param classModel to be added to PlantUML source
	 */
	private static void addClassToPlantUML(StringBuilder source, StringBuilder relationshipsString, ClassModel classModel) {
		StringBuilder oneClass =  new StringBuilder();
		oneClass.append(classModel.getType().character).append(" ");
		oneClass.append(classModel.getName()).append(" {" + NEWLINE);
		for (FieldModel fieldModel : classModel.getFields() ) {
			oneClass.append("{field}").append(fieldModel.toPlantUMLString()).append(NEWLINE);
		}
		for (MethodModel methodModel : classModel.getMethods() ) {
			oneClass.append("{method}").append(methodModel.toPlantUMLString()).append(NEWLINE);
		}
		// Relationships
		String superClass = classModel.getSuperClassName();
		if (superClass != null) {
			relationshipsString.append(classModel.getName()).append(" --|> ").append(superClass).append(NEWLINE);
		}
		for (String interfaceClass : classModel.getInterfaces()) {
			source.append("interface " + interfaceClass + NEWLINE);
			relationshipsString.append(classModel.getName()).append(" ..|> ").append(interfaceClass).append(NEWLINE);
		}
		for (ClassModel nestedClass : classModel.getNestedClasses()) {
			relationshipsString.append(classModel.getName()).append(" +-- ").append(nestedClass.getName()).append(NEWLINE);
		}
		oneClass.append("}" + NEWLINE);
		source.append(oneClass);
	}

}
