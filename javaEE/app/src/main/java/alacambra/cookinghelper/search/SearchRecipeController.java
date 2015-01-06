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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

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
        String[] cats = categories.toLowerCase().split(";");
        String[] ingrs = ingredients.toLowerCase().split(";");

        EntityType<Recipe> recipeMetamodel = facade.getEntityManager().getMetamodel().entity(Recipe.class);
        EntityType<Ingredient> ingredientMetamodel = facade.getEntityManager().getMetamodel().entity(Ingredient.class);
        EntityType<Category> categoryMetamodel = facade.getEntityManager().getMetamodel().entity(Category.class);


        CriteriaBuilder cb = facade.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Recipe> q = cb.createQuery(Recipe.class);
        Root<Recipe> recipes = q.from(Recipe.class);

        Predicate[] ingOr = new Predicate[ingrs.length];
        Predicate[] catOr = new Predicate[cats.length];
        Set<Predicate> predicates = new HashSet<>();

        if(!criteriaIsEmpty(ingrs)) {
            Join<Recipe, Ingredient> ing = recipes.join(recipeMetamodel.getSet("ingredients", Ingredient.class));
            for (int i = 0; i<ingOr.length && !ingrs[i].equals("") ;i++){
                ingOr[i] = cb.like((Expression<String>) ing.get(ingredientMetamodel.getSingularAttribute("name")), "%" + ingrs[i].trim() + "%");
            }
            predicates.add(cb.or(ingOr));
        }

        if(!criteriaIsEmpty(cats)) {
            Join<Recipe, Category> cat = recipes.join(recipeMetamodel.getSet("categories", Category.class));
            for (int i = 0; i<catOr.length && !cats[i].equals("") ;i++){
                catOr[i] = cb.like((Expression<String>) cat.get(categoryMetamodel.getSingularAttribute("name")), "%" + cats[i].trim() + "%");
            }
            predicates.add(cb.or(catOr));
        }

        Predicate[] allOr = new Predicate[predicates.size()];

        int i = 0;
        for(Predicate p : predicates){
            allOr[i] = p;
            i++;
        }

        q.select(recipes).where(allOr);

        this.recipes = facade.getEntityManager().createQuery(q).getResultList();
    }

    public Integer getTotalRecipesFound(){
        if (recipes == null) return 0;

        return recipes.size();
    }

    public void createCriteriaAllANDs(){
        String[] cats = categories.toLowerCase().split(";");
        String[] ingrs = ingredients.toLowerCase().split(";");

        EntityType<Recipe> recipeMetamodel = facade.getEntityManager().getMetamodel().entity(Recipe.class);
        EntityType<Ingredient> ingredientMetamodel = facade.getEntityManager().getMetamodel().entity(Ingredient.class);
        EntityType<Category> categoryMetamodel = facade.getEntityManager().getMetamodel().entity(Category.class);


        CriteriaBuilder cb = facade.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Recipe> q = cb.createQuery(Recipe.class);
        Root<Recipe> recipes = q.from(Recipe.class);

        Predicate[] ingOr = new Predicate[ingrs.length];
        Predicate[] catOr = new Predicate[cats.length];
        Set<Predicate> predicates = new HashSet<>();

        if(!criteriaIsEmpty(ingrs)) {

            for (int i = 0; i<ingOr.length && !ingrs[i].equals("") ;i++){
                Join<Recipe, Ingredient> ing = recipes.join(recipeMetamodel.getSet("ingredients", Ingredient.class));
                predicates.add(cb.like((Expression<String>) ing.get(ingredientMetamodel.getSingularAttribute("name")), "%" + ingrs[i].trim() + "%"));
            }
        }

        if(!criteriaIsEmpty(cats)) {
            for (int i = 0; i<catOr.length && !cats[i].equals("") ;i++){
                Join<Recipe, Category> cat = recipes.join(recipeMetamodel.getSet("categories", Category.class));
                predicates.add(cb.like((Expression<String>) cat.get(categoryMetamodel.getSingularAttribute("name")), "%" + cats[i].trim() + "%"));
            }
        }

        Predicate[] allAND = new Predicate[predicates.size()];

        int i = 0;
        for(Predicate p : predicates){
            allAND[i] = p;
            i++;
        }

        q.select(recipes).where(allAND);

        this.recipes = facade.getEntityManager().createQuery(q).getResultList();
    }

    private Boolean criteriaIsEmpty(String[] criteriaParams){
        return criteriaParams.length == 0 || criteriaParams[0].equals("");
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
