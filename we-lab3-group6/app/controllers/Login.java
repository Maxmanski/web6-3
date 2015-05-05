package controllers;

import play.*;
import play.mvc.*;

import views.html.*;
import play.i18n.*;
import play.i18n.Lang;
import play.i18n.Messages;
import java.util.Formatter;

public class Application extends Controller {

    public static Result index() {
        //Lang.set("de");
        return ok(index.render(Messages.get("test", "Nicole")));
    }

}
