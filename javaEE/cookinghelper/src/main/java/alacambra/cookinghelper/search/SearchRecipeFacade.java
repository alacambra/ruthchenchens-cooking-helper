/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alacambra.cookinghelper.search;

import alacambra.cookinghelper.AbstractFacade;
import alacambra.cookinghelper.book.Book;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alacambra
 */
@Stateless
public class SearchRecipeFacade extends AbstractFacade<Book> {
    @PersistenceContext(unitName = "Cookinghelper")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public SearchRecipeFacade() {
        super(Book.class);
    }
    
}
