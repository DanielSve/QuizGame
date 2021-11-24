package Client;

import Questions.Question;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.Properties;



public class Receiver extends Thread {
    Thread thread;
    ObjectInputStream in;
    Socket socket;
    Client client;
    Object obj;
    public Receiver(Socket socket, Client client){
        this.socket = socket;
        this.client = client;

        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        thread = new Thread(this);
        thread.start();
    }

    public void run(){
        while(true) {

            try {
                while (((obj = in.readObject())!=null)) {
                    if(obj instanceof Properties) {
                        client.gui.setRemainingPanels(Integer.parseInt(((Properties) obj).getProperty("amountOfRows")),
                                Integer.parseInt(((Properties) obj).getProperty("amountOfQuestions")));
                        client.setProperties(Integer.parseInt(((Properties) obj).getProperty("amountOfRows")),
                                Integer.parseInt(((Properties) obj).getProperty("amountOfQuestions")));
                        System.out.println("KLART");
                    }
                    if(obj instanceof List<?>) {
                        client.setCategoryQuestion((List<?>) obj);
                        client.gui.setContentPane(client.gui.gamePanel);
                        client.gui.repaint();
                        client.gui.revalidate();
                        client.gui.scorePanel.player1.setText("Player 1");
                        client.gui.scorePanel.player2.setText("Player 2");
                    }

                    if(obj instanceof Question) {
                        client.setCurrentQuestion((Question)obj);
                        client.gui.setContentPane(client.gui.gamePanel);
                        client.gui.repaint();
                        client.gui.revalidate();
                        client.gui.scorePanel.player1.setText("Player 1");
                        client.gui.scorePanel.player2.setText("Player 2");
                    }
                    else if(obj instanceof String []){
                        String[] temp1 = (String[]) obj;
                        client.setResults((String[]) obj);
                        for (int i = 0; i < temp1.length ; i++) {
                            System.out.println(" Svar: " + temp1[i]);
                        }
                    }
                    else if (obj instanceof String){
                        String s = (String) obj;
                        if (s.equalsIgnoreCase("Player 1")){
                            client.setPlayerID(1);
                        }else if (s.equalsIgnoreCase("Player 2")){
                            client.setPlayerID(2);
                        } else if(s.equalsIgnoreCase("Next Round")){
                            client.setCurrentRow();
                            System.out.println("Received Next Round");
                        } else if(s.contains("Score")) {
                            client.setScore(s);
                        } else if(s.equalsIgnoreCase("won") || s.equalsIgnoreCase("lost") ||
                        s.equalsIgnoreCase("tied")) {
                            client.setEndResult(s);
                        } else if(s.equalsIgnoreCase("start?")){
                            client.showStartButton();
                        } else
                        {
                            System.out.println(s);
                            client.gui.gamePanel.question.setText(s);
                            Thread.sleep(1500);
                            client.resetButtonColor();
                            client.gui.setContentPane(client.gui.scorePanel);
                            client.gui.repaint();
                            client.gui.revalidate();
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

