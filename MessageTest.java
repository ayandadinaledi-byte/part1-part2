/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.part1chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testMessageLengthSuccess() {
        Message m = new Message();
        assertEquals("Message ready to send.", m.checkMessageLength("Hi Mike, can you join us?"));
    }

    @Test
    public void testMessageLengthTooLong() {
        Message m = new Message();
        String longMsg = "a".repeat(260);
        assertEquals("Message exceeds 250 characters by 10", m.checkMessageLength(longMsg));
    }

    @Test
    public void testRecipientValid() {
        Message m = new Message();
        assertEquals("Cell phone number successfully captured.", m.checkRecipientCell("+27718693002"));
    }

    @Test
    public void testRecipientInvalid() {
        Message m = new Message();
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code.",
                     m.checkRecipientCell("08575975889"));
    }

    @Test
    public void testRecipientInvalidNoPlus() {
        Message m = new Message();
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code.",
                     m.checkRecipientCell("27718693002"));
    }

    @Test
    public void testMessageHash() {
        Message m = new Message();
        assertEquals("00:0:HITONIGHT", m.createMessageHash("0012345678", 0, "Hi tonight"));
    }

    @Test
    public void testMessageHashTwoWords() {
        Message m = new Message();
        // First word Hi, Last word Mike -> HIMIKE
        assertEquals("00:1:HIMIKE", m.createMessageHash("0012345678", 1, "Hi Mike"));
    }

    @Test
    public void testMessageIDLength() {
        Message m = new Message(1, "+27718693002", "Hello there");
        assertEquals(10, m.getMessageID().length());
        assertTrue(m.checkMessageID(m.getMessageID()));
    }

    @Test
    public void testSendOptions() {
        Message m = new Message();
        assertEquals("Message successfully sent.", m.sendMessage("1"));
        assertEquals("Message successfully stored.", m.sendMessage("2"));
        assertEquals("Message disregarded.", m.sendMessage("3"));
    }

    @Test
    public void testTotalMessages() {
        Message.resetTotal();
        Message m1 = new Message(1, "+27718693002", "One");
        Message m2 = new Message(2, "+27718693002", "Two");
        m1.processMessage("1");
        m2.processMessage("1");
        assertEquals(2, Message.returnTotalMessages());
        Message.resetTotal(); // clean up
    }
}