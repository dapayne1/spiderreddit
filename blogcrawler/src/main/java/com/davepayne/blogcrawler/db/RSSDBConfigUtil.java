package com.davepayne.blogcrawler.db;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Only used once to generate initial ormlite_config.txt file.
 */
public class RSSDBConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] classes = new Class[]{
            RSSDBData.class,
    };

    public static void main(String[] args) throws IOException, SQLException {
        writeConfigFile(new File("C:/dev/workspace/davepayne/res/raw/ormlite_config.txt"), classes);
    }
}
