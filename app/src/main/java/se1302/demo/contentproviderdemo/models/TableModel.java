package se1302.demo.contentproviderdemo.models;

import java.util.ArrayList;

public class TableModel {
    private String name;
    private ArrayList<ColumnModel> columns;

    public TableModel() {
    }

    public TableModel(String name, ArrayList<ColumnModel> columns) {
        this.name = name;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ColumnModel> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<ColumnModel> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        String columnSql = "(";
        int index = 0;
        for (ColumnModel column : this.columns) {
            columnSql += column.toString() + (index < this.columns.size() - 1 ? "," : "");
            index++;
        }
        columnSql += ")";
        return this.name + columnSql;
    }

    public String[] getArrayColumnName() {
        String[] columnNames = new String[this.columns.size()];
        int index = 0;
        for (ColumnModel column : this.columns) {
            columnNames[index] = column.getName();
            index++;
        }
        return columnNames;
    }

    public String getPrimaryKeyColumnName() {
        String name = "";
        for (ColumnModel column : this.columns) {
            if(column.isPrimary()) {
                name = column.getName();
            }
        }
        return name;
    }
}
