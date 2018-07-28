package com.github.vikramhalder.JavaORM.Error;

import java.sql.SQLException;
import java.util.ArrayList;

public class ErrorMessage {
    static ArrayList<String> ret=new ArrayList<>();
    public static  String SQLError(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                if (ignoreSQLException(((SQLException)e).getSQLState()) == false) {
                    ArrayList<String> err=getExceptionMessageChain(e);
                    ret.add(String.join(",\n",err));
                }
            }
        }
        return String.join("\n",ret);
    }

    private static ArrayList<String> getExceptionMessageChain(Throwable throwable) {
        ArrayList<String> result = new ArrayList<String>();
        while (throwable != null) {
            result.add(throwable.getMessage());
            throwable = throwable.getCause();
        }
        return result;
    }

    private static boolean ignoreSQLException(String sqlState) {

        if (sqlState == null) {
            System.out.println("The SQL state is not defined!");
            return false;
        }

        // X0Y32: Jar file already exists in schema
        if (sqlState.equalsIgnoreCase("X0Y32"))
            return true;

        // 42Y55: Table already exists in schema
        if (sqlState.equalsIgnoreCase("42Y55"))
            return true;

        return false;
    }
}
