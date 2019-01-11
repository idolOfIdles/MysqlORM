package safayat.orm.reflect;

import safayat.orm.annotation.ManyToOne;
import safayat.orm.annotation.OneToMany;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by safayat on 10/22/18.
 */
public class RelationAnnotationInfo {
    private Annotation relationAnnotation;
    private Class parent;
    private Map<String,Map<String,Boolean>> mapped;
    public RelationAnnotationInfo(Annotation relationAnnotationInfo, Class parent) {
        this.relationAnnotation = relationAnnotationInfo;
        this.parent = parent;
        mapped = new HashMap<String, Map<String, Boolean>>();
    }

    public Annotation getRelationAnnotation() {
        return relationAnnotation;
    }

    public Class getParent() {
        return parent;
    }

    public Class getChild() {
        if(relationAnnotation instanceof OneToMany) return ((OneToMany) relationAnnotation).type();
        return ((ManyToOne) relationAnnotation).type();

    }

    public boolean isAlreadyMapped(String parent, String child){
        Map<String, Boolean> childMap = mapped.get(parent);
        if( childMap != null){
            return childMap.get(child) != null;
        }
        return false;
    }
    public void addMap(String parent, String child){
        Map<String, Boolean> childMap = mapped.get(parent);
        if(childMap == null){
            childMap = new HashMap<String, Boolean>();
            mapped.put(parent, childMap);
        }
        childMap.put(child, true);
    }
}
