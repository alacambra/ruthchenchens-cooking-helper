package alacambra.cookinghelper.utils;

import alacambra.cookinghelper.entities.Book;
import alacambra.cookinghelper.entities.Category;
import alacambra.cookinghelper.entities.Ingredient;
import alacambra.cookinghelper.entities.Recipe;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;

import static alacambra.cookinghelper.entities.QueryKeys.*;


/**
 * Created by alacambra on 26/12/14.
 */
@Transactional
public class Importer {

    @PersistenceContext
    EntityManager entityManager;

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
                {isbnHeader, bookTitleHeader, bookKeywordsHeader,
                        recipeTitleHeader, pageHeader, ingredientsHeader, recipeCategoryHeader, ratingHeader};
        String data = null;

        try {
            data = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("receptes1.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Reader in = new StringReader(data);
        try {
            for (CSVRecord record : CSVFormat.DEFAULT.withSkipHeaderRecord().parse(in)) {
                Book currentBook = insertBook(record);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Book insertBook(CSVRecord record){
        String isbn = record.get(isbnHeader);
        Book book =
                entityManager.createNamedQuery(getBookByIsbn, Book.class).setParameter("isbn", isbn).getSingleResult();

        if(book != null)
            return book;

        book = new Book();
        book.setTitle(record.get(bookTitleHeader));
        book.setIsbn(record.get(isbnHeader));
        entityManager.persist(book);
        return book;
    }

    Collection<Category> insertCategories(CSVRecord record){
        String data = record.get(recipeCategoryHeader);
        String[] categories = data.split(";");

//        for (int i = 0;)

        return null;
    }

    Collection<Ingredient> insertIngredients(CSVRecord record){

        return null;
    }

    Recipe insertRecipe(CSVRecord record){

        return null;
    }
}
