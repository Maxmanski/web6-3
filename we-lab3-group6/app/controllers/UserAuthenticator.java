package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by maxmanski on 08.05.15.
 */
public class UserAuthenticator extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context ctx){
        return ctx.session().get("username");
    }

    @Override
    public Result onUnauthorized(Http.Context ctx){
        return redirect(controllers.routes.Authentication.authentication());
    }
}
