package controllers;

import at.ac.tuwien.big.we15.lab2.api.*;
import at.ac.tuwien.big.we15.lab2.api.impl.*;
import play.*;
import play.mvc.*;

import views.html.*;
import play.i18n.*;
import play.i18n.Messages;
import java.util.Formatter;
import java.util.List;
import java.util.ArrayList;

public class Overview extends Controller {

    public static Result jeopardy() {
        String filepath ="/conf/data.de.json";
        JeopardyFactory factory = new PlayJeopardyFactory(filepath);
        QuestionDataProvider provider =factory.createQuestionDataProvider();
        //List<Category> categories = provider.getCategoryData(), tmp = new ArrayList<Category>();

        //SimpleUser user = factory.createUser();
        //SimpleJeopardyGame game = factory.createGame(user);
        //SimplePlayer human = game.getHumanPlayer();

        return ok(index.render(Messages.get("label_titleQuestionSelection")));
    }

}
