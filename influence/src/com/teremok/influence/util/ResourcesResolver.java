package com.teremok.influence.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by Алексей on 24.06.2014
 */
public class ResourcesResolver implements FileHandleResolver {

    private static final String EXTERNAL_PATH_PREFIX = ".influence/";

    @Override
    public FileHandle resolve(String fileName) {
        FileHandle retFile;
        retFile = Gdx.files.external(EXTERNAL_PATH_PREFIX + fileName);

        if (! retFile.exists()) {
            retFile = Gdx.files.internal(fileName);
        }
        return retFile;
    }
}
