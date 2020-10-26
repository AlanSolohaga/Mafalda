package com.project.mafalda.model;

public class User {
    private static User mIntance;
    private static String usuario;
    private static String ID;
    private static String TOKEN;

    private User(String usuario, String ID,String TOKEN){
        this.usuario = usuario;
        this.ID = ID;
        this.TOKEN = TOKEN;
    }
    private User(){}

    public static synchronized User getInstance(){
        if(mIntance == null){
            mIntance = new User(usuario, ID,TOKEN);
        }
        return mIntance;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        User.TOKEN = TOKEN;
    }
}
