/*
 * Copyright 2015 Tarık Yılmaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aegeus.utils;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class SchemaUtils {
    private static List<Class<?>> getClasses(File dir, String pkg) throws ClassNotFoundException {
        File[] files = dir.listFiles();

        Preconditions.checkNotNull(files, dir.getAbsolutePath() + " directory is empty");

        List<Class<?>> group = new ArrayList<>();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                group.add(Class.forName(pkg.replace('/', '.') + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }

        return group;
    }

    public static List<Class> getDepends(String pkg) throws IOException, ClassNotFoundException {
        if (Strings.isNullOrEmpty(pkg)) {
            throw new NullPointerException(String.format("The %s package is missing.", pkg));
        }

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        pkg = pkg.replace('.', File.separatorChar);

        List<File> files = new ArrayList<>();
        Enumeration resources = loader.getResources(pkg);
        while (resources.hasMoreElements()) {
            files.add(new File(((URL) resources.nextElement()).getFile()));
        }

        List<Class> classes = new ArrayList<>();
        for (File file : files) {
            classes.addAll(getClasses(file, pkg));
        }

        return classes;
    }
}