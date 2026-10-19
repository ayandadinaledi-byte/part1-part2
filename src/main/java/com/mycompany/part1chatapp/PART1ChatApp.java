/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.part1chatapp;
import java.util.Scanner;
/**
 *
 * @author Student
 */
public class PART1ChatApp {

    public static void main(String[] args) {
         Scanner input = new Scanner(System.in);

        System.out.println("--- REGISTER ---");
        System.out.print("Enter First Name: ");
        String firstName = input.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = input.nextLine();

        System.out.print("Enter Username: ");
        String username = input.nextLine();

        System.out.print("Enter Password: ");
        String password = input.nextLine();

        System.out.print("Enter Cell Phone Number with +27: ");
        String cell = input.nextLine();

        // Create Login object
        Login login = new Login(firstName, lastName, username, password, cell);

        // Check registration
        String result = login.registerUser();
        System.out.println(result);

        // If registration passed, go to login - from your 2. instructions
        if (result.equals("Cell phone number successfully added.")) {
            System.out.println("\n--- LOGIN ---");
            System.out.print("Enter Username: ");
            String loginUsername = input.nextLine();

            System.out.print("Enter Password: ");
            String loginPassword = input.nextLine();

            // a. If entered username and password are correct
            // b. Else if incorrect
            boolean loggedIn = login.loginUser(loginUsername, loginPassword);
            String message = login.returnLoginStatus(loginUsername, loginPassword);
            System.out.println(message);
            //Added Login class from Part 1
        }
    }
}
        
    

