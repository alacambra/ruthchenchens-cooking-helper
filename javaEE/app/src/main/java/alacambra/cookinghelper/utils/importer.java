package alacambra.cookinghelper.utils;

import alacambra.cookinghelper.entities.Book;
import alacambra.cookinghelper.entities.Category;
import alacambra.cookinghelper.entities.Ingredient;
import alacambra.cookinghelper.entities.Recipe;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static alacambra.cookinghelper.entities.QueryKeys.*;


/**
 * Created by alacambra on 26/12/14.
 */
@Stateless
public class Importer{

    @PersistenceContext
    EntityManager em;

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
            for (CSVRecord record : CSVFormat.DEFAULT.withHeader(headers).withSkipHeaderRecord().parse(in)) {
                Book currentBook = insertBook(record);
                Set<Category> categories = insertCategories(record);
                Set<Ingredient> ingredients = insertIngredients(record);
                insertRecipe(record, currentBook, ingredients, categories);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    <Q> Q getEntittyOrNull(TypedQuery<Q> query){
        try{
            return query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    Book insertBook(CSVRecord record){
        String isbn = record.get(isbnHeader);
        Book book = getEntittyOrNull(em.createNamedQuery(getBookByIsbn, Book.class).setParameter("isbn", isbn));

        if(book != null)
            return book;

        book = new Book();
        book.setTitle(record.get(bookTitleHeader));
        book.setIsbn(record.get(isbnHeader));
        em.persist(book);
        return book;
    }

    Set<Category> insertCategories(CSVRecord record){
        String data = record.get(recipeCategoryHeader);
        String[] categories = data.split(";");
        Set<Category> cats = new HashSet<>();

        for (int i = 0; i < categories.length; i++){

            String catName = categories[i].trim().toLowerCase();

            Category category = getEntittyOrNull(
                    em.createNamedQuery(getCategoryByName, Category.class).setParameter("name", catName));

            if(category == null) {
                category = new Category();
                category.setName(catName);
                em.persist(category);
            }

            cats.add(category);
        }

        return cats;
    }

    Set<Ingredient> insertIngredients(CSVRecord record){

        String data = record.get(ingredientsHeader);
        String[] ingredients = data.split(";");
        Set<Ingredient> ings = new HashSet<>();

        for (int i = 0; i < ingredients.length; i++){
            String ingredientName = ingredients[i].trim().toLowerCase();
            Ingredient ingredient = getEntittyOrNull(
                    em.createNamedQuery(getIngredientByName, Ingredient.class).setParameter("name", ingredientName));

            if(ingredient == null) {
                ingredient = new Ingredient();
                ingredient.setName(ingredientName);
                em.persist(ingredient);
            }

            ings.add(ingredient);
        }

        return ings;
    }

    Recipe insertRecipe(CSVRecord record, Book book, Set<Ingredient> ingredients, Set<Category> categories){

        Recipe recipe = new Recipe();
        recipe.setTitle(record.get(recipeTitleHeader));
        recipe.setBook(book);
        recipe.setCategories(categories);
        recipe.setIngredients(ingredients);
        recipe.setPage(Integer.parseInt(record.get(pageHeader).length() > 0 ? record.get(pageHeader) : "0"));
        recipe.setRating(Integer.parseInt(record.get(ratingHeader).length() > 0 ? record.get(ratingHeader) : "-1"));

        em.persist(recipe);

        return recipe;
    }
}
