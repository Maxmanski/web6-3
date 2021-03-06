package controllers;

import at.ac.tuwien.big.we15.lab2.api.*;
import at.ac.tuwien.big.we15.lab2.api.impl.*;
import play.mvc.*;

import views.html.*;
import play.i18n.Messages;
import play.i18n.Lang;
import java.util.List;
import play.cache.Cache;
import play.db.jpa.Transactional;

public class Overview extends Controller {

    @Security.Authenticated(UserAuthenticator.class)
    @Transactional
    public static Result jeopardy() {
        String filepath;
        String language = play.i18n.Lang.defaultLang().language();
        if(language.startsWith("de")) {
            filepath = "data.de.json";
        }else{
            filepath = "data.en.json";
        }

        JeopardyFactory factory = new PlayJeopardyFactory(filepath);

        String uuid = session().get("uuid");
        String username = session("username");
        User user = models.User.getUserByUsername(username);

        if(user == null){
            flash("error", Messages.get("error_sessionUserInvalid"));
            return redirect(routes.Authentication.authentication());
        }

        // if no game has been started yet, a new one will be created
        JeopardyGame game;
        if((Cache.get(uuid + "game") == null) || !(Cache.get(uuid + "game") instanceof JeopardyGame)){
            game = factory.createGame(user);
            Cache.set(uuid + "game", game);
        }else{
            game = ((JeopardyGame)Cache.get(uuid + "game"));
        }

        // if the game is over, a new one will be started
        if(game.isGameOver()){
            game = factory.createGame(user);
            Cache.set(uuid + "game", game);
        }

        game.getHumanPlayer().getUser().setName(username+ " (Du)");

        return ok(jeopardy.render(game, flash("warning"), flash("error"), session("questionValue"),session("questionCategory")));
    }

}
