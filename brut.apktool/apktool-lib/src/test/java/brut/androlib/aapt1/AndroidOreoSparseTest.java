/*
 *  Copyright (C) 2010 Ryszard Wiśniewski <brut.alll@gmail.com>
 *  Copyright (C) 2010 Connor Tumbleson <connor.tumbleson@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package brut.androlib.aapt1;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import brut.androlib.Androlib;
import brut.androlib.ApkDecoder;
import brut.androlib.BaseTest;
import brut.androlib.TestUtils;
import brut.androlib.options.BuildOptions;
import brut.common.BrutException;
import brut.directory.ExtFile;
import brut.util.OS;

public class AndroidOreoSparseTest extends BaseTest {
    @BeforeClass
    public static void beforeClass() throws Exception {
        TestUtils.cleanFrameworkFile();
        sTmpDir = new ExtFile(OS.createTempDirectory());
        sTestOrigDir = new ExtFile(sTmpDir, "issue1594-orig");
        sTestNewDir = new ExtFile(sTmpDir, "issue1594-new");
        LOGGER.info("Unpacking sparse.apk...");
        TestUtils.copyResourceDir(AndroidOreoSparseTest.class, "aapt1/issue1594", sTestOrigDir);

        File testApk = new File(sTestOrigDir, "sparse.apk");

        LOGGER.info("Decoding sparse.apk...");
        ApkDecoder apkDecoder = new ApkDecoder(testApk);
        apkDecoder.setOutDir(sTestNewDir);
        apkDecoder.decode();

        LOGGER.info("Building sparse.apk...");
        BuildOptions buildOptions = new BuildOptions();
        new Androlib(buildOptions).build(sTestNewDir, testApk);
    }

    @AfterClass
    public static void afterClass() throws BrutException {
        OS.rmdir(sTmpDir);
    }

    @Test
    public void buildAndDecodeTest() {
        assertTrue(sTestNewDir.isDirectory());
        assertTrue(sTestOrigDir.isDirectory());
    }

    @Test
    public void ensureStringsOreoTest() {
        assertTrue((new File(sTestNewDir, "res/values-v26/strings.xml").isFile()));
    }
}
