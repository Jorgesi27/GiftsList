package com.example.application.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class SessionStorage {

    private String selectedListaId;

    public String getSelectedListaId() {
        return selectedListaId;
    }

    public void setSelectedListaId(String selectedListaId) {
        this.selectedListaId = selectedListaId;
    }
}
