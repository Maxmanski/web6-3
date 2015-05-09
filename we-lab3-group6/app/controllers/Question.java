package controllers;

import at.ac.tuwien.big.we15.lab2.api.Category;
import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
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
        JeopardyGame game = null;
        String uuid = session("uuid");
        Integer qId;
        int availableQuestions = 0;
        at.ac.tuwien.big.we15.lab2.api.Question q = null, aiQ = null;

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
                        availableQuestions++;
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

            game.chooseHumanQuestion(qId);
            boolean validAiQuestion = false;

            // AI question id
            int aiQId = 0;

            while((availableQuestions > 0) && (!validAiQuestion)){
                aiQId = (int)(Math.random() * game.getMaxQuestions());
                for(Category c: game.getCategories()){
                    for(at.ac.tuwien.big.we15.lab2.api.Question qu: c.getQuestions()){
                        if((aiQId == qu.getId()) && (!game.hasBeenChosen(qu))){
                            validAiQuestion = true;
                        }
                    }
                }
            }

            game.getMarvinPlayer().chooseQuestion(aiQ);
            Cache.set(uuid + "game", game);
            return ok(question.render(Messages.get("label_titleQuestion"), game, q));

        }else{

            // should not happen unless someone tampered with the cache
            return badRequest();
        }
    }

}
