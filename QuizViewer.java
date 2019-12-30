import java.util.*;
import java.io.*;
import javafx.application.Application; 
import javafx.scene.Scene; 
import javafx.scene.control.*; 
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*; 
import javafx.stage.Stage; 
import javafx.stage.FileChooser;
import javafx.scene.layout.*; 
import javafx.scene.paint.*; 
import javafx.scene.text.*; 
import javafx.geometry.*; 
import javafx.scene.layout.*; 
import javafx.scene.shape.*; 
import java.nio.file.Paths;

public class QuizViewer extends Application{
	boolean close = true;
	private Pane box;
	private static Quiz quiz = new Quiz();
	private TextArea tfDescription;
	private int score;
	public static void main(String[] args) throws InvalidQuizFormatException{
		Application.launch(args);
	}
	public void start(Stage primaryStage) throws IOException, InvalidQuizFormatException{
		Button load = new Button("Load");
		VBox box = new VBox(load);
		box.setAlignment(Pos.CENTER);
		primaryStage.setScene(new Scene(box, 300, 200));
		primaryStage.setTitle("Quiz");
		primaryStage.show();
		load.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			String userDirectoryString = System.getProperty("user.dir");
			File userDirectory = new File(userDirectoryString);
			if(!userDirectory.canRead()) {
				userDirectory = new File("c:/");
			}
			fileChooser.setInitialDirectory(userDirectory);
			Stage quizStage = new Stage();
			File file = fileChooser.showOpenDialog(quizStage);
				quizStage.setOnCloseRequest(ev -> {
					close = false;
				});
				if (file != null){
					try{
						openQuiz(file);
					}
					catch(Exception ex){
						
					}
				}
			try{if (file != null){
				QuizPane quizpane = new QuizPane(quiz.getQuestions());
				quizStage.setScene(new Scene(quizpane));
				quizStage.setTitle("Quiz");
				quizStage.show();
				primaryStage.close();}
			}
			catch (Exception ex){}
		});
	}
	public void openQuiz(File file) throws Exception{
		try{
			if (!file.getName().contains("txt")) throw new InvalidQuizFormatException(file.getName());
			quiz = quiz.loadFromFile(file.getName());
		}
		catch(InvalidQuizFormatException ex){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setContentText("You chose innapropriate file");
			alert.setHeaderText("Ooops, there was an error!");
			alert.showAndWait();
		}
	}
}