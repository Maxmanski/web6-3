package controllers;

import at.ac.tuwien.big.we15.lab2.api.Avatar;
import models.User;
import play.api.mvc.Flash;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.authentication;
import play.cache.Cache;

public class Authentication extends Controller {

    public static Result authentication() {
        return ok(authentication.render(play.data.Form.form(Login.class), flash("warning"), flash("error")));
    }

    @Transactional
    public static Result login(){
        session().clear();
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

        if(loginForm.hasErrors()){
            return ok(authentication.render(loginForm, flash("warning"), flash("error")));
        }

        Login login = loginForm.get();
        User usr = User.authenticate(login.username, login.password);

        // Generate a unique ID
        String uuid=session("uuid");
        if(uuid==null) {
            uuid=java.util.UUID.randomUUID().toString();
            session("uuid", uuid);
        }

        if(usr != null){
            session("username", login.username);
            return redirect(controllers.routes.Overview.jeopardy());
        }else {
            return badRequest(authentication.render(loginForm, flash("warning"), Messages.get("error_invalidLogin")));
        }
    }

    public static Result logout(){
        session().clear();
        Cache.remove("game");
        return ok(authentication.render(Form.form(Login.class), flash("warning"), flash("error")));
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
