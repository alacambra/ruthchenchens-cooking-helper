/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alacambra.cookinghelper.recipe;

import alacambra.cookinghelper.AbstractFacade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author alacambra
 */
@Stateless
@Transactional
public class RecipeFacade extends AbstractFacade<Recipe> {
    @PersistenceContext(unitName = "Cookinghelper")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecipeFacade() {
        super(Recipe.class);
    }
    
}
