package utils;

import java.io.File;

public final class DataPathResolve {

    private DataPathResolve() {
    }

    public static String resolveDataPath(String file) {
        final String dataPath = "src/test/resources/data/";
        String filePath = dataPath + file;
        return new File(filePath).getAbsolutePath();
    }
}
