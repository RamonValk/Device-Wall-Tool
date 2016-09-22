package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sun.rmi.runtime.Log;
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Controller extends Application{

    public TextArea printlnOutput;

    private String whatToDo;
    private String whatIsHappening;
    private String whoAmI;
    private String input;
    private Boolean doINeedRoot;
    private Boolean doINeedInput;

    @FXML
    private Button refreshButton;
    @FXML
    private Button Reboot_BootloaderBTN;
    @FXML
    private Button Reboot_RecoveryBTN;
    @FXML
    private Button Reboot_SideloadBTN;
    @FXML
    private Button Server_StartBTN;
    @FXML
    private Button Server_KillBTN;
    @FXML
    private Button Server_USBBTN;
    @FXML
    private Button Server_TCPBTN;
    @FXML
    private TextField Server_TCPField;

    @FXML
    private Label selectedOptionLabel;
    @FXML
    private Label selectedDeviceLabel;
    @FXML
    private Label selectionLabel;

    @FXML
    private Button executeButton;
    @FXML
    private Button executeMultipleButton;

    @FXML
    private ListView<String> deviceList;

    public ArrayList<String> cArray;

    private Command commandExecutor = new Command();


    public void start(Stage primaryStage){
    }

    @FXML
    public void initialize() {
        fillDeviceList();
        getSelectedDevice();

    }

    //The button life
        //option buttons

    @FXML
    private void Reboot_BootloaderBTN(ActionEvent event) {
        doINeedRoot = false;
        doINeedInput = false;
        selectedOptionLabel.setText(Reboot_BootloaderBTN.getText());
        whatToDo = "reboot-bootloader";
        whatIsHappening = "Rebooting " + whoAmI +"\nto Bootloader";
    }

    @FXML
    private void Reboot_SideloadBTN(ActionEvent event) {
        doINeedRoot = true;
        doINeedInput = false;
        selectedOptionLabel.setText(Reboot_SideloadBTN.getText());
        whatToDo = "reboot sideload";
        whatIsHappening = "Rebooting " + whoAmI +"\nto Recovery then Sideload";
    }

    @FXML
    private void Reboot_RecoveryBTN(ActionEvent event) {
        doINeedRoot = true;
        doINeedInput = false;
        selectedOptionLabel.setText(Reboot_RecoveryBTN.getText());
        whatToDo = "reboot-recovery";
        whatIsHappening = "Rebooting " + whoAmI + "\nto Recovery";
    }

    @FXML
    private void Server_StartBTN(ActionEvent event) {
        doINeedInput = false;
        doINeedRoot = false;
        selectedOptionLabel.setText(Server_StartBTN.getText());
        whatToDo = "start-server";
        whatIsHappening = "Restarting adb server";
    }

    @FXML
    private void Server_KillBTN(ActionEvent event) {
        doINeedInput = false;
        doINeedRoot = false;
        selectedOptionLabel.setText(Server_StartBTN.getText());
        whatToDo = "kill-server";
        whatIsHappening = "Killing server, you\nmust press Start before\nissuing further commands!";
    }

        //other buttons

    @FXML
    private void executeBTN(ActionEvent event) {
        System.out.println("Execute all the things!");
        if (whatToDo != null && !"Nothing, please select a device".equals(selectedDeviceLabel.getText()) && !"Nothing, please select a option".equals(selectedOptionLabel)) {
            try {
                if (doINeedRoot) {
                    commandExecutor.eCommand("adb -s " + whoAmI + " root");
                    TimeUnit.SECONDS.sleep(3);
                }
                System.out.println("adb -s " + whoAmI + " " +  whatToDo);
                commandExecutor.eCommand("adb -s " + whoAmI + " " + whatToDo);
                printlnOutput.setText("Everything seemed to have gone well!");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            printlnOutput.setText(whatIsHappening);
        }
        else {
            printlnOutput.setText("There was an error executing\nthis command, did you select\na device and option?");
        }
        doINeedRoot = false;
    }

    @FXML
    private void executeMultipleBTN(ActionEvent event) {
        System.out.println("Execute all the (multiple) things!");


    }



    @FXML
    private void refreshBTN(ActionEvent event) {
        deviceList.getItems().clear();
        deviceList.refresh();
        fillDeviceList();
        whatToDo = null;
        selectedOptionLabel.setText("Nothing, please select a option");
        selectedDeviceLabel.setText("Nothing, please select a device");
        doINeedRoot = false;
        printlnOutput.clear();
    }

    //end of button

    private void fillDeviceList() {
        Command cFillDeviceList = new Command();
        try {
            cFillDeviceList.eCommand("adb devices");
        } catch (IOException | InterruptedException e) {
            printlnOutput.setText("Something went wrong trying to load the list of devices, Sorry!");
            e.printStackTrace();

        }

        cArray = cFillDeviceList.getCArray();
        cArray.remove(0);
        cArray.remove(cArray.size()-1);
        if (cArray.size() == 0 || Objects.equals(cArray.get(0), "* daemon not running. starting it now on port 5037 *")) {
            cArray.add("No devices found");
        }


        for (int i=0; i<cArray.size(); i++) {
            deviceList.getItems().add(cArray.get(i));
            System.out.println(cArray.get(i));

        }
    }

    public void getSelectedDevice() {
        deviceList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Your action here
                System.out.println("Selected item: " + newValue);
                whoAmI = newValue.substring(0, newValue.length() - 7);
                selectedDeviceLabel.setText(newValue.substring(0, newValue.length() - 7));
            }
        });

    }







}
