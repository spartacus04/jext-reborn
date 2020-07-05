package me.tajam.jext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Log {

  private String title;
  private ChatColor themeColor;
  private List<Token> tokens;

  public Log(String title) {
    this.tokens = new ArrayList<>();
    this.themeColor = ChatColor.WHITE;
    this.title = title;
  }

  public Log() {
    this("Jext");
  }

  // Set message theme to normal and add a header(White)
  public Log norm() {
    this.themeColor = ChatColor.RESET;
    return head();
  }

  // Set message theme to success and add a header (Green)
  public Log okay() {
    this.themeColor = ChatColor.GREEN;
    return head();
  }

  // Set message theme to warning and add a header (Yellow)
  public Log warn() {
    this.themeColor = ChatColor.YELLOW;
    return head();
  }

  // Set message theme to error and add a header (Red)
  public Log eror() {
    this.themeColor = ChatColor.RED;
    return head();
  }

  // Set message theme to infomative and add a header (Blue)
  public Log info() {
    this.themeColor = ChatColor.BLUE;
    return head();
  }

  // Add header pattern
  private Log head() {
    return rst("[").t(title).rst("] ");
  }

  // Add theme-coloured text
  public Log t(String themeColoredMessage) {
    this.tokens.add(new Token(themeColoredMessage, themeColor));
    return this;
  }

  public Log t() {
    this.tokens.add(new Token(themeColor));
    return this;
  }

  // Add green-coloured text
  public Log g(String greenMessage) {
    this.tokens.add(new Token(greenMessage, ChatColor.GREEN));
    return this;
  }

  public Log g() {
    this.tokens.add(new Token(ChatColor.GREEN));
    return this;
  }

  // Add yellow-coloured text
  public Log y(String yellowMessage) {
    this.tokens.add(new Token(yellowMessage, ChatColor.YELLOW));
    return this;
  }

  public Log y() {
    this.tokens.add(new Token(ChatColor.YELLOW));
    return this;
  }

  // Add gold-coloured text
  public Log o(String goldenMessage) {
    this.tokens.add(new Token(goldenMessage, ChatColor.GOLD));
    return this;
  }

  public Log o() {
    this.tokens.add(new Token(ChatColor.GOLD));
    return this;
  }

  // Add red-coloured text
  public Log r(String redMessage) {
    this.tokens.add(new Token(redMessage, ChatColor.RED));
    return this;
  }

  public Log r() {
    this.tokens.add(new Token(ChatColor.RED));
    return this;
  }

  // Add blue-coloured text
  public Log b(String blueMessage) {
    this.tokens.add(new Token(blueMessage, ChatColor.BLUE));
    return this;
  }

  public Log b() {
    this.tokens.add(new Token(ChatColor.BLUE));
    return this;
  }

  // Add teal-coloured text
  public Log a(String aquaMessage) {
    this.tokens.add(new Token(aquaMessage, ChatColor.AQUA));
    return this;
  }

  public Log a() {
    this.tokens.add(new Token(ChatColor.AQUA));
    return this;
  }

  // Add dark teal-coloured text
  public Log da(String darkAquaMessage) {
    this.tokens.add(new Token(darkAquaMessage, ChatColor.DARK_AQUA));
    return this;
  }

  public Log da() {
    this.tokens.add(new Token(ChatColor.DARK_AQUA));
    return this;
  }

  // Add purple-coloured text
  public Log p(String purpleMessage) {
    this.tokens.add(new Token(purpleMessage, ChatColor.LIGHT_PURPLE));
    return this;
  }

  public Log p() {
    this.tokens.add(new Token(ChatColor.LIGHT_PURPLE));
    return this;
  }

  // Add dark purple-coloured text
  public Log dp(String darkPurpleMessage) {
    this.tokens.add(new Token(darkPurpleMessage, ChatColor.DARK_PURPLE));
    return this;
  }

  public Log dp() {
    this.tokens.add(new Token(ChatColor.DARK_PURPLE));
    return this;
  }

  // Add gray-coloured text
  public Log gr(String grayMessage) {
    this.tokens.add(new Token(grayMessage, ChatColor.GRAY));
    return this;
  }

  public Log gr() {
    this.tokens.add(new Token(ChatColor.GRAY));
    return this;
  }

  // Add magic-coloured text
  public Log mag(String magicMessage) {
    this.tokens.add(new Token(magicMessage, ChatColor.MAGIC));
    return this;
  }

  public Log mag() {
    this.tokens.add(new Token(ChatColor.MAGIC));
    return this;
  }

  // Reset colour and add text
  public Log rst(String plainColoredMessage) {
    this.tokens.add(new Token(plainColoredMessage, ChatColor.RESET));
    return this;
  }

  public Log rst() {
    this.tokens.add(new Token(ChatColor.RESET));
    return this;
  }

  // Send the message
  public void send(Object... objects) {
    final Queue<Object> objectQueue = new LinkedList<>(Arrays.asList(objects));
    Bukkit.getConsoleSender().sendMessage(constructMessage(objectQueue));
  }

  public void send(CommandSender target, Object... objects) {
    final Queue<Object> objectQueue = new LinkedList<>(Arrays.asList(objects));
    String message = constructMessage(objectQueue);
    if (target instanceof Player) {
      Player player = (Player)target;
      player.sendMessage(message);
    } else {
      Bukkit.getConsoleSender().sendMessage(message);
    }
  }

  // Get the string
  public String text(Object... objects) {
    final Queue<Object> objectQueue = new LinkedList<>(Arrays.asList(objects));
    return constructMessage(objectQueue);
  }

  private String constructMessage(Queue<Object> parameters) {
    String message = "";
    for (Token token : tokens) {
      message += token.toString(parameters);
    }
    return message;
  }

  private class Token {

    private String message;
    private ChatColor color;
    private boolean parameter;

    private Token(String message, ChatColor color, boolean parameter) {
      this.message = message;
      this.color = color;
      this.parameter = parameter;
    }

    public Token(String message, ChatColor color) {
      this(message, color, false);
    }

    public Token(ChatColor color) {
      this("", color, true);
    }

    public String toString(Queue<Object> objects) {
      if (parameter) {
        final Object object = objects.poll();
        String replacement = "";
        if (object != null) replacement = object.toString();
        return this.color + replacement;
      } else {
        return this.color + this.message;
      }
    }

  }

}