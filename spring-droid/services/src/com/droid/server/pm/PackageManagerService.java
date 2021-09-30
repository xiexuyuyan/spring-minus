package com.droid.server.pm;

import droid.content.Action;
import droid.content.ComponentName;
import droid.content.Intent;
import droid.content.pm.ControllerInfo;
import droid.content.pm.PackageInfo;
import droid.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PackageManagerService extends PackageManager {
    final List<PackageInfo> mPackages =
            new ArrayList<>();

    @Override
    public List<PackageInfo> getAllPackages() {
        return mPackages;
    }

    @Override
    public void updatePackages(Map<ClassLoader, List<PackageInfo>> packageLinkedPackagesList) {
        for (List<PackageInfo> value : packageLinkedPackagesList.values()) {
            mPackages.addAll(value);
        }
    }

    public PackageManagerService() {
        System.out.println("PackageManagerService constructor init.");
    }


    public ControllerInfo resolveComponentName(ComponentName component) {
        for (PackageInfo mPackage : mPackages) {
            for (ControllerInfo controller : mPackage.getControllers()) {
                if (component.getPackageName().equals(controller.getPackageName())) {
                    String componentClassName = component.getClassName();
                    String controllerClassName = controller.getClassName();
                    if (componentClassName.equals(controllerClassName)) {
                        return controller;
                    }
                    String pgkName = component.getPackageName();
                    if (!componentClassName.startsWith(pgkName)) {
                        if (!componentClassName.startsWith(".")) {
                            componentClassName = "." + componentClassName;
                        }
                        componentClassName = pgkName + componentClassName;
                    }
                    if (!controllerClassName.startsWith(pgkName)) {
                        if (!controllerClassName.startsWith(".")) {
                            controllerClassName = "." + controllerClassName;
                        }
                        controllerClassName = pgkName + controllerClassName;
                    }
                    if (componentClassName.equals(controllerClassName)) {
                        return controller;
                    }
                }
            }
        }
        return null;
    }

    public ControllerInfo resolveAction(Action action){
        for (PackageInfo mPackage : mPackages) {
            for (ControllerInfo controller : mPackage.getControllers()) {
                for (Action controllerAction : controller.getActions()) {
                    if (controllerAction.equalsByArguments(
                            action.getUrl(), action.getName(), action.getArguments())) {
                        return controller;
                    }
                }
            }
        }
        return null;
    }
}
