package com.example.calculator;

import com.example.calculator.Utilities.AlertUtilities;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.util.Objects;

public class CalculatorController {

    @FXML
    private TextField display;
    private double beforeOp = 0;
    private double lastAfterOp = 0;
    private String operator = "";
    private boolean startNewNumber = true;
    private boolean decimalUsed = false;
    private boolean error = false;
    private boolean secondNumber = false;


    @FXML
    private void handleNumberAction(ActionEvent event) {
        secondNumber = true;
        String number = ((Button) event.getSource()).getText();
        if (error) {
            handleClearAction();
            error = false;
        }
        if (startNewNumber) {
            display.setText(number);
            startNewNumber = false;
            if (Objects.equals(number, ".")) {
                decimalUsed = true;
            }
        } else {
            if (Objects.equals(number, ".") && !decimalUsed) {
                decimalUsed = true;
                display.setText(display.getText() + number);
            } else if (decimalUsed && Objects.equals(number, ".")) {
                AlertUtilities.displayError("Negalima naudoti dar vieno kablelio.");
            } else if (!Objects.equals(number, ".")) display.setText(display.getText() + number);
        }
    }

    @FXML
    private void handleSignChange() {
        if (error) {
            handleClearAction();
            error = false;
        } else {
            String number = (display.getText());
            double num = Double.parseDouble(number) * -1;
            display.setText(String.valueOf(num));
        }
    }

    @FXML
    private void handleOperatorAction(ActionEvent event) {
        secondNumber = false;
        if (error) {
            handleClearAction();
            error = false;
        }
        String op = ((Button) event.getSource()).getText();
        if (!display.getText().isEmpty()) {
            beforeOp = Double.parseDouble(display.getText());
            operator = op;
            startNewNumber = true;
            decimalUsed = false;
        }
    }

    @FXML
    private void handleEqualsAction() {
        if (error) {
            handleClearAction();
            error = false;
        }
        if (display.getText().isEmpty()) {
            AlertUtilities.displayError("Įveskite skaičius bei veiksmus.");
            return;
        }
        if (operator.isEmpty()) {
            AlertUtilities.displayError("Pasirinkite operatorių.");
            return;
        }

        double afterOp = 0;
        if (startNewNumber) {
            afterOp = lastAfterOp;
        } else {
            afterOp = Double.parseDouble(display.getText());
            lastAfterOp = afterOp;
        }
        if (!secondNumber) {
            AlertUtilities.displayError("Įveskite antrąjį skaičių.");
            return;
        }

        double result = calculate(beforeOp, afterOp, operator);

        if ((afterOp == 0 && operator.equals("÷")) || (afterOp == 0 && operator.equals("%"))) {
            AlertUtilities.displayError("Negalima dalinti iš 0");
            display.setText("KLAIDA");
            error = true;
        } else {
            display.setText(String.valueOf(result));
            beforeOp = result;
            startNewNumber = true;
        }
    }

    @FXML
    private void handleClearAction() {
        display.setText("");
        beforeOp = 0;
        operator = "";
        startNewNumber = true;
        decimalUsed = false;
    }

    private double calculate(double a, double b, String op) {
        switch (op) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "×":
                return a * b;
            case "÷":
                if (b != 0) {
                    return a / b;
                } else {
                    return 0;
                }
            case "%":
                if (b != 0) {
                    return a % b;
                } else {
                    return 0;
                }
            default:
                return 0;
        }
    }
}
