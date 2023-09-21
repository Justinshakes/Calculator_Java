package Calc;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;

public class Calculator extends JFrame implements ActionListener {

    private JPanel contentPane;
    private JTextField textField;
    private String displayText = "", displayTextBack = "";
    private String numBuilder = "";
    private String num1, num2;
    private JTextField BGtf;
    private String operator = "", oldOperator = "";
    private boolean pressedOperator = false;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Calculator frame = new Calculator();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Calculator() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 378, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Use this as background / history of inputs
        BGtf = new JTextField();
        BGtf.setHorizontalAlignment(SwingConstants.RIGHT);
        BGtf.setFont(new Font("Arial", Font.PLAIN, 16));
        BGtf.setBounds(127, 0, 219, 42);
        contentPane.add(BGtf);
        BGtf.setColumns(10);

        JPanel panel = new JPanel();
        panel.setBounds(0, 103, 366, 318);
        contentPane.add(panel);
        panel.setLayout(new GridLayout(6, 4, 1, 1));

        textField = new JTextField();
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        textField.setFont(new Font("Arial", Font.BOLD, 18));
        textField.setBounds(30, 42, 316, 55);
        contentPane.add(textField);
        textField.setColumns(10);

        JButton[] buttons = new JButton[24];
        String[] buttonLabels = { "%", "Sqrt", "x^2", "1/x", "CE", "C", "<-X", "/", "7", "8", "9", "*", "4", "5", "6",
                "-", "1", "2", "3", "+", "+/-", "0", ".", "=" };
        Font buttonFont = new Font("Arial", Font.BOLD, 22);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setFont(buttonFont);
            buttons[i].addActionListener(this);
            /*
             * addActionListener(this) means that when the button is clicked, it will
             * trigger a method that is implemented in the current class (the class where
             * this is being used).
             */
            panel.add(buttons[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        String input = e.getActionCommand();
        switch (input) {

            case "CE":
                //System.out.println("Num1: " + num1 + "  Num2: " + num2 + "  numBuilder: " + numBuilder + "Operator: " + operator);
                numBuilder = "";
                displayText = "";
                displayTextBack = "";
                textField.setText(displayText);
                break;
            case "C":
                clear();
                break;

            case "%":
                finishNumber();
                double tempNum;
                tempNum = (Double.parseDouble(num2) / 100) * Double.parseDouble(num1); // create a percentage of num1
                num2 = String.valueOf(tempNum);
                Calculate();
                break;
            case "x^2":
                if(pressedOperator) {
                    tempNum = Double.valueOf(numBuilder) * Double.valueOf(numBuilder);
                    numBuilder = String.valueOf(tempNum);
                    finishNumber();
                    Calculate();
                } else {
                    finishNumber();
                    operator = "*";
                    num2 = num1;
                    Calculate();
                }
                break;
            case "<-X":
                numBuilder = numBuilder.substring(0, numBuilder.length() - 1);
                textField.setText(numBuilder);
                displayTextBack = numBuilder;
                break;
            case "1/x":
                finishNumber();
                operator = "/";
                num2 = num1;
                num1 = "1";
                Calculate();
                break;
            case "Sqrt":
                finishNumber();
                operator = "Sqrt";
                num2 = "0";
                Calculate();
                break;
            case "+/-":
                System.out.println("HERE: +-");
                double temp = Double.valueOf(numBuilder);
                temp *= -1;
                numBuilder = String.valueOf(temp);
                displayTextBack = numBuilder;
                textField.setText(displayTextBack);
                break;
            case "+":
            case "-":
            case "*":
            case "/":
                try {
                    finishNumber();
                    operator = input;
                    if(pressedOperator && operator == oldOperator) {
                        Calculate();
                    }
                    oldOperator = operator;
                    if(displayTextBack.contains("+") || displayTextBack.contains("-") || displayTextBack.contains("*") || displayTextBack.contains("/")) {
                        displayTextBack = displayTextBack.substring(0, displayTextBack.length() - 1);
                        displayTextBack += input;
                        BGtf.setText(displayTextBack);
                    } else {
                        displayTextBack += input;
                        BGtf.setText(displayTextBack);
                    }
                    displayText = "";
                    textField.setText(displayText);

                    pressedOperator = true;
                }catch(Exception x) {

                }
                break;

            case "=":
                if(numBuilder == "") { // Repeat = equation
                    numBuilder = num2;
                    Calculate();
                    break;
                }
                finishNumber();
                Calculate();
                break;
            default:
                if (numBuilder.contains(".") && input == ".") { // So user can't input multible decimals

                } else {
                    numBuilder += input;
                    textField.setText(numBuilder);
                    displayTextBack += input;
                }
        }
    }

    private void Calculate() {
        boolean sqrt = false,divByZero = false;
        double answer = 0;
        System.out.println("Num1: " + num1);
        System.out.println("Num2: " + num2);
        System.out.println("Operator: " + operator);
        switch (operator) {
            case "+":
                answer = Double.parseDouble(num1) + Double.parseDouble(num2);
                break;
            case "-":
                answer = Double.parseDouble(num1) - Double.parseDouble(num2);
                break;
            case "*":
                answer = Double.parseDouble(num1) * Double.parseDouble(num2);
                break;
            case "/":
                if (Double.parseDouble(num2) == 0) {
                    divByZero = true;
                    break;
                }
                answer = Double.parseDouble(num1) / Double.parseDouble(num2);
                break;
            case "Sqrt":
                sqrt = true;
                answer = Double.parseDouble(num1);
                answer = Math.pow(answer, .5);
                break;
        }
        if (sqrt) {
            BGtf.setText(operator + "(" + num1 + ")" + "=");
        } else {
            BGtf.setText(num1 + operator + num2 + "=");
        }
        num1 = String.valueOf(answer);
        displayText = num1;
        displayTextBack = num1;
        textField.setText(displayText);
        pressedOperator = false;
        if (divByZero) {
            textField.setText("Undefined");
        }
    }

    private void finishNumber() {
        if (num1 == null || num1 == "") {
            num1 = numBuilder;
        } else {
            System.out.println("Num2 = numbuilder");
            num2 = numBuilder;
        }
        numBuilder = "";

    }

    private void clear() {
        numBuilder = "";
        displayText = "";
        displayTextBack = "";
        num1 = "";
        num2 = "";
        BGtf.setText(displayTextBack);
        textField.setText(displayText);
        pressedOperator = false;
    }

}
