package cz.muni.fi.diagram.visitors;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import cz.muni.fi.diagram.model.ClassModel;

/**
 * Creates class model from given Compilation Unit using ClassVisitor.
 * 
 * @author Veronika Lenkova
 */
public class ClassModelFactory {
	
	private ClassModelFactory() {
		//empty
	}

	public static ClassModel createClassModel(ICompilationUnit unit) {
		CompilationUnit parse = parse(unit);
		ClassVisitor visitor = new ClassVisitor();
		parse.accept(visitor);
		return visitor.getClassModel();
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
