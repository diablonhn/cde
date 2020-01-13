package org.nammy.cde.main;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CdeMainTest {
  private InputStream oldInputStream;
  private PrintStream oldOutputStream;

  @Before
  public void onBefore() {
    oldInputStream = System.in;
    oldOutputStream = System.out;
  }

  @After
  public void onAfter() {
    System.setIn(oldInputStream);
    System.setOut(oldOutputStream);
  }

  @Test
  public void test_main() throws Exception {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("input0.txt");
    System.setIn(is);

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    PrintStream os = new PrintStream(bos);
    System.setOut(os);

    CdeMain.main(new String[0]);
    os.close();

    String output = new String(bos.toByteArray());
  }
}
