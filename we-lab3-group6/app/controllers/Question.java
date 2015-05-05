package controllers;

import play.*;
import play.mvc.*;

import views.html.*;
import play.i18n.*;
import play.i18n.Messages;

public class Question extends Controller {

    public static Result index() {
        return ok(index.render(Messages.get("label_titleQuestion")));
    }

}
