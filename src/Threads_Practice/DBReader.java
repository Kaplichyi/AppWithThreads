package Threads_Practice;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import org.sqlite.JDBC;

public class DBReader {
	
	public String getItems() throws ClassNotFoundException{
		Connection con = null;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:database.db");
		}
		catch (SQLException e){
			System.out.println("Не удалось установить соединение");
			e.printStackTrace();
			return null;
		}
		System.out.println("Соединение установлено");
		
		try {
			Statement statment = con.createStatement();
			String query = "SELECT ID, Name, Price FROM Items";
			ResultSet rs = statment.executeQuery(query);
			String items = "";
			while(rs.next())
			{
                int ID = rs.getInt(1);
                String Name = rs.getString(2);
                int Price = rs.getInt(3);
				items += ID + " " + Name + " " + Price + "\n";
			}
            con.close();
    		return items;
		}
		catch (SQLException e){
			return e.toString();
		}
	}
	
	public Integer countItems() throws ClassNotFoundException{
		Connection con = null;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:database.db");
		}
		catch (SQLException e){
			System.out.println("Не удалось установить соединение");
			e.printStackTrace();
			return null;
		}
		System.out.println("Соединение установлено");
		
		try {
			Statement statment = con.createStatement();
			String query = "SELECT COUNT(*) as Count FROM Items";
			ResultSet rs = statment.executeQuery(query);
			rs.next();
			Integer count = null;
			count = rs.getInt(1);
            con.close();
    		return count;
		}
		catch (SQLException e){
			return -1;
		}
	}
}
