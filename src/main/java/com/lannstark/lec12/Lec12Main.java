package com.lannstark.lec12;

import yoon.lec12.Person;

public class Lec12Main {

  public static void main(String[] args) {
    Person.Factory.newBaby("윤영진");
//    Person.Companion.newBaby("윤영진");
//    Person.newBaby("윤영진");
    moveSomething(new Movable() {
      @Override
      public void move() {

      }

      @Override
      public void fly() {

      }
    });

  }

  private static void moveSomething(Movable movable) {
    movable.move();
    movable.fly();
  }

}
