package Server;


import Questions.Question;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Game extends Thread {
    public int roundNr = 1;
    public int questionNr = 0;
    public boolean p1Answered;
    public boolean p2Answered;
    Player currentPlayer;
    Player playerOne;
    Player playerTwo;
    Database database;
    List<Question> questions;

    Thread thread = new Thread(this);
    public Game() throws IOException {
        database= new Database();
        questions = database.getQuestionsForGame();
        Collections.shuffle(questions);
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    @Override
    public void run() {
        currentPlayer = playerOne;
        playerTwo.sendMessageToPlayer("Player 2");
        while(true) {
            currentPlayer.sendMessageToPlayer(questions.get(questionNr));
            String temp;
            temp = currentPlayer.receiver.getAnswer();
            if (temp.equalsIgnoreCase(questions.get(questionNr).getCorrectAnswer())) {
                currentPlayer.sendMessageToPlayer("Rätt svar!");
                currentPlayer.setResults(questionNr, "correct");
            } else {
                currentPlayer.sendMessageToPlayer("Fel svar!");
                currentPlayer.setResults(questionNr, "false");
            }
            questionNr ++;
            System.out.println(questionNr);
            if(currentPlayer == playerOne && questionNr == 3){
                p1Answered = true;
            }
            else if (currentPlayer == playerTwo && questionNr == 3) {
                p2Answered = true;
            }
            if(p1Answered && p2Answered){
                //nya frågor sen
                questionNr = 0;
                p1Answered = false;
                p2Answered = false;
            } else if (questionNr==3) {
                changePlayer();
                questionNr = 0;
            }
        }
    }

    public void changePlayer(){
        if (currentPlayer == playerOne){
            currentPlayer = playerTwo;
        } else {
            currentPlayer = playerOne;
        }
    }
}
