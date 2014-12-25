/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package alacambra.cookinghelper.entities.jsf;

import alacambra.cookinghelper.entities.Ingredient;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alacambra
 */
@Stateless
public class IngredientFacade extends AbstractFacade<Ingredient> {
    @PersistenceContext(unitName = "ExampleDS")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IngredientFacade() {
        super(Ingredient.class);
    }
    
}
