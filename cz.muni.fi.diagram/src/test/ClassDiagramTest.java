/** Copyright (c) 2023, Veronika Lenková */
package test;
import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.junit.jupiter.api.Test;

import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.model.ClassType;
import cz.muni.fi.diagram.model.FieldModel;
import cz.muni.fi.diagram.model.MethodModel;
import cz.muni.fi.diagram.model.Visibility;
import cz.muni.fi.diagram.ui.image_generator.PlantUMLImageGenerator;
import cz.muni.fi.diagram.ui.view.ClassDiagram;

/**
 * ClassDiagram basic tests.
 * 
 * @author Veronika Lenková
 */
public class ClassDiagramTest {

	/** Image generator which can generate image from class diagram */
	private PlantUMLImageGenerator imageGenerator = new PlantUMLImageGenerator();

	/**
	 * Test for generating Image from empty ClassDiagram.
	 */
	@Test
	void testGenerateImageFromClassDiagramWithEmptyClass() {
		ClassDiagram classDiagram = new ClassDiagram();
		ClassModel dogModel = new ClassModel();
		dogModel.setName("Dog");
		dogModel.setType(ClassType.CLASS);
		classDiagram.addClass(dogModel);
		
		Image classDiagramImage = imageGenerator.generateImage(classDiagram);
		Image classDiagramImageWithEmptyClassTemplate = null;
		classDiagramImageWithEmptyClassTemplate = ImageDescriptor.createFromFile(getClass(),"../../testsData/classDiagramImageWithEmptyClassTemplate.png")
				.createImage();		
		assertEquals(classDiagramImage.getImageData().alphaData, classDiagramImageWithEmptyClassTemplate.getImageData().alphaData);

		classDiagramImageWithEmptyClassTemplate.dispose();
		classDiagramImage.dispose();
	}

	/**
	 * Test for generating Image from ClassDiagram with class.
	 */
	@Test
	void testGenerateImageFromClassDiagram() {
		ClassDiagram classDiagram = new ClassDiagram();
		ClassModel dogModel = new ClassModel();
		fillClassModel(dogModel);
		classDiagram.addClass(dogModel);

		Image classDiagramImage = imageGenerator.generateImage(classDiagram);
		Image classDiagramImageTemplate = ImageDescriptor.createFromFile(getClass(),"../../testsData/classDiagramImageTemplate.png")
				.createImage();
		assertEquals(classDiagramImage.getImageData().alphaData, classDiagramImageTemplate.getImageData().alphaData);

		classDiagramImageTemplate.dispose();
		classDiagramImage.dispose();
	}

	/**
	 * Fills the dog ClassModel with method and field.
	 * @param dogModel to fill
	 */
	private void fillClassModel(ClassModel dogModel) {
		dogModel.setName("Dog");
		dogModel.setType(ClassType.CLASS);
		dogModel.setParentName("Animal");

		MethodModel bafMethod = new MethodModel();
		bafMethod.setName("baf");
		bafMethod.setReturnType("String");
		bafMethod.setVisibility(Visibility.PUBLIC);
		dogModel.addMethod(bafMethod);

		FieldModel nameField = new FieldModel();
		nameField.setName("name");
		nameField.setType("String");
		nameField.setVisibility(Visibility.PRIVATE);
		dogModel.addField(nameField);
	}

	/**
	 * Test for generating Image from PlantUML source code.
	 */
	@Test
	void testGenerateImageFromPlantUMLSourceCode() {
		String plantUMLSource = "@startuml\r\n"
				+ "scale 1.0\r\n"
				+ "class Dog {\r\n"
				+ "{field}-name : String\r\n"
				+ "{method}+baf() : String\r\n"
				+ "}\r\n"
				+ "Dog --|> Animal\r\n"
				+ "@enduml";

		Image classDiagramImage = imageGenerator.generateImageFromSourceString(plantUMLSource);
		Image classDiagramImageTemplate = ImageDescriptor.createFromFile(getClass(),"../../testsData/classDiagramImageTemplate.png")
				.createImage();
		assertEquals(classDiagramImage.getImageData().alphaData, classDiagramImageTemplate.getImageData().alphaData);

		classDiagramImageTemplate.dispose();
		classDiagramImage.dispose();
	}
}
