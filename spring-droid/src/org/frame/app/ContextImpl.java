package org.frame.app;

import org.frame.content.Context;

class ContextImpl extends Context {
    final Object[] mServiceCache = SystemServiceRegister.createServiceCache();

    @Override
    public Object getSystemService(String name) {
        return SystemServiceRegister.getSystemService(this, name);
    }
}
