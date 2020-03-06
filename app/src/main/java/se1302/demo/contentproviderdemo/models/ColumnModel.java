package se1302.demo.contentproviderdemo.models;

public class ColumnModel {
    private String name, type;
    private boolean isRequired, isPrimary;

    public ColumnModel() {
    }

    public ColumnModel(String name, String type, boolean isPrimary, boolean isRequired) {
        this.name = name;
        this.type = type;
        this.isRequired = isRequired;
        this.isPrimary = isPrimary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    @Override
    public String toString() {
        return this.name + " " +  this.type + " " + (this.isPrimary ? "primary key" : "") + (this.isRequired ? " not null" : "");
    }
}
