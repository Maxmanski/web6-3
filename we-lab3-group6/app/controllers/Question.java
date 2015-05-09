package controllers;

import at.ac.tuwien.big.we15.lab2.api.Category;
import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import models.User;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.question;

public class Question extends Controller {

    @Security.Authenticated(UserAuthenticator.class)
    public static Result question() {
        if(User.getUserByUsername(session("username")) == null){
            flash("error", Messages.get("error_sessionUserInvalid"));
            return redirect(routes.Authentication.authentication());
        }

        JeopardyGame game = null;
        String uuid = session("uuid");
        Integer qId;
        at.ac.tuwien.big.we15.lab2.api.Question q = null;

        DynamicForm form = Form.form().bindFromRequest();

        // check if the game is still in the cache: if it isn't, start new game
        if((Cache.get(uuid + "game") == null) || !(Cache.get(uuid + "game") instanceof JeopardyGame)){
            flash("error", Messages.get("error_loadingGameData"));
            return redirect(routes.Overview.jeopardy());

        }else{
            game = ((JeopardyGame)Cache.get(uuid + "game"));

            // check for valid chosen question
            try {
                qId = Integer.parseInt(form.get("question_selection"));
                game.chooseHumanQuestion(qId);
                for(Category c: game.getCategories()){
                    for(at.ac.tuwien.big.we15.lab2.api.Question qu: c.getQuestions()){
                        if(qu.getId() == qId){
                            q = qu;
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                q = null;
                qId = null;
            }

            // if no valid question was chosen, redirect back to overview
            if(qId == null){
                flash("error", Messages.get("error_noValidQuestion"));
                return redirect(routes.Overview.jeopardy());

            }

            Cache.set(uuid + "game", game);
            return ok(question.render(game, q, flash("warning"), flash("error")));

        }
    }
}
