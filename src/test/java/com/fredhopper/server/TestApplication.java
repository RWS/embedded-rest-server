package com.fredhopper.server;

import java.util.Random;

class TestApplication {

  private final Random random;

  public TestApplication(long seed) {
    this.random = new Random(seed);
  }

  public int doBusiness() {
    return this.random.nextInt();
  }

}
