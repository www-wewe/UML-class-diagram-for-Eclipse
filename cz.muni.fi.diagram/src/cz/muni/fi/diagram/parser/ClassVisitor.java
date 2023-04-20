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
        	String superClassName = removeDisallowedChars(node.getSuperclassType().toString());
            classModel.setParentName(superClassName);
        }
        for (Object o : node.superInterfaceTypes()) {
            Type t = (Type) o;
            String interfaceName = removeDisallowedChars(t.toString());
            classModel.addInterface(interfaceName);
        }
	    classModels.add(classModel);
        return true;
    }

	private String removeDisallowedChars(String interfaceName) {
		int indexGeneric = interfaceName.indexOf("<");
		if (indexGeneric > 0) {
			interfaceName = interfaceName.substring(0, indexGeneric);
		}
		return interfaceName;
	}

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
    	ClassModel classModel = getCurrentClassModel(((AbstractTypeDeclaration)node.getParent())
    			.getName().getIdentifier());
    	assert(classModel != null);
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
    public boolean visit(MethodDeclaration node) { //node.parent
    	ClassModel classModel = getCurrentClassModel(((AbstractTypeDeclaration)node.getParent())
    			.getName().getIdentifier());
    	assert(classModel != null);
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
            interfaceName = removeDisallowedChars(interfaceName);
            classModel.addInterface(interfaceName);
        }
        classModels.add(classModel);
		return true;
	}

	@Override
	public boolean visit(EnumConstantDeclaration node) {
		ClassModel classModel = getCurrentClassModel(((AbstractTypeDeclaration)node.getParent())
    			.getName().getIdentifier());
		assert(classModel != null);
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
	private ClassModel getCurrentClassModel(String className) {
		for (ClassModel classModel : classModels) {
		    if (classModel.getName().equals(className)) {
		        return classModel;
		    }
		}
		assert(false); // unreachable
        return null;
    }
}