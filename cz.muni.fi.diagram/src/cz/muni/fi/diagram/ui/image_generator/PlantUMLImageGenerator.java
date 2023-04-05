/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.image_generator;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.model.FieldModel;
import cz.muni.fi.diagram.model.MethodModel;
import cz.muni.fi.diagram.ui.view.ClassDiagram;
import net.sourceforge.plantuml.SourceStringReader;

/**
 * Class which reads Class Diagram data and generate Image using PlantUML.
 * @author Veronika Lenková
 *
 */
public final class PlantUMLImageGenerator implements IClassDiagramImageGenerator {

	public PlantUMLImageGenerator() {
		// Intentionally empty
	}

	@Override
	public Image getImage(ClassDiagram classDiagram) {
		return generateImage(classDiagram);
	}

	/**
	 * @param classDiagram
	 * @return generated Image
	 */
	public static Image generateImage(ClassDiagram classDiagram) {
		String diagramPlantUMLSource = getPlantUMLSource(classDiagram);
		ByteArrayOutputStream pngStream = new ByteArrayOutputStream();
		SourceStringReader reader = new SourceStringReader(diagramPlantUMLSource);
		try (FileOutputStream fos = new FileOutputStream("mydiagram.png")) {
			reader.generateImage(pngStream);
			fos.write(pngStream.toByteArray());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new Image(Display.getDefault(), "mydiagram.png");
	}

    /**
     * Gets PlantUML source code which will be used for generating image from it.
     * See {@link https://plantuml.com/class-diagram} for more about PlantUML class diagram syntax.
     * 
     * @return PlantUML text
     */
	private static String getPlantUMLSource(ClassDiagram classDiagram) {
		StringBuilder source = new StringBuilder("@startuml\n");
		StringBuilder relationshipsString = new StringBuilder();
		for (ClassModel classModel : classDiagram.getClasses()) {
			StringBuilder oneClass =  new StringBuilder();
			oneClass.append(classModel.getType().character).append(" ");
			oneClass.append(classModel.getName()).append(" {\n");
			for (FieldModel fieldModel : classModel.getFields() ) {
				oneClass.append("{field}").append(fieldModel).append("\n");
			}
			for (MethodModel methodModel : classModel.getMethods() ) {
				oneClass.append("{method}").append(methodModel).append("\n");
			}
			// Relationships
			String superClass = classModel.getSuperClassName();
			if (superClass != null) {
				relationshipsString.append(classModel.getName()).append(" --|> ").append(superClass).append("\n");
			}
			for (String interfaceClass : classModel.getInterfaces()) {
				relationshipsString.append(classModel.getName()).append(" ..|> ").append(interfaceClass).append("\n");
			}
			for (String subclass : classModel.getSubclasses()) {
				relationshipsString.append(classModel.getName()).append(" --|> ").append(subclass).append("\n");
			}
			oneClass.append("}\r\n");
			source.append(oneClass);
		}
		source.append(relationshipsString);
		source.append("@enduml\n");
		//System.out.print(source);
		return source.toString();
	}

}
