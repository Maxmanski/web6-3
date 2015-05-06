package controllers;

import play.*;
import play.mvc.*;

import views.html.*;
import play.i18n.*;
import play.i18n.Lang;
import play.i18n.Messages;
import java.util.Formatter;

public class Authentication extends Controller {

    public static Result authentication() {
        return ok(index.render(Messages.get("label_login")));
    }

}
