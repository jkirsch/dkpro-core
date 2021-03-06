/*
 * Copyright 2017
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.tudarmstadt.ukp.dkpro.core.api.datasets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.datasets.Dataset;
import de.tudarmstadt.ukp.dkpro.core.api.datasets.DatasetFactory;
import de.tudarmstadt.ukp.dkpro.core.api.datasets.Split;
import de.tudarmstadt.ukp.dkpro.core.testing.DkproTestContext;

public class DatasetFactoryTest
{
    @Ignore("Used at times for offline testing / development")
    @Test
    public void testOne()
        throws Exception
    {
        Path cache = testContext.getTestOutputFolder().toPath();
        
        DatasetFactory df = new DatasetFactory(cache);
        {
            Dataset ds = df.load("wasr-en-xl-1.00");
            assertDatasetOk(ds);
        }
//        {
//            Dataset ds = df.load("ndt-nb-1.01");
//            assertDatasetOk(ds);
//        }
    }
    
    @Ignore("Used at times for offline testing / development")
    @Test
    public void testLoadAll()
        throws Exception
    {
        Path cache = testContext.getTestOutputFolder().toPath();
        
        DatasetFactory df = new DatasetFactory(cache);
        for (String id : df.listIds()) {
            Dataset ds = df.load(id);
            assertDatasetOk(ds);
        }
    }

    @Ignore("Used at times for offline testing / development")
    @Test
    public void testShared()
        throws Exception
    {
        Path cache = testContext.getTestOutputFolder().toPath();
        
        DatasetFactory df = new DatasetFactory(cache);
        Dataset ds1 = df.load("perseus-el-2.1");
        assertDatasetOk(ds1);
        Dataset ds2 = df.load("perseus-la-2.1");
        assertDatasetOk(ds2);
    }

    @Ignore("Used at times for offline testing / development")
    @Test
    public void testLoadSimple()
        throws Exception
    {
        Path cache = testContext.getTestOutputFolder().toPath();
        
        DatasetFactory df = new DatasetFactory(cache);
        Dataset ds = df.load("germeval2014-de");
        assertDatasetOk(ds);
    }

    @Ignore("Used at times for offline testing / development")
    @Test
    public void testLoadWithExplode()
        throws Exception
    {
        Path cache = testContext.getTestOutputFolder().toPath();
        
        DatasetFactory df = new DatasetFactory(cache);
        Dataset ds = df.load("brown-en-teixml");
        assertDatasetOk(ds);
        
        assertFalse(Files.exists(cache.resolve("brownCorpus-TEI-XML/brown_tei/Corpus.xml")));
    }

    private void assertDatasetOk(Dataset ds)
    {
        
        System.out.printf("Dataset         : %s%n", ds.getName());
        System.out.printf("Data files      : %d%n", ds.getDataFiles().length);
        
        Split split = ds.getDefaultSplit();
        if (split != null) {
            System.out.printf("Training set    : %d%n",
                    split.getTrainingFiles() != null ? split.getTrainingFiles().length : "none");
            System.out.printf("Development set : %d%n",
                    split.getDevelopmentFiles() != null ? split.getDevelopmentFiles().length : "none");
            System.out.printf("Testing set     : %d%n",
                    split.getTestFiles() != null ? split.getTestFiles().length : "none");
        }
        
        assertNotNull("Name not set", ds.getName());
        assertNotNull("Language not set", ds.getLanguage());
        if (split != null) {
            assertNullOrExists(split.getTrainingFiles());
            assertNullOrExists(split.getTestFiles());
            assertNullOrExists(split.getDevelopmentFiles());
        }
        assertNullOrExists(ds.getLicenseFiles());
        assertNotNull(ds.getDataFiles());
        assertTrue(ds.getDataFiles().length > 0);
    }

    private void assertNullOrExists(File... aFiles)
    {
        if (aFiles != null) {
            for (File f : aFiles) {
                assertTrue("File does not exist: [" + f + "]", f.exists());
            }
        }
    }

    @Rule
    public DkproTestContext testContext = new DkproTestContext();
}
