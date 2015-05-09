package controllers;

import models.User;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.registration;

import java.util.List;

public class Registration extends Controller {

    public static Result registration() {
        Form<models.User> userForm = Form.form(models.User.class);
        return ok(registration.render(userForm, flash("warning"), flash("error")));
    }

    public static Result register(){
        Form<User> form = Form.form(User.class).bindFromRequest();

        if(form.hasErrors()){
            // there were errors in the form
            return badRequest(registration.render(form, flash("warning"), flash("error")));

        }else if(form.get().validate() != null){

            List<ValidationError> validationErrors = form.get().validate();
            String errors = "";
            // the specified user was not valid
            for(int i=0; i<validationErrors.size(); i++){
                errors += validationErrors.get(i).message();
                if(i < (validationErrors.size() - 1)){
                    errors += ", ";
                }
            }
            return badRequest(registration.render(form, flash("warning"), errors));

        }else{

            // the User was valid
            form.get().save();
            return redirect(controllers.routes.Authentication.authentication());
        }
    }
}
