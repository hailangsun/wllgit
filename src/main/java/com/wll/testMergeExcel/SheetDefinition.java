package com.wll.testMergeExcel;

import java.io.Serializable;
import java.util.List;


public class SheetDefinition implements Serializable {

    private static final long serialVersionUID = 1632248617174732404L;
    private String name;
    private String displayName;
    private List<FieldDefinition> fieldList;
    private List<FieldDefinition> fieldMergeList;//合并单元格
    private List<FieldDefinition> fieldMergeTwoList;//合并单元格
    private List<String> sqlList;

    public List<FieldDefinition> getFieldMergeTwoList() {
        return fieldMergeTwoList;
    }

    public void setFieldMergeTwoList(List<FieldDefinition> fieldMergeTwoList) {
        this.fieldMergeTwoList = fieldMergeTwoList;
    }

    public List<FieldDefinition> getFieldMergeList() {
        return fieldMergeList;
    }

    public void setFieldMergeList(List<FieldDefinition> fieldMergeList) {
        this.fieldMergeList = fieldMergeList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<FieldDefinition> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<FieldDefinition> fieldList) {
        this.fieldList = fieldList;
    }

    public List<String> getSqlList() {
        return sqlList;
    }

    public void setSqlList(List<String> sqlList) {
        this.sqlList = sqlList;
    }
}
