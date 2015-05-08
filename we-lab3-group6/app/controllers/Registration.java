package controllers;

import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.registration;

public class Registration extends Controller {

    public static Result registration() {
        Form<models.User> userForm = Form.form(models.User.class);
        return ok(registration.render(Messages.get("label_titleRegistration"), userForm));
    }

    public static Result register(){

        DynamicForm form = Form.form().bindFromRequest();

        String firstname, lastname, birthday, sex, avatar, username, password;

        firstname = form.get("firstname");
        lastname = form.get("lastname");
        birthday = form.get("birthdate");
        sex = form.get("gender");
        avatar = form.get("avatar");
        username = form.get("username");
        password = form.get("password");

        if(sex.equals("m")){
            sex = "male";
        }else{
            sex = "female";
        }

        boolean valid = true;

        if((username == null) || (username.length() < 4) || (username.length() > 8)){
            valid = false;

        }else if((password == null) || (password.length() < 4) || (password.length() > 8)){
            valid = false;

        }else if(firstname.equals("max")){
            valid = false;
        }

        if(valid){
            User usr = new User(username, firstname, lastname, birthday, User.Gender.valueOf(sex), avatar, password);
            usr.save();
            System.out.println(usr);
            User found = User.find.where().eq("username", username).findUnique();
            System.out.println(found);
            return redirect(controllers.routes.Authentication.authentication());

        }else{
            return redirect(controllers.routes.Registration.registration());

        }
    }
}
