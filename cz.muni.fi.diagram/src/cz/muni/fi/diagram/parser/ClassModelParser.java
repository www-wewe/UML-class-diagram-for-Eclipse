/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.parser;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import cz.muni.fi.diagram.model.ClassModel;

/**
 * Creates class model from given Compilation Unit using ClassVisitor.
 * 
 * @author Veronika Lenková
 */
public class ClassModelParser {
	
	private ClassModelParser() {
		// Intentionally empty
	}

	/**
	 * Parses CompilationUnit and using ClassVisitor creates ClassModel.
	 * @param compilationUnit
	 * @return ClassModel
	 */
	public static ClassModel createClassModel(ICompilationUnit compilationUnit) {
		CompilationUnit parse = parse(compilationUnit);
		ClassVisitor visitor = new ClassVisitor();
		parse.accept(visitor);
		ClassModel classModel = visitor.getClassModel();
		try {
			classModel.setPackageName(compilationUnit.getPackageDeclarations()[0].getElementName());
		} catch (JavaModelException e) {
			classModel.setPackageName("unknown");
			e.printStackTrace();
		}
		return classModel;
	}

	/**
	 * Reads a ICompilationUnit and creates the AST DOM for manipulating the Java source file
	 * @param unit
	 * @return CompilationUnit
	 */
	private static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null);
	}
}
