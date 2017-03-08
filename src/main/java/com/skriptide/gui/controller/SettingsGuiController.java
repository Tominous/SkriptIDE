package com.skriptide.gui.controller;

import com.skriptide.Main;
import com.skriptide.include.Api;
import com.skriptide.include.Skript;
import com.skriptide.theme.ThemeImpl;
import com.skriptide.util.IDESettings;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;

/**
 * Created by yannh on 15.02.2017.
 */
public class SettingsGuiController {
    IDESettings s = null;
    @FXML
    private ComboBox languageComboBox;
    @FXML
    private ComboBox styleComboBox;
    @FXML
    private TextField autoSaveField;
    @FXML
    private ComboBox<String> autoSaveComboBox;
    @FXML
    private CheckBox updatesCheckBox;
    @FXML
    private CheckBox skriptMsgMarkCheck;
    @FXML
    private CheckBox startSrvAfterCreateCheck;
    @FXML
    private CheckBox clearPrsAfterServerStopCheck;
    @FXML
    private CheckBox outputSaveCheck;
    @FXML
    private CheckBox minifyOutputCheck;
    @FXML
    private CheckBox codeManageCheck;
    @FXML
    private CheckBox highlightCheck;
    @FXML
    private CheckBox autoCompleteCheck;
    @FXML
    private TreeTableView settingsTable;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cnlBtn;
    @FXML
    private Button applyBtn;
    @FXML
    private ListView<String> sList;
    @FXML
    private TabPane tabPane;
    @FXML
    private CheckBox complexeCheck;
    @FXML
    private CheckBox errorSysCheck;
    @FXML
    private ComboBox<String> errorSkComboBox;
    @FXML
    private ComboBox<String> errorApiComboBox;

    private HashMap<String, Api> apis;
    private HashMap<String, Skript> skripts;


    public void init() {

        apis = new HashMap<>();
        skripts = new HashMap<>();

        errorSkComboBox.getItems().clear();
        errorApiComboBox.getItems().clear();

        styleComboBox.getItems().addAll("Light","Dark");

        sList.getItems().addAll("Main Settings", "Server", "Code", "Error Handling");

        sList.setOnMouseReleased(event -> {

            tabPane.getSelectionModel().select(sList.getSelectionModel().getSelectedIndex());

        });
        s = Main.settings;

        autoSaveComboBox.getItems().addAll("Seconds", "Minutes", "Hours");

        switch (String.valueOf(s.getDelayMultiPlier())) {
            case "1000":
                autoSaveComboBox.getSelectionModel().select("Seconds");
                break;
            case "60000":
                autoSaveComboBox.getSelectionModel().select("Minutes");
                break;
            case "3600000":
                autoSaveComboBox.getSelectionModel().select("Hours");
                break;

        }

        errorSysCheck.setSelected(s.isErrorSys());
        styleComboBox.getSelectionModel().select(s.getTheme());
        autoSaveField.setText(String.valueOf(s.getDelay()));
        skriptMsgMarkCheck.setSelected(s.isMarkMessage());
        startSrvAfterCreateCheck.setSelected(s.isStartAfterCreate());
        clearPrsAfterServerStopCheck.setSelected(s.isClearAfterStop());
        outputSaveCheck.setSelected(s.isSaveOutput());
        minifyOutputCheck.setSelected(s.isMinifyOutput());
        codeManageCheck.setSelected(s.isCodeManagement());
        highlightCheck.setSelected(s.isHighlight());
        autoCompleteCheck.setSelected(s.isAutoComplete());
        complexeCheck.setSelected(s.isComplexeAutoComplete());

        applyBtn.setOnAction(event -> save());

        saveBtn.setOnAction(event -> {
            save();
            saveBtn.getScene().getWindow().hide();
        });
        cnlBtn.setOnAction(event -> saveBtn.getScene().getWindow().hide());

        for(Api api : Main.manager.getApis().values()) {

            errorApiComboBox.getItems().add(api.getName() + " - " + api.getVersion());
            apis.put(api.getName() + " - " + api.getVersion(), api);
            if(s.getErrorApi().equalsIgnoreCase(api.getName())) {
                errorApiComboBox.getSelectionModel().select(api.getName() + " - " + api.getVersion());
            }
        }
        for(Skript api : Main.manager.getSkripts().values()) {

            errorSkComboBox.getItems().add(api.getName() + " - " + api.getVersion());
            skripts.put(api.getName() + " - " + api.getVersion(), api);
            if(s.getErrorSkript().equalsIgnoreCase(api.getName())) {
                errorSkComboBox.getSelectionModel().select(api.getName() + " - " + api.getVersion());
            }
        }
    }

    private void save() {

        if(s.getTheme().getName().equals("Light") && styleComboBox.getSelectionModel().getSelectedItem().equals("Dark")) {
            Main.sceneManager.infoCheck("Info", "Please notice:", "Currently with the Dark Theme, the highlighting will not work during a issue in the Highlighting API", Alert.AlertType.INFORMATION);
        }
        s.setCodeManagement(codeManageCheck.isSelected());
        s.setHighlight(highlightCheck.isSelected());
        s.setAutoComplete(autoCompleteCheck.isSelected());
        s.setMinifyOutput(minifyOutputCheck.isSelected());
        s.setMarkMessage(skriptMsgMarkCheck.isSelected());
        s.setStartAfterCreate(startSrvAfterCreateCheck.isSelected());
        s.setClearAfterStop(clearPrsAfterServerStopCheck.isSelected());
        s.setSaveOutput(outputSaveCheck.isSelected());
        s.setDelay(Long.parseLong(autoSaveField.getText()));
        s.setComplexeAutoComplete(complexeCheck.isSelected());
        s.setTheme((ThemeImpl) new ThemeImpl().load("Dark"));
        String sel = autoSaveComboBox.getSelectionModel().getSelectedItem();
        s.setErrorSys(errorSysCheck.isSelected());
        if(errorApiComboBox.getSelectionModel().getSelectedItem() != null) {
            s.setErrorApi(apis.get(errorApiComboBox.getSelectionModel().getSelectedItem()).getName());
        }
        if(errorSkComboBox.getSelectionModel().getSelectedItem() != null) {
            s.setErrorSkript(skripts.get(errorSkComboBox.getSelectionModel().getSelectedItem()).getName());
        }

        switch (sel) {

            case "Seconds":
                s.setDelayMultiPlier(1000);
                break;
            case "Minutes":
                s.setDelayMultiPlier(60000);
                break;
            case "Hours":
                s.setDelayMultiPlier(3600000);
                break;
        }

        s.saveSettings();
        Main.sceneManager.infoCheck("Info", "Please notice:", "Some changed, need a restart of the IDE!", Alert.AlertType.INFORMATION);

    }
}
