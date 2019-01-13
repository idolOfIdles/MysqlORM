package safayat.orm.reflect;

import com.mysql.cj.core.util.StringUtils;
import safayat.orm.annotation.ManyToMany;
import safayat.orm.annotation.ManyToOne;
import safayat.orm.annotation.OneToMany;
import safayat.orm.config.ConfigManager;
import safayat.orm.jdbcUtility.TableInfo;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Created by safayat on 1/8/19.
 */
public class RelationInfo {
    private Annotation relation;
    private Class parent;


    public RelationInfo(Annotation relation, Class parent) {
        this.relation = relation;
        this.parent = parent;
    }

    public boolean isRelationAnnotation(){
        return isManyToOne() || isOneToMany() || isManyToMany();
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

    public String getNativeColumnWithDefaultAlias(){
        return getTableName().toLowerCase() + "." + getNativeColumn();
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
        if(Util.isEmpty(getNativeColumn()) || Util.isEmpty(getMatchingColumn())) return false;
        if(isManyToMany()){
            if(Util.isEmpty(getNativeRelationColumn()) || Util.isEmpty(getMatchingRelationColumn())) return false;
        }
        return true;
    }

    public boolean doNotHaveRelationInfo(){
        return !haveRelationInfo();
    }

    public String getTableName(){
        return TableInfo.getTableName(parent);
    }

    public String getChildTableName(){
        return TableInfo.getTableName(getFieldType());
    }

    public  String createManyToManyJoinSql() throws Exception{

        if(doNotHaveRelationInfo()) throw new Exception("Not enough relation info");
        StringBuilder sql = new StringBuilder();
        sql.append(" join ").append(getRelationTable()).append(" ").append(getRelationTable().toLowerCase())
                .append(" on ").append(getNativeColumnWithDefaultAlias()).append("=").append(getNativeRelationColumnWithDefaultAlias())
                .append(" join ").append(getChildTableName()).append(" ").append(getChildTableName().toLowerCase())
                .append(" on ").append(getMatchingRelationColumnWithDefaultAlias()).append("=").append(getMatchingColumnWithDefaultAlias());
        return sql.toString();

    }

    public  String createJoinSql(
    ) throws Exception{

        return new StringBuilder(" ")
                .append(getTableName()).append(" ").append(getTableName().toLowerCase())
                .append(" join ")
                .append(getChildTableName()).append(" ").append(getChildTableName().toLowerCase())
                .append(" on ")
                .append(getNativeColumnWithDefaultAlias())
                .append(" = ")
                .append(getMatchingColumnWithDefaultAlias())
                .toString();
    }

    public Class getParent() {
        return parent;
    }
}
