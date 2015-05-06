package controllers;

import play.*;
import play.mvc.*;

import views.html.*;
import play.i18n.*;
import play.i18n.Messages;

public class Question extends Controller {

    public static Result question() {
        return ok(question.render(Messages.get("label_titleQuestion")));
    }

}
