package controllers;

import at.ac.tuwien.big.we15.lab2.api.Category;
import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import jdk.nashorn.internal.ir.ReturnNode;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.question;

public class Question extends Controller {

    @Security.Authenticated(UserAuthenticator.class)
    public static Result question() {
        JeopardyGame game = null;
        String uuid = session("uuid");
        Integer qId;
        at.ac.tuwien.big.we15.lab2.api.Question q = null;

        DynamicForm form = Form.form().bindFromRequest();

        // check if the game is still in the cache: if it isn't, start new game
        if(Cache.get(uuid + "game") == null){
            flash("error", "Game could not be loaded from the cache - starting new one");
            return badRequest(); // TODO

        }else if(Cache.get(uuid + "game") instanceof JeopardyGame){
            game = ((JeopardyGame)Cache.get(uuid + "game"));

            // check for valid chosen question
            try {
                qId = Integer.parseInt(form.get("question_selection"));
                for(Category c: game.getCategories()){
                    for(at.ac.tuwien.big.we15.lab2.api.Question qu: c.getQuestions()){
                        if(qu.getId() == qId){
                            q = qu;
                        }
                    }
                }
            }catch (Exception e){
                qId = null;
                q = null;
            }

            // if no valid question was chosen, redirect back to overview
            if(q == null){
                flash("error", "No valid question was chosen");
                return redirect(routes.Overview.jeopardy());

            }

            return ok(question.render(Messages.get("label_titleQuestion"), game, q));

        }else{

            // should not happen unless someone tampered with the cache
            return badRequest();
        }
    }

}
