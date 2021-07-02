package org.yuyan.room.annotation.processor.entity;

public class ColumnType {
    String columnName;// annotation value in columnInfo
    String varName;
    String varClassName;

    public String getColumnName() {
        return columnName;
    }

    public String getVarName() {
        return varName;
    }

    public String getVarClassName() {
        return varClassName;
    }

    public ColumnType(String columnName, String varName, String varClassName) {
        this.columnName = columnName;
        this.varName = varName;
        this.varClassName = varClassName;
    }

    @Override
    public String toString() {
        return "columnName: " + columnName
                + ", varName: " + varName
                + ", varClassName: " + varClassName;
    }
}