package meta;

import java.net.URL;
import java.net.URLClassLoader;

public class MetaLoader extends URLClassLoader {
    public MetaLoader() {
        super(
                new URL[]{},
                ClassLoader.getSystemClassLoader()
        );
    }

    public void add(URL url){
        addURL(url);
    }
}
