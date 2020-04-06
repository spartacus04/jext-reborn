package me.tajam.jext.command.tab;

import java.util.List;

public interface Completor {

  public List<String> onComplete(String parameter);

}