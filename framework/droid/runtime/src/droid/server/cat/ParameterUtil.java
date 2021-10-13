package droid.server.cat;

import droid.content.Argument;
import droid.content.Parameter;

import java.lang.reflect.Array;
import java.util.Map;

public class ParameterUtil {
    public static Argument[] formatValues(Map<String, String[]> entries, Parameter[] parameters) {
        Argument[] arguments = new Argument[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String paramName = parameter.getName();
            String[] values = entries.get(paramName);
            Argument argument = formatValue(values, parameter);
            arguments[i] = argument;
        }

        return arguments;
    }

    public static Argument formatValue(String[] values, Parameter parameter) {
        Object valueObject = null;
        Class<?> parameterCLz = parameter.getTypeClz();
        if (!parameterCLz.isArray()) {
            valueObject = createValue(parameterCLz, values[0]);
        } else {
            Class<?> paramBaseClz = parameterCLz.getComponentType();
            valueObject = Array.newInstance(paramBaseClz, values.length);
            for (int i = 0; i < values.length; i++) {
                Array.set(valueObject, i, createValue(paramBaseClz, values[i]));
            }
        }

        return new Argument(parameter.getName(), valueObject);
    }

    public static Object createValue(Class<?> clz, String value) {
        Object o = null;
        if (clz.equals(int.class)) {
            o = Integer.parseInt(value);
        } else if (clz.equals(boolean.class)) {
            o = Boolean.parseBoolean(value);
        } else if (clz.equals(String.class)) {
            o = value;
        }
        return o;
    }


    public static boolean match(String[] argumentNames, Parameter[] parameters) {
        if (argumentNames.length != parameters.length) {
            return false;
        }

        int i = 0;
        for (; i < parameters.length; i++) {
            String parameterName = parameters[i].getName();

            int j = 0;
            for (; j < argumentNames.length; j++) {
                if (argumentNames[j].equals(parameterName)) {
                    break;
                }
            }

            if (j == argumentNames.length) {
                return false;
            }
        }

        return true;
    }
}
