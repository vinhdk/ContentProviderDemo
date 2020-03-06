package se1302.demo.contentproviderdemo.models;

import java.io.Serializable;

public class StudentModel implements Serializable {
    private String id, name;
    private int core;
    private int isGradute;

    public StudentModel() {
    }

    public StudentModel(String id, String name, int core, int isGradute) {
        this.id = id;
        this.name = name;
        this.core = core;
        this.isGradute = isGradute;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCore() {
        return core;
    }

    public void setCore(int core) {
        this.core = core;
    }

    public int isGradute() {
        return isGradute;
    }

    public void setGradute(int gradute) {
        isGradute = gradute;
    }

    @Override
    public String toString() {
        return "name: " + this.name + " core: " + this.core;
    }
}
