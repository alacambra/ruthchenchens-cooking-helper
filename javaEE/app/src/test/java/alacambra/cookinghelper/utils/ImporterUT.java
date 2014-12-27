package alacambra.cookinghelper.utils;

import junit.framework.TestCase;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

@RunWith(JUnit4.class)
public class ImporterUT extends TestCase {

    Importer importer;

    @Before
    public void init(){
        importer = new Importer();
    }

    @Test
    public void testLoadLines() throws Exception {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("receptes1.csv");
        String data = importer.getData(stream);
        String[] lines = importer.loadLines(data);
        assertEquals(259, lines.length);

        Reader in = new StringReader(IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("receptes1.csv")));
        for (CSVRecord record : CSVFormat.DEFAULT.parse(in)) {
            for (String field : record) {
                System.out.print("\"" + field + "\", ");
            }
            System.out.println();
        }
    }
}