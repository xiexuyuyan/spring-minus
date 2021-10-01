package droid.content;

public class Parameter {
    private String name;
    private String type;
    private Class<?> typeClz;

    public Parameter() {
    }

    public Parameter(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Parameter(String name, Class<?> typeClz) {
        this.name = name;
        this.typeClz = typeClz;
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

    public Class<?> getTypeClz() {
        return typeClz;
    }

    public void setTypeClz(Class<?> typeClz) {
        this.typeClz = typeClz;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", typeClz=" + typeClz +
                '}';
    }

    public static String toClassName(Class<?> typeClz) {
        if (Boolean.class.equals(typeClz)) {
            return "boolean";
        } else if (int.class.equals(typeClz) || Integer.class.equals(typeClz)) {
            return "int";
        }
        return "string";
    }

    public static Class<?> toClass(String name){
        switch (name) {
            case "string":
            case "String":
                return String.class;

            case "int":
                return int.class;

            case "Int":
            case "Integer":
                return Integer.class;

            default:
                return Class.class;
        }
    }
}
