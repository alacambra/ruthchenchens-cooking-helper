package alacambra.cookinghelper.search;

import alacambra.cookinghelper.beans.AbstractFacade;
import alacambra.cookinghelper.entities.Category;
import alacambra.cookinghelper.entities.Ingredient;
import alacambra.cookinghelper.entities.Recipe;
import alacambra.cookinghelper.jsf.util.PaginationHelper;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by alacambra on 1/4/15.
 */
@Named("search")
@SessionScoped
public class SearchRecipeController implements Serializable{

    private PaginationHelper pagination;
    private DataModel items = null;

    @EJB
    private SearchRecipeFacade facade;

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(30) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(
                            new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()})
                    );
                }
            };
        }
        return pagination;
    }

    public void createCriteriaAllORs(){
        String categories;
        String ingredients;
        categories = "";
        ingredients = "k;l;t";
        String[] cats = categories.split(";");
        String[] ingrs = ingredients.split(";");

        EntityType<Recipe> recipeMetamodel = facade.getEntityManager().getMetamodel().entity(Recipe.class);
        EntityType<Ingredient> ingredientMetamodel = facade.getEntityManager().getMetamodel().entity(Ingredient.class);
        EntityType<Category> categoryMetamodel = facade.getEntityManager().getMetamodel().entity(Category.class);


        CriteriaBuilder cb = facade.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Recipe> q = cb.createQuery(Recipe.class);
        Root<Recipe> recipes = q.from(Recipe.class);
        Join<Recipe, Ingredient> ing = recipes.join(recipeMetamodel.getSet("ingredients", Ingredient.class));
        Join<Recipe, Category> cat = recipes.join(recipeMetamodel.getSet("categories", Category.class));

        Predicate[] ingOr = new Predicate[ingrs.length];
        Predicate[] catOr = new Predicate[ingrs.length];

        int totalOrs = ingOr.length == 0 || ingrs[0].equals("") ? 0 : 1;
        totalOrs = catOr.length == 0 || cats[0].equals("") ? totalOrs : totalOrs + 1;

        Predicate[] allOr = new Predicate[totalOrs];
        for (int i = 0; i<ingOr.length && !ingrs[i].equals("") ;i++){
            ingOr[i] = cb.like((Expression<String>) ing.get(ingredientMetamodel.getSingularAttribute("name")), "%" + ingrs[i] + "%");
            allOr[0] = cb.or(ingOr);
        }

        for (int i = 0; i<catOr.length && !cats[i].equals("") ;i++){
            catOr[i] = cb.like((Expression<String>) cat.get(categoryMetamodel.getSingularAttribute("name")), "%" + cats[i] + "%");
            allOr[1] = cb.or(catOr);
        }

        q.select(recipes).where(allOr);

        System.out.println(facade.getEntityManager().createQuery(q).getResultList());
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
