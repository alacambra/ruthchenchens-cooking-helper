package alacambra.cookinghelper.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by alacambra on 26/12/14.
 */
public class Importer {

    final String isbnHeader = "ISBN";
    final String bookTitleHeader = "Titel";
    final String bookKeywordsHeader = "HauptschlagwortBuch";
    final String recipeTitleHeader = "Rezept";
    final String pageHeader = "Seite";
    final String ingredientsHeader = "Basiszutaten";
    final String recipeCategoryHeader = "Kategorie";
    final String ratingHeader = "Bewertung";

    String data;

    public void loadLines(){

        String[] headers =
                {"ISBN", "Titel", "HauptschlagwortBuch", "Rezept", "Seite", "Basiszutaten", "Kategorie", "Bewertung"};
        String data = null;

        try {
            data = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("receptes1.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Reader in = new StringReader(data);
        try {
            for (CSVRecord record : CSVFormat.DEFAULT.withSkipHeaderRecord().parse(in)) {
                for (String field : record) {
                    System.out.print("\"" + field + "\", ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
