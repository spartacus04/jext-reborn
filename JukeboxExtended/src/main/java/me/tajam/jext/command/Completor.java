package me.tajam.jext.command;

import java.util.List;

interface Completor {

  public List<String> onComplete(String parameter);

}