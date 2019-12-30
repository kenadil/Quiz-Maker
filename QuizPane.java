import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;

public class QuizPane extends VBox {
	Button checkResults = new Button("Check answers");
	TextArea tfDescription = new TextArea();
	RadioButton btFirst = new RadioButton();
	RadioButton btSecond = new RadioButton();
	RadioButton btThird = new RadioButton();
	RadioButton btFourth = new RadioButton();
	VBox answerBox = new VBox();
	String description;
	ArrayList<Question> questions = new ArrayList<>();
	ArrayList<String> paramQuestions = new ArrayList<>();
	ArrayList<String> answerBoxes = new ArrayList<>();
	Label status = new Label();
	int number = 0;
	int misch = -1;
	int score=0;
	
	QuizPane(ArrayList<Question> questions) throws IOException{
		this.questions = questions;
		Button btForward = new Button(">>");
		Button btBack = new Button("<<");
		gather();
		paramQuestions = removeDuplicates(paramQuestions);
		answerBox.setPadding(new Insets(5, 5, 0, 5));
		for (int i = 0; i<questions.size(); i++){
			answerBoxes.add("PlaceForAnswer");
		}
		setDescription(questions.get(number).getDescription().replaceAll("\\{blank\\}", "_____"));
		if (questions.get(number) instanceof Test){
			setBox(paramQuestions.get(number));
		}
		else{
			setField();
		}
		checkResults.setOnMouseClicked(e -> {
			check();
		});
		HBox box = new HBox(btBack, btForward);
		status.setText("Status " + (number+1) + "/" + questions.size() + " questions.");
		btForward.setOnMouseClicked(e -> {
			number++; if (number>questions.size()-1) number = questions.size()-1;
			setDescription(questions.get(number).getDescription().replaceAll("\\{blank\\}", "_____"));
			if (questions.get(number) instanceof Test){
				try{setBox(paramQuestions.get(number));}catch(Exception ex){answerBox.getChildren().clear();  System.out.println(ex);}
			}
			else{
				try{setField();} catch(Exception ex){answerBox.getChildren().clear();  System.out.println(ex);}
			}
			status.setText("Status " + (number+1) + "/" + questions.size() + " questions.");
			if ((misch++)+1 > questions.size()-2){
				status.setText("Status " + (number+1) + "/" + questions.size() + " questions.\nEnd of the quiz!");
				misch--;
			}
		});
		btBack.setOnMouseClicked(e -> {
			number--; if (number<0) number = 0;
			setDescription(questions.get(number).getDescription().replaceAll("\\{blank\\}", "_____"));
			if (questions.get(number) instanceof Test){
				try{setBox(paramQuestions.get(number));} catch(Exception ex){answerBox.getChildren().clear(); System.out.println(ex);}
			}
			else{
				try{setField();} catch(Exception ex){answerBox.getChildren().clear(); System.out.println(ex);}
			}
			status.setText("Status " + (number+1) + "/" + questions.size() + " questions.");
			if ((misch--)-1 < -1){
				status.setText("Status " + (number+1) + "/" + questions.size() + " questions.\nStart of the quiz!");
				misch++;
			}
		});
		box.setSpacing(400); box.setPadding(new Insets(5, 5, 5, 5)); box.setPrefHeight(20);
		this.setPadding(new Insets(10));
		HBox statusBox = new HBox(150, status, checkResults);
		statusBox.setPadding(new Insets(5, 5, 0, 5));
		statusBox.setAlignment(Pos.CENTER);
		statusBox.setPrefHeight(100);
		this.getChildren().addAll(tfDescription, box, answerBox, statusBox);
	}
	
	public void setButtons(){
		btFirst.setOnAction(e -> {
			answerBoxes.set(number, btFirst.getText());
			btThird.setSelected(false); btSecond.setSelected(false); btFourth.setSelected(false);
		});
		btSecond.setOnAction(e -> {
			answerBoxes.set(number, btSecond.getText());
			btFirst.setSelected(false); btThird.setSelected(false); btFourth.setSelected(false);
		});
		btThird.setOnAction(e -> {
			answerBoxes.set(number, btThird.getText());
			btFirst.setSelected(false); btSecond.setSelected(false); btFourth.setSelected(false);
		});
		btFourth.setOnAction(e -> {
			answerBoxes.set(number, btFourth.getText());
			btFirst.setSelected(false); btSecond.setSelected(false); btThird.setSelected(false);
		});
		
	}
	
	public void textButtons(String[] text){
		btFirst = new RadioButton(text[1]);
		btSecond = new RadioButton(text[2]);
		btThird = new RadioButton(text[3]);
		btFourth = new RadioButton(text[4]);
	}
		
	public void setBox(String question) throws IOException{
		
		String[] text = question.split("\n");
		answerBox.getChildren().clear();
		textButtons(text);
		setButtons();
		answerBox.getChildren().addAll(btFirst, btSecond, btThird, btFourth);
		String[] texts = {btFirst.getText(), btSecond.getText(), btThird.getText(), btFourth.getText()};
		if (!answerBoxes.get(number).equals("PlaceForAnswer")){
			for (int i = 0; i<texts.length; i++){
				if (answerBoxes.get(number).equals(texts[i])){
					switch (i){
						case 0: btFirst.setSelected(true); break;
						case 1: btSecond.setSelected(true); break;
						case 2: btThird.setSelected(true); break;
						case 3: btFourth.setSelected(true); break;
					}
					break;
				}
			}
		}
	}
	public void setField() throws IOException{
		answerBox.getChildren().clear();
		TextField tfAnswer = new TextField();
		if (!answerBoxes.get(number).equals("PlaceForAnswer")){
			tfAnswer.setText(answerBoxes.get(number));
		}
		answerBox.getChildren().add(tfAnswer);
		tfAnswer.setOnKeyReleased(e -> {
			if (tfAnswer.getText()!=null){
				answerBoxes.set(number, tfAnswer.getText());
			}
		});
	}
	
	
	public void setDescription(String question){
		tfDescription.setText(question.split("\n")[0]);
		tfDescription.setEditable(false); tfDescription.setFont(Font.font("Verdana", 14));
		tfDescription.setPrefWidth(400);
		tfDescription.setPrefHeight(100);
		tfDescription.setWrapText(true);
	}
	
	
	public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) 
    { 
  
        // Create a new ArrayList 
        ArrayList<T> newList = new ArrayList<T>(); 
  
        // Traverse through the first list 
        for (T element : list) { 
  
            // If this element is not present in newList 
            // then add it 
            if (!newList.contains(element)) { 
  
                newList.add(element); 
            } 
        } 
  
        // return the new list 
        return newList; 
    } 
	public void check(){
		score = 0;
		for (int i = 0; i<questions.size(); i++){
			if (answerBoxes.get(i).toLowerCase().equals(questions.get(i).getAnswer().toLowerCase()))
				score++;
		}
		if (score < questions.size()){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Quiz: Results");
			alert.setHeaderText("Number of correct answers: " + score + "/" + questions.size() + " (" + (100.0*score)/(double)questions.size() + "%)");
			alert.setContentText("You may try again");

			alert.showAndWait();
		}
		else{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Quiz: Results");
			alert.setHeaderText("Number of correct answers: " + score + "/" + questions.size() + " (" + (100.0*score)/(double)questions.size() + "%)");
			alert.setContentText("Good job!");

			alert.showAndWait();
		}
	}
	
	public void gather(){
		for (Question question : questions){
			if (question instanceof Test){
				
				String[] temp = ((Test)question).toString().split("\n");
				String temps = "";
				for (int j = 0; j<5; j++){
					StringBuilder ts = new StringBuilder();
					for (int c = temp[j].length()-1; c>2; c--){
						ts.append(temp[j].charAt(temp[j].length()-1-c+3));
					}
					temps += ts.toString() + "\n";
				}
				paramQuestions.add(temps);
			}
			else{
				paramQuestions.add(question.toString());
			}
		}
	}
}