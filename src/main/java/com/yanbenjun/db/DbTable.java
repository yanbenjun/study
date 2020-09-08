package com.yanbenjun.db;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

public class DbTable
{
    private static final String CREATE_TABLE_SQL_TEMPLATE = "CREATE TABLE `%s` (%s)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '%s';";
    
    private long id;

    private String tableName;

    private String name;

    private String comment;

    private List<DbField> dbFields;

    public String getCreateTableSql()
    {
        Objects.requireNonNull(this.tableName);
        String tableSegment = this.tableName;
        String fieldsSegment = getFieldsSqlSegment();
        String primaryKeySegment = getPrimaryKeySegment();
        String keySegment = getKeySegment();
        String mainSegment = fieldsSegment + primaryKeySegment + keySegment;
        mainSegment = deleteLastIfPresent(mainSegment);
        String commentSegment = this.comment == null ? StringUtils.EMPTY : this.comment;
        return String.format(CREATE_TABLE_SQL_TEMPLATE, tableSegment,mainSegment, commentSegment);
    }
    
    private String deleteLastIfPresent(String str)
    {
        return StringUtils.isEmpty(str) ? StringUtils.EMPTY : str.substring(0, str.length() - 1);
    }
    
    private String getFieldsSqlSegment()
    {
        if(dbFields == null || dbFields.isEmpty())
        {
            Logger.getLogger("WARN").log(Level.WARNING, "没有可以使用的数据库字段定义。");
            return StringUtils.EMPTY;
        }
        List<DbField> cs = dbFields.stream().sorted(Comparator.comparing(DbField::getOrderInTable))
                .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for (DbField df : cs)
        {
            sb.append(df.toCreateSqlSegment()).append(",");
        }
        return sb.toString();
    }
    
    private String getPrimaryKeySegment()
    {
        List<DbField> primaryFields = dbFields.stream().filter(k->{return k.isPrimaryKey();}).collect(Collectors.toList());
        if(primaryFields.isEmpty())
        {
            return StringUtils.EMPTY;
        }
        String PRIMARY_STR = "PRIMARY KEY (%s),";
        StringBuilder sb = new StringBuilder();
        for(DbField df : primaryFields)
        {
            sb.append("`").append(df.getOriginName()).append("`,");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        return String.format(PRIMARY_STR, sb);
    }
    
    private String getKeySegment()
    {
        List<DbField> keyFields = dbFields.stream().filter(k->{return k.isKey();}).collect(Collectors.toList());
        if(keyFields.isEmpty())
        {
            return StringUtils.EMPTY;
        }
        String KEY_STR = "KEY `%s` (%s) USING BTREE,";
        StringBuilder sb = new StringBuilder();
        for(DbField df : keyFields)
        {
            String str = String.format(KEY_STR, getDefaultKeyName(df.getOriginName()), df.getOriginName());
            sb.append(str);
        }
        return sb.toString();
    }
    
    private String getDefaultKeyName(String fieldName)
    {
        return "index_"+tableName+"_" + fieldName;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public List<DbField> getDbFields()
    {
        return dbFields;
    }

    public void setDbFields(List<DbField> dbFields)
    {
        this.dbFields = dbFields;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }
}
