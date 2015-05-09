package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Class used for User Authentication Checks
 *
 * Redirects to the login page whenever an access violation occurs
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
