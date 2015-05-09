package controllers;

import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import play.cache.Cache;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.winner;

public class Evaluation extends Controller {

    private static JeopardyGame getGame(){
        String uuid = session("uuid");

        if(Cache.get(uuid + "game") == null){
            return null;
        }else if(Cache.get(uuid + "game") instanceof JeopardyGame){
            return (JeopardyGame)Cache.get(uuid + "game");
        }
        return null;
    }

    @Security.Authenticated(UserAuthenticator.class)
    public static Result winner() {
        JeopardyGame game = getGame();

        if(game == null){
            flash("error", "Game could not be loaded from the cache - starting new one");
            return badRequest(); // TODO
        }

        return ok(winner.render(Messages.get("label_titleWinnerNotification"), game));
    }

    @Security.Authenticated(UserAuthenticator.class)
    public static Result evaluate(){
        JeopardyGame game = getGame();

        if(game == null){
            flash("error", "Game could not be loaded from the cache - starting new one");
            return badRequest(); // TODO
        }

        Integer qId;
        at.ac.tuwien.big.we15.lab2.api.Question question;

        return redirect(routes.Evaluation.winner());
    }

}
