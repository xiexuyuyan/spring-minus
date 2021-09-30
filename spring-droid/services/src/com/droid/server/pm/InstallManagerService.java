package com.droid.server.pm;

import com.droid.server.clz.ClassloaderManagerService;
import droid.content.Action;
import droid.content.ComponentName;
import droid.content.Parameter;
import droid.content.pm.ControllerInfo;
import droid.content.pm.InstallManager;
import droid.content.pm.PackageInfo;
import javafx.util.Pair;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class InstallManagerService extends InstallManager {

    public InstallManagerService() {
        System.out.println("InstallManagerService constructor init.");
    }

    public PackageInfo loadJarFile(String filePath) throws IOException {
        return loadJarFile(new String[]{filePath}).get(0);
    }

    public List<PackageInfo> loadJarFile(String[] filePaths) throws IOException {
        List<PackageInfo> packageInfoList = new ArrayList<>();
        String projectDir = System.getProperty("user.dir");
        for (String filePath : filePaths) {
            JarFile jarFile = performJarFile(projectDir + filePath);
            if (jarFile == null) {
                return packageInfoList;
            }
            PackageInfo packageInfo = performPackage(jarFile);
            if (packageInfo != null) {
                packageInfo.setPkgPath(jarFile.getName());
            }
            packageInfoList.add(packageInfo);
        }
        return packageInfoList;
    }

    private JarFile performJarFile(String jarFilePath) throws IOException{
        String jarFileUrlPath = PackageInfo.JAR_URL_PREFIX
                + jarFilePath
                + PackageInfo.JAR_URL_SUFFIX;
        URL jarUrl = new URL(jarFileUrlPath);
        return  ((JarURLConnection) jarUrl.openConnection()).getJarFile();
    }

    private PackageInfo performPackage(JarFile jarFile) throws IOException {
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        JarEntry manifestEntry = null;
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();

            if (jarEntry.isDirectory()) {
                continue;
            }

            String entryName = jarEntry.getName();
            if (entryName.equals(PackageInfo.JAR_MANIFEST)) {
                manifestEntry = jarEntry;
            }
        }

        if (manifestEntry == null) {
            return null;
        }

        InputStream inputStream = jarFile.getInputStream(manifestEntry);

        // todo("Check the package info generated by the manifest
        //  to see if the jar package can find the corresponding class")
        return parseJarFileManifest(inputStream);
    }

    private PackageInfo parseJarFileManifest(InputStream inputStream) throws IOException {
        Document document = null;
        try {
            document = formatDocument(inputStream);
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        if (document == null) {
            return null;
        }

        return parseManifestDocument(document);
    }

    private PackageInfo parseManifestDocument(Document document) {
        PackageInfo packageInfo = new PackageInfo();

        Element rootElement = document.getRootElement();
        String pkgName = rootElement.attribute("package").getValue();
        packageInfo.setPkgName(pkgName);

        Iterator<Element> controllerIterator = rootElement.elementIterator("controller");
        List<ControllerInfo> controllers = new ArrayList<>();
        while (controllerIterator.hasNext()) {
            Element controllerElement = controllerIterator.next();
            String controllerName = controllerElement.attributeValue("name");
            ComponentName componentName = new ComponentName(pkgName, controllerName);
            ControllerInfo controllerInfo = new ControllerInfo(componentName);
            Action[] actions = parseActionUrl(controllerElement);
            controllerInfo.setActions(actions);
            controllers.add(controllerInfo);
        }
        ControllerInfo[] controllerInfoArray = new ControllerInfo[controllers.size()];
        for (int i = 0; i < controllerInfoArray.length; i++) {
            controllerInfoArray[i] = controllers.get(i);
        }
        packageInfo.setControllers(controllerInfoArray);

        return packageInfo;
    }

    private Action[] parseActionUrl(Element controller){
        Iterator<Element> actionIterator = controller.elementIterator("action");
        List<Action> actions = new ArrayList<>();
        while (actionIterator.hasNext()) {
            Element actionElement = actionIterator.next();
            String url = actionElement.attributeValue("url");
            String name = actionElement.attributeValue("name");
            Parameter[] parameters = parseParameter(actionElement);
            Action action = new Action();
            action.setUrl(url);
            action.setName(name);
            action.setParameters(parameters);
            actions.add(action);
        }
        Action[] actionArray = new Action[actions.size()];
        for (int i = 0; i < actionArray.length; i++) {
            actionArray[i] = actions.get(i);
        }
        return actionArray;
    }

    private Parameter[] parseParameter(Element actionElement){
        List<Parameter> params = new ArrayList<>();
        Iterator<Element> actionIterator = actionElement.elementIterator("parameter");
        while (actionIterator.hasNext()) {
            Element paramElement = actionIterator.next();
            String name = paramElement.attributeValue("name");
            String type = paramElement.attributeValue("type");
            Parameter parameter = new Parameter();
            parameter.setName(name);
            parameter.setType(type);
            parameter.setTypeClz(Parameter.toClass(type));
            params.add(parameter);
        }
        Parameter[] paramArray = new Parameter[params.size()];
        for (int i = 0; i < paramArray.length; i++) {
            paramArray[i] = params.get(i);
        }
        return paramArray;
    }

    private Document formatDocument(InputStream inputStream)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        org.w3c.dom.Document doc = db.parse(inputStream);
        DOMReader domReader = new DOMReader();
        return domReader.read((org.w3c.dom.Document) doc);
    }

























            /*if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                String classFullName = entryName
                        .replace("/", ".")
                        .substring(0, entryName.length() - 6);
            }*/


}