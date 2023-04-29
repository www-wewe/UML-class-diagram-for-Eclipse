/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.parser;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;

import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.model.ClassType;
import cz.muni.fi.diagram.model.FieldModel;
import cz.muni.fi.diagram.model.MethodModel;
import cz.muni.fi.diagram.model.ParameterModel;
import cz.muni.fi.diagram.model.Visibility;

/**
 * AST Visitor which visits CompilationUnit by {@code unit.accept(ASTVisitor)}
 * and reads information from it.
 * 
 * @author Veronika Lenková
 */
public class ClassVisitor extends ASTVisitor {

	/** Class Models representing one java file in the workspace (can have nested classes) */
	List<ClassModel> classModels = new ArrayList<>();

	public ClassVisitor() {
		// Intentionally empty
	}

	/**
	 * @return created class model
	 */
	public ClassModel getClassModel() {
		if (!classModels.isEmpty()) {
			ClassModel classModel = classModels.get(0);
			classModels.remove(0);
			classModel.setNestedClasses(classModels);
			return classModel;
		}
		return new ClassModel();
	}

	@Override
    public boolean visit(TypeDeclaration node) {
        // Create a new ClassModel object to represent the class
		ClassModel classModel = new ClassModel();
		classModel.setType(getClassType(node));
        String className = node.getName().getIdentifier();
        classModel.setName(className);

        // Set the superclass and interface information
        if (node.getSuperclassType() != null) {
        	String superClassName = reduceName(node.getSuperclassType().toString());
            classModel.setParentName(superClassName);
        }
        for (Object o : node.superInterfaceTypes()) {
            Type t = (Type) o;
            String interfaceName = reduceName(t.toString());
            classModel.addInterface(interfaceName);
        }
	    classModels.add(classModel);
        return true;
    }

	/**
	 * Reduces the name if it contains generic type.
	 * @param name
	 * @return new name
	 */
	private String reduceName(String name) {
		int indexGeneric = name.indexOf("<");
		if (indexGeneric > 0) {
			name = name.substring(0, indexGeneric);
		}
		return name;
	}

	/**
	 * @param node - class
	 * @return type of class
	 */
	private ClassType getClassType(TypeDeclaration node) {
		int modifiers = node.getModifiers();
		if (node.isInterface()) {
			return ClassType.INTERFACE;
		} else if (Modifier.isAbstract(modifiers)) {
			return ClassType.ABSTRACT_CLASS;
		} else {
			return ClassType.CLASS;
		}
	}
	
    @Override
    public boolean visit(FieldDeclaration node) {
    	ClassModel classModel = getCurrentClassModel(node);
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
        else if (Modifier.isPublic(modifiers)) {
        	fieldModel.setVisibility(Visibility.PUBLIC);
        }
        else {
        	fieldModel.setVisibility(Visibility.PACKAGE_PRIVATE);
        }
        classModel.addField(fieldModel);
        return true;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
    	ClassModel classModel = getCurrentClassModel(node);
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
        else if (Modifier.isPublic(modifiers)) {
        	methodModel.setVisibility(Visibility.PUBLIC);
        }
        else {
        	methodModel.setVisibility(Visibility.PACKAGE_PRIVATE);
        }

        // Parse the parameters
        for (Object o : node.parameters()) {
            SingleVariableDeclaration param = (SingleVariableDeclaration) o;
            ParameterModel paramModel = new ParameterModel();
            paramModel.setName(param.getName().getIdentifier());
            paramModel.setType(param.getType().toString());
            methodModel.addParameter(paramModel);
        }
        classModel.addMethod(methodModel);
        return false;
    }

	@Override
	public boolean visit(EnumDeclaration node) {
		ClassModel classModel = new ClassModel();
		classModel.setType(ClassType.ENUM);
        String className = node.getName().getIdentifier();
        classModel.setName(className);

        // Set the superclass and interface information
        for (Object o : node.superInterfaceTypes()) {
            Type t = (Type) o;
            String interfaceName = t.toString();
            interfaceName = reduceName(interfaceName);
            classModel.addInterface(interfaceName);
        }
        classModels.add(classModel);
		return true;
	}

	@Override
	public boolean visit(EnumConstantDeclaration node) {
		ClassModel classModel = getCurrentClassModel(node);
		FieldModel fieldModel = new FieldModel();
		fieldModel.setName(node.getName().getFullyQualifiedName());

		int modifiers = node.getModifiers();
        fieldModel.setFinal(Modifier.isFinal(modifiers));
        fieldModel.setStatic(Modifier.isStatic(modifiers));
        if (Modifier.isPrivate(modifiers)) {
        	fieldModel.setVisibility(Visibility.PRIVATE);
        }
        else if (Modifier.isProtected(modifiers)) {
        	fieldModel.setVisibility(Visibility.PROTECTED);
        }
        else if (Modifier.isPublic(modifiers)) {
        	fieldModel.setVisibility(Visibility.PUBLIC);
        }
        else {
        	fieldModel.setVisibility(Visibility.PACKAGE_PRIVATE);
        }
        classModel.addField(fieldModel);
		return true;
	}

	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		// skip the functions in function
		return false;
	}

	/**
	 * Gets the ClassModel for the current type (method/field)
	 * @param className of model
	 * @return class model with {@code className}
	 */
	private ClassModel getCurrentClassModel(BodyDeclaration node) {
		String className = ((AbstractTypeDeclaration)node.getParent())
				.getName().getIdentifier();
		for (ClassModel classModel : classModels) {
		    if (classModel.getName().equals(className)) {
		        return classModel;
		    }
		}
		throw new IllegalArgumentException("The node belongs to a different AST");
    }
}