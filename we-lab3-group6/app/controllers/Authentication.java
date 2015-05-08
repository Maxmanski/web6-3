package controllers;

import models.User;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.authentication;

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

        if(usr != null){
            session().put("username", usr.getUsername());
            return redirect(controllers.routes.Overview.jeopardy());
        }else {
            loginForm.reject("Authentication Failed", "Username and/or Password did not match");
            return badRequest(authentication.render(loginForm));
        }
    }

    public static Result logout(){
        session().clear();
        return ok(authentication.render(Form.form(Login.class)));
    }

    public static class Login{
        public String username;
        public String password;
    }
}
