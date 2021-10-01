package cat.handler;

import droid.content.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

public class ServletHandler {

    public static void handle(Context context, ServletRequest req, ServletResponse res) {
        String requestUri = ((HttpServletRequest)req).getRequestURI();
        System.out.println("requestUri = " + requestUri);
        String[] urlSplits = requestUri.split("/", 3);
        if (urlSplits.length != 3) {
            return;
        }
        String pkgName = urlSplits[1];
        String url = urlSplits[2];

        Map<String, String[]> params = req.getParameterMap();
        Argument[] arguments = new Argument[params.size()];
        int i = 0;
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            arguments[i++] = new Argument(entry.getKey(), entry.getValue()[0]);
        }

        System.out.println("arguments = " + Arrays.toString(arguments));

        Action action = new Action(url, null);
        action.setArguments(arguments);
        Intent intent = new WebIntent(pkgName);
        intent.setAction(action);
        context.executeController(intent);
    }

}
