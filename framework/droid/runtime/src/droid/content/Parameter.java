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
        this.typeClz = toClass(type);
    }

    public Parameter(String name, Class<?> typeClz) {
        this.name = name;
        this.typeClz = typeClz;
    }

    public String getName() {
        return name;
    }

    public Parameter setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public Parameter setType(String type) {
        this.type = type;
        return this;
    }

    public Class<?> getTypeClz() {
        return typeClz;
    }

    public Parameter setTypeClz(Class<?> typeClz) {
        this.typeClz = typeClz;
        return this;
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

            case "boolean":
                return boolean.class;

            case "string[]":
            case "String[]":
                return String[].class;

            case "int[]":
                return int[].class;

            case "boolean[]":
                return boolean[].class;

            default:
                return Class.class;
        }
    }
}
