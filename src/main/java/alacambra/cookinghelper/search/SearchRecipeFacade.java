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

/**
 *
 * @author alacambra
 */
@Stateless
public class SearchRecipeFacade extends AbstractFacade<Recipe> {
    @PersistenceContext(unitName = "Cookinghelper")
    private EntityManager em;

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
            }
        }

        if(!criteriaIsEmpty(cats)) {
            for (int i = 0; i<catOr.length && !cats[i].equals("") ;i++){
                Join<Recipe, Category> cat = recipes.join(recipeMetamodel.getSet("categories", Category.class));
                predicates.add(cb.like((Expression<String>) cat.get(categoryMetamodel.getSingularAttribute("name")), cats[i].trim()));
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

        if(!criteriaIsEmpty(ingrs)) {

            for (int i = 0; i<ingOr.length && !ingrs[i].equals("") ;i++){
                Join<Recipe, Ingredient> ing = recipes.join(recipeMetamodel.getSet("ingredients", Ingredient.class));
                predicates.add(cb.notLike((Expression<String>) ing.get(ingredientMetamodel.getSingularAttribute("name")), ingrs[i].trim()));
            }
        }

        if(!criteriaIsEmpty(cats)) {
            Join<Recipe, Category> cat = recipes.join(recipeMetamodel.getSet("categories", Category.class));
            for (int i = 0; i<catOr.length && !cats[i].equals("") ;i++){
                catOr[i] = cb.like((Expression<String>) cat.get(categoryMetamodel.getSingularAttribute("name")), "%" + cats[i].trim() + "%");
            }
            predicates.add(cb.or(catOr));
        }

        return predicates;
    }

    private Boolean criteriaIsEmpty(String[] criteriaParams){
        return criteriaParams.length == 0 || criteriaParams[0].equals("");
    }
    
}
