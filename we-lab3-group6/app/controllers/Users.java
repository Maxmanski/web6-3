package controllers;

import models.User;
import play.db.jpa.JPA;
import play.mvc.Controller;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

/**
 * Created by maxmanski on 08.05.15.
 */
public class Users extends Controller{



    public static Collection<User> getAllUsers(){
        EntityManager em = JPA.em();
        String queryString = ""; // TODO
        TypedQuery<User> query = em.createQuery(queryString, User.class);
        return (Collection<User>)query.getResultList();
    }

    public static User getUserByUsername(String username){
        EntityManager em = JPA.em();
        return em.find(User.class, username);
    }
}
