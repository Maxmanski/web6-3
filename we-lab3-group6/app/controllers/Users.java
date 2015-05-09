package controllers;

import models.User;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

/**
 * Created by maxmanski on 08.05.15.
 */
public class Users extends Controller{

    @Transactional
    public static Collection<User> getAllUsers(){
        EntityManager em = JPA.em();
        String queryString = "SELECT u FROM User u";
        TypedQuery<User> query = em.createQuery(queryString, User.class);
        return (Collection<User>)query.getResultList();
    }

    @Transactional
    public static User getUserByUsername(String username){
        EntityManager em = JPA.em();
        return User.find.where().eq("username", username).findUnique();
    }

    @Transactional
    public static Result listAll(){
        for(User usr: getAllUsers()){
            System.out.println("READ USER: " + usr);
        }
        return ok();
    }
}
