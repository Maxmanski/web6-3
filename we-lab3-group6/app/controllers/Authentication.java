package controllers;

import at.ac.tuwien.big.we15.lab2.api.Avatar;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.authentication;
import play.cache.Cache;

public class Authentication extends Controller {

    public static Result authentication() {
        return ok(authentication.render(play.data.Form.form(Login.class)));
    }

    @Transactional
    public static Result login(){
        session().clear();
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

        if(loginForm.hasErrors()){
            System.out.println(loginForm.errors());
            return ok(authentication.render(loginForm));
        }

        Login login = loginForm.get();
        User usr = User.authenticate(login.username, login.password);

        usr = new User("asdf", "asdf", "asdf", "20.04.1993", User.Gender.female, Avatar.ALDRICH_KILLIAN.getId(), "asdf");
        if(Users.getUserByUsername(usr.getUsername()) == null){
            usr.save();
        }

        // Generate a unique ID
        String uuid=session("uuid");
        if(uuid==null) {
            uuid=java.util.UUID.randomUUID().toString();
            session("uuid", uuid);
        }

        if(usr != null){
            session().put("username", usr.getUsername());
            return redirect(controllers.routes.Overview.jeopardy());
        }else {
            flash("error", "Username and/or Password did not match");
            return badRequest(authentication.render(loginForm));
        }
    }

    public static Result logout(){
        session().clear();
        Cache.remove("game");
        return ok(authentication.render(Form.form(Login.class)));
    }

    public static class Login{
        public String username;
        public String password;

        public Login(){
            this(null, null);
        }

        public Login(String username, String password){
            this.username = username;
            this.password = password;
        }
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }
}
