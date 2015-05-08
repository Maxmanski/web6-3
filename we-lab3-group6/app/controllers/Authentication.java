package controllers;

import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.authentication;

public class Authentication extends Controller {

    public static Result authentication() {
        return ok(authentication.render(Messages.get("label_login"), play.data.Form.form(Login.class)));
    }

    @Transactional
    public static Result login(){
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

        if(loginForm.hasErrors()){
            System.out.println(loginForm.errors());
            return ok(authentication.render(Messages.get("label_login"), loginForm));
        }

        User usr = Users.getUserByUsername(loginForm.get().username);

        System.out.println(usr);

        System.out.println(loginForm.get().username);
        System.out.println(loginForm.get().password);

        session().clear();
        session().put("username", loginForm.get().username);
        return redirect(controllers.routes.Overview.jeopardy());
    }

    public static class Login{
        public String username;
        public String password;
    }
}
