/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alacambra.cookinghelper.search;

import alacambra.cookinghelper.AbstractFacade;
import alacambra.cookinghelper.book.Book;
import alacambra.cookinghelper.category.Category;
import alacambra.cookinghelper.ingredient.Ingredient;
import alacambra.cookinghelper.recipe.Recipe;

import javax.ejb.Stateless;
import javax.faces.model.DataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author alacambra
 */
@Stateless
public class SearchRecipeFacade extends AbstractFacade<Recipe> {
    @PersistenceContext(unitName = "Cookinghelper")
    private EntityManager em;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public SearchRecipeFacade() {
        super(Recipe.class);
    }

    public Integer getTotal(boolean count, SearchType searchType, String categories, String ingredients){
        javax.persistence.Query q = em.createQuery(getCriteria(true, SearchType.OR, categories, ingredients));
        return ((Long) q.getSingleResult()).intValue();
    }

    public CriteriaQuery getCriteria(boolean count, SearchType searchType, String categories, String ingredients){

        if(searchType == SearchType.NOT){
            return getNotCriteria(count, searchType, categories, ingredients);
        }


        String[] cats = categories.toLowerCase().split(";");
        String[] ingrs = ingredients.toLowerCase().split(";");

        EntityType<Recipe> recipeMetamodel = em.getMetamodel().entity(Recipe.class);
        EntityType<Ingredient> ingredientMetamodel = em.getMetamodel().entity(Ingredient.class);
        EntityType<Category> categoryMetamodel = em.getMetamodel().entity(Category.class);


        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery cq;
        if(count){
            cq = cb.createQuery(Long.class);
        }else{
            cq = cb.createQuery(Recipe.class);
        }

        cb.createQuery(Recipe.class);
        Root<Recipe> recipes = cq.from(Recipe.class);

        Set<Predicate> predicates = null;

        if(searchType == SearchType.OR) {
            predicates = getOrPredicates(ingrs, cats, recipes, recipeMetamodel, ingredientMetamodel, categoryMetamodel, cb);
        } else if(searchType == SearchType.AND){
            predicates = getAndPredicates(ingrs, cats, recipes, recipeMetamodel, ingredientMetamodel, categoryMetamodel, cb);
        } else if(searchType == SearchType.NOT){
            predicates = getNotPredicates(ingrs, cats, recipes, recipeMetamodel, ingredientMetamodel, categoryMetamodel, cb);
        }

        Predicate[] allPredicates = new Predicate[predicates.size()];

        int i = 0;
        for(Predicate p : predicates){
            allPredicates[i] = p;
            i++;
        }

        if(count){
            cq.select(cb.countDistinct(recipes)).where(allPredicates);
        } else {
            cq.select(recipes).where(allPredicates);
        }

        return cq;
    }

    public CriteriaQuery getNotCriteria(boolean count, SearchType searchType, String categories, String ingredients){

        String[] cats = categories.toLowerCase().split(";");
        String[] ingrs = ingredients.toLowerCase().split(";");

        EntityType<Recipe> recipeMetamodel = em.getMetamodel().entity(Recipe.class);
        EntityType<Ingredient> ingredientMetamodel = em.getMetamodel().entity(Ingredient.class);
        EntityType<Category> categoryMetamodel = em.getMetamodel().entity(Category.class);


        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery cq;
        if(count){
            cq = cb.createQuery(Long.class);
        }else{
            cq = cb.createQuery(Recipe.class);
        }

        Subquery<Recipe> recipesWith = cq.subquery(Recipe.class);
        cb.createQuery(Recipe.class);
        Root<Recipe> recipes = cq.from(Recipe.class);
        Root<Recipe> fromRecipe = recipesWith.from(Recipe.class);
        Path<Object> path = fromRecipe.get("id");

        Set<Predicate> subPredicates = new HashSet<>();
        Predicate[] ingNot = new Predicate[ingrs.length];

        if(!criteriaIsEmpty(ingrs)) {
            Join<Recipe, Ingredient> ing = recipes.join(recipeMetamodel.getSet("ingredients", Ingredient.class));
            for (int i = 0; i<ingNot.length && !ingrs[i].equals("") ;i++){
                ingNot[i] = cb.like((Expression<String>) ing.get(ingredientMetamodel.getSingularAttribute("name")), ingrs[i].trim());
                logger.info("NOT:ING:" + ingrs[i].trim());
            }
            subPredicates.add(cb.or(ingNot));
        }

        recipesWith.select(recipes);
        Predicate[] subPredicatesArr = new Predicate[subPredicates.size()];

        int j = 0;
        for(Predicate p : subPredicates){
            subPredicatesArr[j] = p;
            j++;
        }
        cq.select(recipes).where(subPredicatesArr);
        Predicate p = fromRecipe.get(recipeMetamodel.getSingularAttribute("id", Integer.class)).in(recipesWith).not();

        if(count){
            cq.select(cb.countDistinct(recipes)).where(cb.not(p));
        } else {
            cq.select(recipes).where(p);
        }

        return cq;
    }

    private Set<Predicate> getOrPredicates(
            String[] ingrs,
            String[] cats,
            Root<Recipe> recipes,
            EntityType<Recipe> recipeMetamodel,
            EntityType<Ingredient> ingredientMetamodel,
            EntityType<Category> categoryMetamodel,
            CriteriaBuilder cb){

        Set<Predicate> predicates = new HashSet<>();
        Predicate[] ingOr = new Predicate[ingrs.length];
        Predicate[] catOr = new Predicate[cats.length];

        if(!criteriaIsEmpty(ingrs)) {
            Join<Recipe, Ingredient> ing = recipes.join(recipeMetamodel.getSet("ingredients", Ingredient.class));
            for (int i = 0; i<ingOr.length && !ingrs[i].equals("") ;i++){
                ingOr[i] = cb.like((Expression<String>) ing.get(ingredientMetamodel.getSingularAttribute("name")), ingrs[i].trim());
                logger.info("OR:ING:" + ingrs[i].trim());
            }
            predicates.add(cb.or(ingOr));
        }

        if(!criteriaIsEmpty(cats)) {
            Join<Recipe, Category> cat = recipes.join(recipeMetamodel.getSet("categories", Category.class));
            for (int i = 0; i<catOr.length && !cats[i].equals("") ;i++){
                catOr[i] = cb.like((Expression<String>) cat.get(categoryMetamodel.getSingularAttribute("name")), cats[i].trim());
                logger.info("OR:CAT:" + cats[i].trim());
            }
            predicates.add(cb.or(catOr));
        }

        return predicates;
    }

    private Set<Predicate> getAndPredicates(
            String[] ingrs,
            String[] cats,
            Root<Recipe> recipes,
            EntityType<Recipe> recipeMetamodel,
            EntityType<Ingredient> ingredientMetamodel,
            EntityType<Category> categoryMetamodel,
            CriteriaBuilder cb){

        Set<Predicate> predicates = new HashSet<>();
        Predicate[] ingOr = new Predicate[ingrs.length];
        Predicate[] catOr = new Predicate[cats.length];

        if(!criteriaIsEmpty(ingrs)) {

            for (int i = 0; i<ingOr.length && !ingrs[i].equals("") ;i++){
                Join<Recipe, Ingredient> ing = recipes.join(recipeMetamodel.getSet("ingredients", Ingredient.class));
                predicates.add(cb.like((Expression<String>) ing.get(ingredientMetamodel.getSingularAttribute("name")), ingrs[i].trim()));
                logger.info("And:ING:" + ingrs[i].trim());
            }
        }

        if(!criteriaIsEmpty(cats)) {
            for (int i = 0; i<catOr.length && !cats[i].equals("") ;i++){
                Join<Recipe, Category> cat = recipes.join(recipeMetamodel.getSet("categories", Category.class));
                predicates.add(cb.like((Expression<String>) cat.get(categoryMetamodel.getSingularAttribute("name")), cats[i].trim()));
                logger.info("And:CAT:" + cats[i].trim());

            }
        }

        return predicates;
    }

    private Set<Predicate> getNotPredicates(
            String[] ingrs,
            String[] cats,
            Root<Recipe> recipes,
            EntityType<Recipe> recipeMetamodel,
            EntityType<Ingredient> ingredientMetamodel,
            EntityType<Category> categoryMetamodel,
            CriteriaBuilder cb){

        Set<Predicate> predicates = new HashSet<>();
        Predicate[] ingOr = new Predicate[ingrs.length];
        Predicate[] catOr = new Predicate[cats.length];

//        Subquery<Recipe> subquery = criteriaQuery.subquery(PROJECT.class);
//        Root fromProject = subquery.from(PROJECT.class);
//        subquery.select(fromProject.get("requiredColumnName")); // field to map with main-query
//        subquery.where(criteriaBuilder.equal("name",name_value));
//        subquery.where(criteriaBuilder.equal("id",id_value));

        if(!criteriaIsEmpty(ingrs)) {

            for (int i = 0; i<ingOr.length && !ingrs[i].equals("") ;i++){
                Join<Recipe, Ingredient> ing = recipes.join(recipeMetamodel.getSet("ingredients", Ingredient.class));
                predicates.add(cb.notLike((Expression<String>) ing.get(ingredientMetamodel.getSingularAttribute("name")), ingrs[i].trim()));
                logger.info("NOT:ING:" + ingrs[i].trim());
            }
        }

        if(!criteriaIsEmpty(cats)) {
            Join<Recipe, Category> cat = recipes.join(recipeMetamodel.getSet("categories", Category.class));
            for (int i = 0; i<catOr.length && !cats[i].equals("") ;i++){
                catOr[i] = cb.like((Expression<String>) cat.get(categoryMetamodel.getSingularAttribute("name")), cats[i].trim());
                logger.info("OR:CAT:" + cats[i].trim());
            }
            predicates.add(cb.or(catOr));
        }

        return predicates;
    }

    private Boolean criteriaIsEmpty(String[] criteriaParams){
        return criteriaParams.length == 0 || criteriaParams[0].equals("");
    }

}
