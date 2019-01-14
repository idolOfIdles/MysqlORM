package safayat.orm.reflect;

import safayat.orm.config.ConfigManager;
import safayat.orm.jdbcUtility.TableMetadata;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationGraph{

    private Class classTreeNode;
    private Map<String, Class> visitedMap = new HashMap<String, Class>();
    private Map<Class, RelationInfo> relationInfoByParentClass = new HashMap<Class, RelationInfo>();

    public RelationGraph(Class classTreeNode) throws Exception{
        this.classTreeNode = classTreeNode;
        visitedMap = new HashMap<>();
        relationInfoByParentClass = new HashMap<>();
        populateRelationDataStructures(classTreeNode);
    }

    private  void populateRelationDataStructures(Class node) throws Exception{
        addToVisitedMap(node);
        List<RelationInfo> annotationList = ConfigManager.getInstance().getTableMetadata(node).getRelationInfos();
        for(RelationInfo relationInfo : annotationList){
            Class child = relationInfo.getFieldType();
            if(child!= null){
                if(notVisited(child)){
                    addNewRelation(relationInfo);
                    populateRelationDataStructures(child);
                }
            }
        }
    }

    private void addNewRelation(RelationInfo relationInfo){
        relationInfoByParentClass.put(relationInfo.getFieldType(), relationInfo);
    }

    private void addToVisitedMap(Class node){
        visitedMap.put(TableMetadata.getTableName(node).toLowerCase(), node);
    }

    private boolean isVisited(Class node){
        return visitedMap.containsKey(TableMetadata.getTableName(node).toLowerCase());
    }
    private boolean notVisited(Class node){
        return !isVisited(node);
    }
    public RelationInfo getRelationInfo(Class parent){
        return relationInfoByParentClass.get(parent);
    }

}
