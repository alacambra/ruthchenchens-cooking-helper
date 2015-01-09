package alacambra.cookinghelper.search;

import alacambra.cookinghelper.category.Category;
import alacambra.cookinghelper.ingredient.Ingredient;
import alacambra.cookinghelper.recipe.Recipe;
import alacambra.cookinghelper.jsf.PaginationHelper;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.io.Serializable;
import java.util.*;

/**
 * Created by alacambra on 1/4/15.
 */
@Named("search")
@SessionScoped
public class SearchRecipeController implements Serializable{

    private PaginationHelper pagination;
    private DataModel items = null;
    private String ingredients;
    private String categories;
    private List<Recipe> recipes;
    private int lastCriteriaCount = -1;

    @EJB
    private SearchRecipeFacade facade;

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(3000) {

                @Override
                public int getItemsCount() {
                    return lastCriteriaCount;
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(new ArrayList());
                }

                @Override
                public DataModel createPageDataModel(CriteriaQuery cq) {
                    return new ListDataModel(getFacade().findRange(
                            new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, cq)
                    );
                }
            };
        }
        return pagination;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public void createCriteriaAllORs(){
        items = getPagination().createPageDataModel(facade.getCriteria(false, SearchType.OR, categories, ingredients));
        lastCriteriaCount = getFacade().getTotal(false, SearchType.OR, categories, ingredients);
    }

    public void createCriteriaAllANDs(){
        items = getPagination().createPageDataModel(facade.getCriteria(false, SearchType.AND, categories, ingredients));
        lastCriteriaCount = getFacade().getTotal(true, SearchType.AND, categories, ingredients);
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    public SearchRecipeFacade getFacade() {
        return facade;
    }
}
