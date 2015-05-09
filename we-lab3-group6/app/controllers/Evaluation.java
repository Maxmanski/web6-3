package controllers;

import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
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
    public static Result winner() {
        JeopardyGame game = getGame();

        if(game == null){
            flash("error", "Game could not be loaded from the cache - starting new one");
            return badRequest(); // TODO
        }

        return ok();
        //return ok(game));
    }

    @Security.Authenticated(UserAuthenticator.class)
    public static Result evaluate(){
        JeopardyGame game = getGame();

        DynamicForm form = Form.form().bindFromRequest();
        List<Integer> questionIds = new ArrayList<Integer>();

        for(String str: form.data().keySet()){
            try {
                questionIds.add(Integer.parseInt(form.get(str)));

            }catch (Exception e){

            }
        }

        if(game == null){
            flash("error", "Game could not be loaded from the cache - starting new one");
            return badRequest(); // TODO
        }

        System.out.println(game);

        game.answerHumanQuestion(questionIds);
        Cache.set(session("uuid") + "game", game);

        if(game.isGameOver()){
            return ok(winner.render(game));
        }

        if(session("round") == null){
            session("round", "1");
        }else{
            try {
                session("round", Integer.toString(Integer.parseInt(session("round")) + 1));
            }catch (Exception e){
                session("round", "1");
            }
        }

        return redirect(routes.Overview.jeopardy());
    }

}
