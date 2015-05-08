package controllers;

import models.User;
import play.api.data.validation.ValidationError;
import play.data.DynamicForm;
import play.data.Form;
import play.data.format.Formatters;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.registration;

import java.text.ParseException;
import java.util.Locale;

public class Registration extends Controller {

    public static Result registration() {
        Form<models.User> userForm = Form.form(models.User.class);
        return ok(registration.render(userForm));
    }

    public static Result register(){
        Form<User> form = Form.form(User.class).bindFromRequest();

        if(form.hasErrors()){
            for(String str: form.errors().keySet()){
                System.out.println(form.errors().get(str));
            }

            // there were errors in the form
            return badRequest(registration.render(form));

        }else if(form.get().validate() != null){

            // the specified user was not valid
            for(play.data.validation.ValidationError err: form.get().validate()){
                form.reject(err);
            }
            return badRequest(registration.render(form));

        }else{

            // the User was valid
            form.get().save();
            return redirect(controllers.routes.Authentication.authentication());
        }
    }
}
