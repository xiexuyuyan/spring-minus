package droid.content;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class WebIntent extends Intent{
    private ServletRequest request;
    private ServletResponse response;

    public WebIntent() {
        super();
    }

    public WebIntent(ComponentName component) {
        super(component);
    }

    public WebIntent(Action action) {
        super(action);
    }

    @Override
    public Action getAction() {
        return super.getAction();
    }

    @Override
    public Intent setAction(Action action) {
        return super.setAction(action);
    }

    @Override
    public ComponentName getComponent() {
        return super.getComponent();
    }

    @Override
    public Intent setComponent(ComponentName component) {
        return super.setComponent(component);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public ServletRequest getRequest() {
        return request;
    }

    public void setRequest(ServletRequest request) {
        this.request = request;
    }

    public ServletResponse getResponse() {
        return response;
    }

    public void setResponse(ServletResponse response) {
        this.response = response;
    }
}
