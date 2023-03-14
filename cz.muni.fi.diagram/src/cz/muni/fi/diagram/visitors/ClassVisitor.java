package cz.muni.fi.diagram.visitors;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.model.FieldModel;
import cz.muni.fi.diagram.model.MethodModel;
import cz.muni.fi.diagram.model.ParameterModel;
import cz.muni.fi.diagram.model.Visibility;
import cz.muni.fi.diagram.view.ClassDiagram;
import cz.muni.fi.diagram.view.ClassObject;

public class ClassVisitor extends ASTVisitor {
	ClassModel classModel = new ClassModel();
	ClassDiagram classDiagram;
	
	public ClassVisitor() {
		// ...
	}

	public ClassModel getClassModel() {
		return classModel;
	}

	@Override
    public boolean visit(TypeDeclaration node) {
        // Create a new ClassModel object to represent the class
        String className = node.getName().getIdentifier();
        classModel.setName(className);

        // Set the superclass and interface information
        if (node.getSuperclassType() != null) {
            classModel.setSuperClassName(node.getSuperclassType().toString());
        }
        for (Object o : node.superInterfaceTypes()) {
            Type t = (Type) o;
            classModel.addInterfaceName(t.toString());
        }
        
        // Set list of subclasses
        for (TypeDeclaration type : node.getTypes()) {
            // Check if the type extends the superclass
            if (type.getSuperclassType() != null && type.getSuperclassType().toString().equals(className)) {
                // Add the subclass to a list
                String subclass = type.getName().getFullyQualifiedName();
                classModel.addSubclass(subclass);
            }
        }

        return true;
    }

    @Override
    public boolean visit(FieldDeclaration node) {
        // Create a new FieldModel object to represent the field
        FieldModel fieldModel = new FieldModel();
        fieldModel.setName(node.fragments().get(0).toString());
        fieldModel.setType(node.getType().toString());

        int modifiers = node.getModifiers();
        fieldModel.setFinal(Modifier.isFinal(modifiers));
        fieldModel.setStatic(Modifier.isStatic(modifiers));
        if (Modifier.isPrivate(modifiers)) {
        	fieldModel.setVisibility(Visibility.PRIVATE);
        }
        else if (Modifier.isProtected(modifiers)) {
        	fieldModel.setVisibility(Visibility.PROTECTED);
        }
        else {
        	fieldModel.setVisibility(Visibility.PUBLIC);
        }
        
        // Add the field model to the ClassModel
        classModel.addField(fieldModel);

        return true;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        // Create a new MethodModel object to represent the method
        MethodModel methodModel = new MethodModel();
        methodModel.setName(node.getName().getIdentifier());
        Type returnType = node.getReturnType2();
        if (returnType != null) {
        	methodModel.setReturnType(node.getReturnType2().toString());
        }

        int modifiers = node.getModifiers();
        methodModel.setAbstract(Modifier.isAbstract(modifiers));
        methodModel.setStatic(Modifier.isStatic(modifiers));
        if (Modifier.isPrivate(modifiers)) {
        	methodModel.setVisibility(Visibility.PRIVATE);
        }
        else if (Modifier.isProtected(modifiers)) {
        	methodModel.setVisibility(Visibility.PROTECTED);
        }
        else {
        	methodModel.setVisibility(Visibility.PUBLIC);
        }

        // Parse the parameters
        for (Object o : node.parameters()) {
            SingleVariableDeclaration param = (SingleVariableDeclaration) o;
            ParameterModel paramModel = new ParameterModel();
            paramModel.setName(param.getName().getIdentifier());
            paramModel.setType(param.getType().toString());
            methodModel.addParameter(paramModel);
        }

        // Add the method model to the ClassModel
        classModel.addMethod(methodModel);

        return true;
    }
}