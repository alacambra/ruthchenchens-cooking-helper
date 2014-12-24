/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package alacambra.cookinghelper.entities.jsf;

import alacambra.cookinghelper.entities.IngredientCluster;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alacambra
 */
@Stateless
public class IngredientClusterFacade extends AbstractFacade<IngredientCluster> {
    @PersistenceContext(unitName = "alacambra.cookinghelper_app_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IngredientClusterFacade() {
        super(IngredientCluster.class);
    }
    
}
