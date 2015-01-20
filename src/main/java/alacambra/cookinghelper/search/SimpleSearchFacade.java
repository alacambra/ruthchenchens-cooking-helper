package alacambra.cookinghelper.search;

import alacambra.cookinghelper.AbstractFacade;
import alacambra.cookinghelper.book.Book;
import alacambra.cookinghelper.category.Category;
import alacambra.cookinghelper.ingredient.Ingredient;
import alacambra.cookinghelper.recipe.Recipe;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.util.logging.Logger;

/**
 * Created by alacambra on 1/20/15.
 */
@Stateless
public class SimpleSearchFacade extends AbstractFacade<Recipe> {

    @PersistenceContext(unitName = "Cookinghelper")
    private EntityManager em;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public SimpleSearchFacade() {
        super(Recipe.class);
    }


    public CriteriaQuery getCriteria(boolean count, SearchBy searchBy, Long id){
        return null;
    }

    public CriteriaQuery getCriteria(boolean count, String searchString){

        EntityType<Recipe> recipeMetamodel = em.getMetamodel().entity(Recipe.class);
        EntityType<Ingredient> ingredientMetamodel = em.getMetamodel().entity(Ingredient.class);
        EntityType<Category> categoryMetamodel = em.getMetamodel().entity(Category.class);
        EntityType<Book> bookMetamodel = em.getMetamodel().entity(Book.class);

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery cq;
        if(count){
            cq = cb.createQuery(Long.class);
        }else{
            cq = cb.createQuery(Recipe.class);
        }

        cb.createQuery(Recipe.class);
        Root<Recipe> recipes = cq.from(Recipe.class);

        Join<Recipe, Ingredient> ingredientJoin = recipes.join(recipeMetamodel.getSet("ingredients", Ingredient.class));
        Predicate ingredientPred = cb.like(
                (Expression<String>)ingredientJoin.get(ingredientMetamodel.getSingularAttribute("name")),
                "%" + searchString.trim() + "%");

        Join<Recipe, Book> bookJoin = recipes.join(recipeMetamodel.getSingularAttribute("book", Book.class));
        Predicate recipePred = cb.like((Expression<String>)bookJoin.get(bookMetamodel.getSingularAttribute("title")),
                "%" + searchString.trim() + "%");

        Join<Recipe, Category> categoryJoin = recipes.join(recipeMetamodel.getSet("categories", Category.class));
        Predicate categoryPred = cb.like(
                (Expression<String>)categoryJoin.get(categoryMetamodel.getSingularAttribute("name")),
                "%" + searchString.trim() + "%");

        CriteriaQuery<Recipe> select = count ? cq.select(cb.countDistinct(recipes)) : cq.select(recipes);


        return select.where(
                cb.or(
                        ingredientPred, recipePred, categoryPred,
                        cb.like(
                                (Expression<String>) recipes.get(recipeMetamodel.getSingularAttribute("title")),
                                "%" + searchString.trim() + "%"
                        )
                )
        );
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
