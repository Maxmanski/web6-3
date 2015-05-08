package controllers;

import play.*;
import play.mvc.*;

import views.html.*;
import play.i18n.*;
import play.i18n.Lang;
import play.i18n.Messages;
import java.util.Formatter;

public class Evaluation extends Controller {

    @Security.Authenticated(UserAuthenticator.class)
    public static Result winner() {
        return ok(winner.render(Messages.get("label_titleWinnerNotification")));
    }

}
