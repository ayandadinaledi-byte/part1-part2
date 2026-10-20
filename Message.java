/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.part1chatapp;

import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class Message {

    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private int messageNumber;
    private static int totalSent = 0;

    public Message() {
    }

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash(messageID, messageNumber, messageText);
    }

    public String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }

    public boolean checkMessageID(String id) {
        return id.length() == 10;
    }

    public String checkRecipientCell(String cell) {
        if (cell == null) {
            return "Cell phone number is incorrectly formatted or does not contain an international code.";
        }
        if (cell.startsWith("+") == false) {
            return "Cell phone number is incorrectly formatted or does not contain an international code.";
        }
        if (cell.length() < 10 || cell.length() > 13) {
            return "Cell phone number is incorrectly formatted or does not contain an international code.";
        }
        String digits = cell.substring(1);
        if (digits.matches("\\d+") == false) {
            return "Cell phone number is incorrectly formatted or does not contain an international code.";
        }
        return "Cell phone number successfully captured.";
    }

    public String checkMessageLength(String message) {
        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess;
        }
    }

    public String createMessageHash(String id, int num, String message) {
        String trimmed = message.trim();
        int firstSpace = trimmed.indexOf(' ');
        int lastSpace = trimmed.lastIndexOf(' ');
        String firstWord;
        String lastWord;
        if (firstSpace == -1) {
            firstWord = trimmed;
        } else {
            firstWord = trimmed.substring(0, firstSpace);
        }
        if (lastSpace == -1) {
            lastWord = trimmed;
        } else {
            lastWord = trimmed.substring(lastSpace + 1);
        }
        String hash = id.substring(0, 2) + ":" + num + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    public String sendMessage(String option) {
        if (option.equals("1")) {
            return "Message successfully sent.";
        }
        if (option.equals("2")) {
            return "Message successfully stored.";
        }
        return "Message disregarded.";
    }

    public String processMessage(String option) {
        String result = sendMessage(option);
        if (option.equals("1")) {
            totalSent++;
        }
        if (option.equals("2")) {
            storeMessage(messageID, recipient, messageText, messageHash);
        }
        return result;
    }

    public String storeMessage(String id, String recipient, String message, String hash) {
        String json = "{\n  \"messageID\": \"" + escape(id) + "\",\n  \"recipient\": \"" + escape(recipient) + "\",\n  \"message\": \"" + escape(message) + "\",\n  \"messageHash\": \"" + escape(hash) + "\"\n}\n";
        try (FileWriter file = new FileWriter("messages.json", true)) {
            file.write(json);
            return "Message stored in messages.json.";
        } catch (IOException e) {
            return "Error storing message: " + e.getMessage();
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public String getMessageDetails() {
        return "Message ID: " + messageID + "\nMessage Hash: " + messageHash + "\nRecipient: " + recipient + "\nMessage: " + messageText;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public static int returnTotalMessages() {
        return totalSent;
    }

    public static void resetTotal() {
        totalSent = 0;
    }

    public String searchByMessageID(String id) {
        return searchStored("messageID", id);
    }

    public String searchByRecipient(String recipient) {
        return searchStored("recipient", recipient);
    }

    private String searchStored(String field, String value) {
        StringBuilder results = new StringBuilder();
        String id = "";
        String rec = "";
        String msg = "";
        String hash = "";
        try (BufferedReader br = new BufferedReader(new FileReader("messages.json"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("\"messageID\"")) {
                    id = extractValue(line);
                }
                if (line.startsWith("\"recipient\"")) {
                    rec = extractValue(line);
                }
                if (line.startsWith("\"message\"")) {
                    msg = extractValue(line);
                }
                if (line.startsWith("\"messageHash\"")) {
                    hash = extractValue(line);
                }
                if (line.equals("}")) {
                    boolean match = false;
                    if (field.equals("messageID")) {
                        if (id.equals(value)) match = true;
                    }
                    if (field.equals("recipient")) {
                        if (rec.equals(value)) match = true;
                    }
                    if (match) {
                        results.append("Message ID: ").append(id).append("\nMessage Hash: ").append(hash).append("\nRecipient: ").append(rec).append("\nMessage: ").append(msg).append("\n\n");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            return "No stored messages found.";
        } catch (IOException e) {
            return "Error reading messages.json: " + e.getMessage();
        }
        if (results.length() == 0) {
            return "No matching message found.";
        }
        return results.toString();
    }

    private String extractValue(String line) {
        int start = line.indexOf(": \"") + 3;
        int end = line.lastIndexOf("\"");
        String value = line.substring(start, end);
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    public static void startQuickChat(Scanner input) {
        System.out.println("\nWelcome to QuickChat.");
        boolean running = true;
        while (running) {
            System.out.println("\n1) Send Messages\n2) Show recently sent messages\n3) Search stored messages\n4) Quit");
            System.out.print("Choose an option: ");
            String choice = input.nextLine();
            if (choice.equals("1")) {
                sendMessages(input);
            }
            if (choice.equals("2")) {
                System.out.println("Coming Soon - Total sent: " + returnTotalMessages());
            }
            if (choice.equals("3")) {
                searchMessages(input);
            }
            if (choice.equals("4")) {
                System.out.println("Goodbye.");
                running = false;
            }
            if (choice.equals("1") == false && choice.equals("2") == false && choice.equals("3") == false && choice.equals("4") == false) {
                System.out.println("Invalid option.");
            }
        }
    }

    public static void searchMessages(Scanner input) {
        Message searcher = new Message();
        System.out.println("1) Search by Message ID\n2) Search by Recipient");
        System.out.print("Choose an option: ");
        String option = input.nextLine();
        if (option.equals("1")) {
            System.out.print("Enter Message ID: ");
            System.out.println(searcher.searchByMessageID(input.nextLine()));
        }
        if (option.equals("2")) {
            System.out.print("Enter recipient cell number: ");
            System.out.println(searcher.searchByRecipient(input.nextLine()));
        }
    }

    private static void sendMessages(Scanner input) {
        System.out.print("How many messages do you want to send? ");
        int count = Integer.parseInt(input.nextLine());
        for (int i = 1; i <= count; i++) {
            System.out.print("Enter recipient (+27...): ");
            String rec = input.nextLine();
            System.out.print("Enter message (max 250 chars): ");
            String txt = input.nextLine();
            Message msg = new Message(i, rec, txt);
            System.out.println(msg.checkRecipientCell(rec));
            System.out.println(msg.checkMessageLength(txt));
            System.out.println(msg.getMessageDetails());
            System.out.print("1) Send 2) Store 3) Disregard: ");
            String opt = input.nextLine();
            System.out.println(msg.processMessage(opt));
        }
        System.out.println("Total sent: " + returnTotalMessages());
    }
}