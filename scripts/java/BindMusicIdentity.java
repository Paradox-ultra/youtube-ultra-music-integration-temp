import javassist.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;

public class BindMusicIdentity {
    public static void main(String[] args) throws Exception {
        ClassPool p = new ClassPool(false);
        p.appendClassPath("/tmp/music-tools/platform33/android-13/android.jar");
        p.appendClassPath("/tmp/music-native-loader/classes");
        p.appendClassPath("/tmp/music-tools/music-plugin-rebased.jar");
        p.appendClassPath("/tmp/music-tools/music-plugin-desugar.jar");
        Map<String, byte[]> modified = new HashMap<>();
        for (String name : new String[]{"adzv", "adyz"}) {
            CtClass c = p.get(name);
            for (CtConstructor ctor : c.getDeclaredConstructors()) {
                ctor.insertBeforeBody("paradox.music.nativeui.AccountBridge.seedPluginPreferences($1);");
                ctor.insertAfter("paradox.music.nativeui.AccountBridge.bindPlugin(this, $1);");
            }
            modified.put(name + ".class", c.toBytecode());
        }
        try (JarFile in = new JarFile("/tmp/music-tools/music-plugin-rebased.jar");
             JarOutputStream out = new JarOutputStream(Files.newOutputStream(Paths.get("/tmp/music-tools/music-plugin-bound.jar")))) {
            Enumeration<JarEntry> es = in.entries();
            while (es.hasMoreElements()) {
                JarEntry e = es.nextElement();
                out.putNextEntry(new JarEntry(e.getName()));
                out.write(modified.containsKey(e.getName()) ? modified.get(e.getName()) : in.getInputStream(e).readAllBytes());
                out.closeEntry();
            }
        }
    }
}
