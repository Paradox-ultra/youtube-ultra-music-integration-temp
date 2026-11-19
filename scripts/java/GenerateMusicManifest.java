import java.io.File;
import com.tencent.shadow.core.manifest_parser.ManifestParserKt;
public class GenerateMusicManifest {
 public static void main(String[] args) {
  ManifestParserKt.generatePluginManifest(new File("/tmp/music-native-loader/AndroidManifest-metadata.xml"),new File("/tmp/music-native-loader/generated"),"com.tencent.shadow.core.manifest_parser",ManifestParserKt.createManifestValueParser(new File("/tmp/music-tools/music-plugin-R.txt")));
 }
}
