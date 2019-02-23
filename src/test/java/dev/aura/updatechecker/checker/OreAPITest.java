package dev.aura.updatechecker.checker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import dev.aura.updatechecker.AuraUpdateChecker;
import io.specto.hoverfly.junit.core.SimulationSource;
import io.specto.hoverfly.junit.rule.HoverflyRule;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.spongepowered.api.plugin.PluginContainer;

public class OreAPITest {
  @ClassRule
  public static HoverflyRule hoverflyRule =
      HoverflyRule.inSimulationMode(SimulationSource.defaultPath("simulation.json"));

  @Before
  @After
  public void resetCounter() {
    OreAPI.resetErrorCounter();
  }

  @Test
  public void availablityTest() {
    final PluginContainer existingContainer = new DummyPluginContainer(AuraUpdateChecker.ID);
    final PluginContainer nonExistingContainer =
        new DummyPluginContainer("thisIDdoesntexistbfjsdgbhjbhghhsfgdsghfh");

    assertTrue("Expected updatechecker to be available", OreAPI.isOnOre(existingContainer));
    assertFalse(
        "Expected thisIDdoesntexistbfjsdgbhjbhghhsfgdsghfh not to be available",
        OreAPI.isOnOre(nonExistingContainer));
    assertEquals("No errors should have happend", 0, OreAPI.getErrorCounter());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void constructorTest() throws Throwable {
    try {
      Constructor<OreAPI> contructor = OreAPI.class.getDeclaredConstructor();
      contructor.setAccessible(true);
      contructor.newInstance();
    } catch (InvocationTargetException e) {
      if (e.getCause().getClass() == UnsupportedOperationException.class) throw e.getCause();
      else throw e;
    }
  }

  @Test
  public void errorTest()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException,
          IllegalAccessException {
    final int count = 10;
    final PluginContainer errorContainer = new DummyPluginContainer("Spaces are bad&!?&=?");

    for (int i = 0; i < count; ++i) {
      assertFalse(
          "Expected \"Spaces are bad&!?&=?\" not to be available", OreAPI.isOnOre(errorContainer));
    }

    assertEquals(count + " errors should have happend", count, OreAPI.getErrorCounter());
  }
}
