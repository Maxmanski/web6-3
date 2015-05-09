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
        String username = session().get("username");
        User user = Users.getUserByUsername(username);

        JeopardyGame game;
        if((Cache.get(uuid + "game") == null) || !(Cache.get(uuid + "game") instanceof JeopardyGame)){
            game = factory.createGame(user);
            Cache.set(uuid + "game", game);
        }else{
            game = ((JeopardyGame)Cache.get(uuid + "game"));
        }

        if(game.isGameOver()){
            game = factory.createGame(user);
            Cache.set(uuid + "game", game);
        }

        return ok(jeopardy.render(Messages.get("label_titleQuestionSelection"), game));
    }

}
