package alacambra.cookinghelper.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by alacambra on 26/12/14.
 */
public class Importer {

    String data;

    public void getData(){

        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("receptes1.csv");
        try{
            StringWriter writer = new StringWriter();
            IOUtils.copy(stream, writer);
            data = writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] loadLines(String data){
        return data.split("");
    }
}
