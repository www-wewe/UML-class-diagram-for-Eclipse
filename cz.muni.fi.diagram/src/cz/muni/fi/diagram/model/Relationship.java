/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.model;

// TODO delete
public class Relationship {
    private ClassModel fromClass;
    private ClassModel toClass;
    private RelationshipType type;
    
    public enum RelationshipType {
        INHERITANCE, REALIZATION, DEPENDENCY, ASSOCIATION, AGGREGATION, COMPOSITION;
    }

    public Relationship(ClassModel fromClass, ClassModel toClass, RelationshipType type) {
        this.fromClass = fromClass;
        this.toClass = toClass;
        this.type = type;
    }

    public ClassModel getFromClass() {
        return fromClass;
    }

    public ClassModel getToClass() {
        return toClass;
    }

    public RelationshipType getType() {
        return type;
    }
    
}

