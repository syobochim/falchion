package net.unit8.falchion;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author syobochim
 */
public class ContainerTest {

    @Test
    public void createClasspathWithBaseDirAndAplVersion() throws Exception {
        Container sut = new Container(1);
        String basePath = new File(ContainerTest.class.getClassLoader().getResource("containerTestResources").getPath())
                .getAbsolutePath();
        assertThat(sut.createClasspath(basePath, "0.1.0"), is(basePath + File.separator + "application-0.1.0"));
        assertThat(sut.createClasspath(basePath, "0.1.1"),
                is(basePath + File.separator + "subDir" + File.separator + "application-0.1.1"));
    }

    @Test
    public void returnBaseDirWhenFileNotFound() throws Exception {
        Container sut = new Container(1);
        String basePath = new File(ContainerTest.class.getClassLoader().getResource("containerTestResources").getPath())
                .getAbsolutePath();
        assertThat(sut.createClasspath(basePath, "0.2.0"), is(basePath));
    }
}