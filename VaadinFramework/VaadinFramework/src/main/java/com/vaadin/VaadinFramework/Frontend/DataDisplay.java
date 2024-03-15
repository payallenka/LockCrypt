package com.vaadin.VaadinFramework.Frontend;

import com.vaadin.VaadinFramework.Model.KeysDetails;
import com.vaadin.VaadinFramework.Model.UserData;
import com.vaadin.VaadinFramework.Service.MaskingService;
import com.vaadin.VaadinFramework.Service.UserDetails;
import com.vaadin.VaadinFramework.Service.allKeys;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.VaadinFramework.Service.allKeys.*;

import java.util.ArrayList;
import java.util.List;

@Route("view")
public class DataDisplay extends HorizontalLayout {

    private final UserDetails userDetails;
    private final allKeys allkeys;
    private final MaskingService maskingService;
    private static int index = 0;
    private final List<String> dataToDisplay = new ArrayList<>();

    @Autowired
    public DataDisplay(MaskingService masking, UserDetails userDetails, allKeys allKeys) {
        this.userDetails = userDetails;
        this.allkeys = allKeys;
        this.maskingService = masking;
        initializeGrid();
        addBackOption();
    }

    private void initializeGrid() {
        Grid<UserData> userGrid = new Grid<>();
        userGrid.addColumn(UserData::getUsername).setHeader("Username");
        userGrid.addColumn(UserData::getAccountFor).setHeader("Account for");
        userGrid.setItems(userDetails.DataReterive());

        add(userGrid);

        for (UserData u : userDetails.DataReterive()) {
            dataToDisplay.add(u.getPassword());
        }

        for (KeysDetails k : allkeys.keysReterive()) {
            String originalPassword = maskingService.decryptPassword(k.getPasswordKey(), dataToDisplay.get(index));
            dataToDisplay.set(index, originalPassword);
            index++;
        }
        index = 0;

        Grid<String> keyGrid = new Grid<>();
        keyGrid.addColumn(String::toString).setHeader("Password");
        keyGrid.setItems(dataToDisplay);

        add(keyGrid);
    }

    private void addBackOption() {
        Button backButton = new Button("Back");
        backButton.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate("")));
        add(backButton);
    }


}
