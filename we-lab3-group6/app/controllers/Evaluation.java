package controllers;

import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import models.User;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.winner;

import java.util.ArrayList;
import java.util.List;

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
    public static Result evaluate(){
        if(User.getUserByUsername(session("username")) == null){
            flash("error", Messages.get("error_sessionUserInvalid"));
            return redirect(routes.Authentication.authentication());
        }

        JeopardyGame game = getGame();

        DynamicForm form = Form.form().bindFromRequest();
        List<Integer> questionIds = new ArrayList<Integer>();

        // get the user's chosen answers for the question
        for(String str: form.data().keySet()){
            try {
                questionIds.add(Integer.parseInt(form.get(str)));

            }catch (Exception e){

            }
        }

        if(game == null){
            flash("error", Messages.get("error_loadingGameData"));
            return redirect(routes.Overview.jeopardy());
        }

        game.answerHumanQuestion(questionIds);
        Cache.set(session("uuid") + "game", game);

        if(game.isGameOver()){
            return ok(winner.render(game, flash("warning"), flash("error")));
        }

        return redirect(routes.Overview.jeopardy());
    }

}
