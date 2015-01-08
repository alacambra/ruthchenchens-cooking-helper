package alacambra.cookinghelper.utils;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

@RunWith(JUnit4.class)
public class ImporterIT extends TestCase {

    EntityManager em;
    EntityTransaction tx;

    @Before
    public void initEM() {
        this.em = Persistence.createEntityManagerFactory("integration-test").createEntityManager();
        this.tx = this.em.getTransaction();
    }

    @Test
    public void testLoadLines() throws Exception {

    }

    public void testInsertBook() throws Exception {

    }
}