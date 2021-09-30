package droid.view;

import droid.content.ContextWrapper;

public class ContextThemeWrapper extends ContextWrapper {
    @Override
    public Object getSystemService(String name) {
        return null;
    }
}
