package cat.handler;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerMapper {
    private final List<ServletHandler> handlers = new ArrayList<>();

    public void handle(ServletRequest req, ServletResponse res) {
        for (ServletHandler handler : handlers) {
            if (handler.handle(req, res)) {
                break;
            }
        }
    }

    public void updateHandlerList(List<ServletHandler> handlerList) {
        handlers.addAll(handlerList);
    }

    public List<ServletHandler> getHandlerList() {
        return handlers;
    }

    public void register(ServletHandler handler) {
        handlers.add(handler);
    }
}
