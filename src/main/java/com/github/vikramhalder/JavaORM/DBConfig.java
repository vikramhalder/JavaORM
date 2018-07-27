package com.github.vikramhalder.JavaORM;

public class DBConfig {
    private String host;
    private String port;
    private String username;
    private String password;
    private Boolean viewQuery=false;
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getViewQuery() {
        return viewQuery;
    }

    public void setViewQuery(Boolean viewQuery) {
        this.viewQuery = viewQuery;
    }
}
