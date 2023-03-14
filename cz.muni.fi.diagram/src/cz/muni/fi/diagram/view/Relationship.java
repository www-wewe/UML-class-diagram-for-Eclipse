package cz.muni.fi.diagram.view;

public class Relationship {
    private ClassObject fromClass;
    private ClassObject toClass;
    private RelationshipType type;
    
    public enum RelationshipType { // nebudem potrebovať
        INHERITANCE, REALIZATION, DEPENDENCY, ASSOCIATION, AGGREGATION, COMPOSITION;
    }

    public Relationship(ClassObject fromClass, ClassObject toClass, RelationshipType type) {
        this.fromClass = fromClass;
        this.toClass = toClass;
        this.type = type;
    }

    public ClassObject getFromClass() {
        return fromClass;
    }

    public ClassObject getToClass() {
        return toClass;
    }

    public RelationshipType getType() {
        return type;
    }
}

