package droid.content;

import java.util.Arrays;

public class Action {
    private String url;
    private String name;
    private Parameter[] parameters;
    private Argument[] arguments;

    @Override
    public String toString() {
        return "Action{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", parameters=" + Arrays.toString(parameters) +
                ", arguments=" + Arrays.toString(arguments) +
                '}';
    }

    public Action() {
    }

    public Action(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public Action setParameters(Parameter[] parameters) {
        this.parameters = parameters;
        return this;
    }

    public Argument[] getArguments() {
        return arguments;
    }

    public Action setArguments(Argument[] arguments) {
        this.arguments = arguments;
        return this;
    }


    public boolean equalsByArguments(String url, String name, Argument[] arguments) {
        if ( !url.equals(this.url) || !name.equals(this.name) ) {
            return false;
        }

        boolean noParam = (this.parameters == null || this.parameters.length == 0);
        boolean noArgv = (this.arguments == null || this.arguments.length == 0);
        boolean inNoArgv = (arguments == null || arguments.length ==0);

        if (noParam && noArgv) {
            if (!inNoArgv) {
                return false;
            }
        }

        if (!noArgv && !inNoArgv && this.arguments.length == arguments.length) {
            for (int i = 0; i < arguments.length; i++) {
                if (!arguments[i].equals(this.arguments[i])) {
                    return false;
                }
            }
        }

        if (!noParam && !inNoArgv && this.parameters.length == arguments.length) {
            for (int i = 0; i < arguments.length; i++) {
                // todo("Checks the type of the argument")
                if (!arguments[i].getName().equals(parameters[i].getName())) {
                    return false;
                }
            }
        }

        return true;
    }
}
