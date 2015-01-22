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

        Join join = null;
        Predicate predicate = null;

        if(searchBy == SearchBy.INGREDIENT) {

            join = recipes.join(recipeMetamodel.getSet("ingredients", Ingredient.class));
            predicate = cb.equal((Expression<Long>) join.get(ingredientMetamodel.getSingularAttribute("id")), id);

        } else if(searchBy == SearchBy.BOOK){

            join = recipes.join(recipeMetamodel.getSingularAttribute("book", Book.class));
            predicate = cb.equal((Expression<Long>) join.get(bookMetamodel.getSingularAttribute("id")), id);

        } else if(searchBy == SearchBy.CATEGORY){

            join = recipes.join(recipeMetamodel.getSet("categories", Category.class));
            predicate = cb.equal((Expression<Long>)join.get(categoryMetamodel.getSingularAttribute("id")), id);

        }

        CriteriaQuery<Recipe> select = count ? cq.select(cb.countDistinct(recipes)) : cq.select(recipes).distinct(true);
        return select.where(predicate);
    }

    public CriteriaQuery getCriteria(boolean count, String searchString){

        searchString = searchString.trim().toLowerCase();

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

        Join<Recipe, Ingredient> ingredientJoin = recipes.join(recipeMetamodel.getSet("ingredients", Ingredient.class), JoinType.LEFT);
        Predicate ingredientPred = cb.like(
                (Expression<String>)ingredientJoin.get(ingredientMetamodel.getSingularAttribute("name")),
                "%" + searchString + "%");

        Join<Recipe, Book> bookJoin = recipes.join(recipeMetamodel.getSingularAttribute("book", Book.class), JoinType.LEFT);
        Predicate recipePred = cb.like((Expression<String>)bookJoin.get(bookMetamodel.getSingularAttribute("title")),
                "%" + searchString + "%");

        Join<Recipe, Category> categoryJoin = recipes.join(recipeMetamodel.getSet("categories", Category.class), JoinType.LEFT);
        Predicate categoryPred = cb.like(
                (Expression<String>)categoryJoin.get(categoryMetamodel.getSingularAttribute("name")),
                "%" + searchString + "%");

        CriteriaQuery<Recipe> select = count ? cq.select(cb.countDistinct(recipes)) : cq.select(recipes).distinct(true);


        return select.where(
                cb.or(
                        ingredientPred, recipePred, categoryPred,
                        cb.like(
                                (Expression<String>) recipes.get(recipeMetamodel.getSingularAttribute("title")),
                                "%" + searchString + "%"
                        )
                )
        );
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
