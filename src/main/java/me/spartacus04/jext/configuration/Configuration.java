package me.spartacus04.jext.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Configuration implements Configurable {
  
  private Configuration parent;
  private List<Configurable> childs;
  private int level;

  public Configuration() {
    this.childs = new ArrayList<>();
    this.level = 0;
  }

  public int getLevel() {
    return this.level;
  }

  public Configurable getParent() {
    return this.parent;
  }

  private void setParent(Configuration parent) {
    this.parent = parent;
    this.level = this.parent.level + 1;
  }

  public void addChild(Configuration child) {
    child.setParent(this);
    childs.add(child);
  }

  public Iterator<Configurable> getChildsIterable() {
    return this.childs.iterator();
  }

}
