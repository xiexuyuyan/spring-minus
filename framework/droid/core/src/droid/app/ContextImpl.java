package droid.app;

import droid.content.Context;
import droid.content.Intent;
import droid.content.cm.ControllerManager;

class ContextImpl extends Context {
    final Object[] mServiceCache = SystemServiceRegister.createServiceCache();

    @Override
    public Object getSystemService(String name) {
        return SystemServiceRegister.getSystemService(this, name);
    }

    @Override
    public void executeController(Intent intent) {
        ControllerManager controllerManager =
                (ControllerManager) getSystemService(Context.CONTROLLER_SERVICE);
        controllerManager.executeController(intent);
    }
}
