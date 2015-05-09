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
        String language = Lang.apply$default$2();
        if(language.equals("de")) {
            filepath = "data.de.json";
        }else{
            filepath = "data.en.json";
        }
        JeopardyFactory factory = new PlayJeopardyFactory(filepath);
        QuestionDataProvider provider =factory.createQuestionDataProvider();
        //List<Category> categories = provider.getCategoryData();

        String uuid = session().get("uuid");
        String username = session().get("username");
        User user = Users.getUserByUsername(username);

        /** NUR FUER TESTEN, SPAETER LOESCHEN**/
        if(user == null){
            user=new SimpleUser();
            user.setName("user 1");
            user.setAvatar(Avatar.ALDRICH_KILLIAN);
        }
        /**************************************/

        JeopardyGame game = factory.createGame(user);
        Cache.set(uuid+"game", game);

        return ok(jeopardy.render(Messages.get("label_titleQuestionSelection")));
    }

}
