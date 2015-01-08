package alacambra.cookinghelper.recipe;

import alacambra.cookinghelper.QueryKeys;
import alacambra.cookinghelper.book.Book;
import alacambra.cookinghelper.category.Category;
import alacambra.cookinghelper.ingredient.Ingredient;

import java.util.Set;
import javax.persistence.*;

/**
 * Created by alacambra on 23/12/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = QueryKeys.getRecipeByBookAndTitle,
                query = "SELECT r from Recipe as r where r.title like :title and r.book = :book")
})
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String title;

    @ManyToOne
    Book book;
    Integer rating;
    Integer page;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Ingredient> ingredients;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Category> categories;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Set<Ingredient> getIngredient() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredient) {
        this.ingredients = ingredient;
    }

    public Set<Category> getCategory() {
        return categories;
    }

    public void setCategories(Set<Category> category) {
        this.categories = category;
    }

    @Override
    public String toString() {
        return title;
    }
}
