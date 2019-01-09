package safayat.orm.reflect;

import com.mysql.cj.core.util.StringUtils;
import safayat.orm.annotation.ManyToMany;
import safayat.orm.annotation.ManyToOne;
import safayat.orm.annotation.OneToMany;
import safayat.orm.config.ConfigManager;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Created by safayat on 1/8/19.
 */
public class RelationInfo {
    private Annotation relation;


    public RelationInfo(Annotation relation) {
        this.relation = relation;
    }

    public boolean isOneToMany(){
        return relation instanceof OneToMany;
    }

    public boolean isManyToMany(){
        return relation instanceof ManyToMany;
    }

    public boolean isManyToOne(){
        return relation instanceof ManyToOne;
    }

    public String typeAsString(){
        return relation.annotationType().getName();
    }

    public Annotation getRelation(){
        return relation;
    }

    public String getNativeColumn(){
        if(isOneToMany()){
            return ((OneToMany)relation).nativeColumnName();
        }
        if(isManyToMany()){
            return ((ManyToMany)relation).nativeColumnName();
        }
        return ((ManyToOne)relation).nativeColumnName();
    }

    public String getMatchingColumn(){
        if(isOneToMany()){
            return ((OneToMany)relation).matchingColumnName();
        }
        if(isManyToMany()){
            return ((ManyToMany)relation).matchingColumnName();
        }
        return ((ManyToOne)relation).matchingColumnName();
    }

    public String getNativeColumnWithDefaultAlias(Class parent){
        return getTableName(parent).toLowerCase() + "." + getNativeColumn();
    }

    public String getMatchingColumnWithDefaultAlias(){
        return getChildTableName().toLowerCase() + "." + getMatchingColumn();
    }

    public String getFieldName(){
        if(isOneToMany()){
            return ((OneToMany)relation).name();
        }
        if(isManyToMany()){
            return ((ManyToMany)relation).name();
        }
        return ((ManyToOne)relation).name();
    }

    public String getNativeRelationColumn(){
        return isManyToMany() ? ((ManyToMany)relation).nativeRelationColumnName() : null;
    }
    public String getMatchingRelationColumn(){
        return isManyToMany() ? ((ManyToMany)relation).matchingRelationColumnName() : null;
    }
    public String getNativeRelationColumnWithDefaultAlias(){
        return getRelationTable().toLowerCase() + "." + getNativeRelationColumn();
    }
    public String getMatchingRelationColumnWithDefaultAlias(){
        return getRelationTable().toLowerCase() + "." + getMatchingRelationColumn();
    }

    public String getRelationTable(){
        return isManyToMany() ? ((ManyToMany)relation).relationTable() : null;
    }

    public Class getFieldType(){
        if(isOneToMany()){
            return ((OneToMany)relation).type();
        }
        if(isManyToMany()){
            return ((ManyToMany)relation).type();
        }
        return ((ManyToOne)relation).type();
    }

    public boolean haveRelationInfo(){
        if(StringUtils.isNullOrEmpty(getNativeColumn()) || StringUtils.isNullOrEmpty(getMatchingColumn())) return false;
        if(isManyToMany()){
            if(StringUtils.isNullOrEmpty(getNativeRelationColumn()) || StringUtils.isNullOrEmpty(getMatchingRelationColumn())) return false;
        }
        return true;
    }

    public boolean doNotHaveRelationInfo(){
        return !haveRelationInfo();
    }

    public String getTableName(Class parent){
        return ConfigManager.getInstance().getTableName(parent);
    }

    public String getChildTableName(){
        return ConfigManager.getInstance().getTableName(getFieldType());
    }

    public  String createManyToManyJoinSql(Class parent) throws Exception{

        if(doNotHaveRelationInfo()) throw new Exception("Not enough relation info");
        StringBuilder sql = new StringBuilder();
        sql.append(" join ").append(getRelationTable()).append(" ").append(getRelationTable().toLowerCase())
                .append(" on ").append(getNativeColumnWithDefaultAlias(parent)).append("=").append(getNativeRelationColumnWithDefaultAlias())
                .append(" join ").append(getChildTableName()).append(" ").append(getChildTableName().toLowerCase())
                .append(" on ").append(getMatchingRelationColumnWithDefaultAlias()).append("=").append(getMatchingColumnWithDefaultAlias());
        return sql.toString();

    }

    public  String createJoinSql(
            Class parent
    ) throws Exception{

        return new StringBuilder(" ")
                .append(getTableName(parent)).append(" ").append(getTableName(parent).toLowerCase())
                .append(" join ")
                .append(getChildTableName()).append(" ").append(getChildTableName().toLowerCase())
                .append(" on ")
                .append(getNativeColumnWithDefaultAlias(parent))
                .append(" = ")
                .append(getMatchingColumnWithDefaultAlias())
                .toString();
    }


}
