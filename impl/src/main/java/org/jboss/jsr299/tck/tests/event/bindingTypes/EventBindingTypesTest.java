package org.jboss.jsr299.tck.tests.event.bindingTypes;

import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * @author Dan Allen
 */
@Artifact
@SpecVersion(spec="cdi", version="20091018")
public class EventBindingTypesTest extends AbstractJSR299Test
{
   @Test(groups = { "events" })
   @SpecAssertion(section = "10.1", id = "d")
   public void testEventBindingTypeTargetsMethodFieldParameterElementTypes()
   {
      Animal animal = new Animal();
      getCurrentManager().fireEvent(animal, new TameAnnotationLiteral());
      getInstanceByType(AnimalAssessment.class).classifyAsTame(animal);
   }

   @Test(groups = { "events" })
   @SpecAssertion(section = "10.1", id = "e")
   public void testEventBindingTypeTargetsFieldParameterElementTypes()
   {
      Animal animal = new Animal();
      getCurrentManager().fireEvent(animal, new WildAnnotationLiteral());
      getInstanceByType(AnimalAssessment.class).classifyAsWild(animal);
   }
   
   /**
    * This test ensures that an event binding type without runtime retention is effectively invisible
    */
   @Test(groups = "events")
   @SpecAssertion(section = "10.1", id = "f")
   public void testNonRuntimeBindingTypeIsNotAnEventBindingType()
   {
      DiscerningObserver observer = getInstanceByType(DiscerningObserver.class);
      observer.reset();
      EventEmitter emitter = getInstanceByType(EventEmitter.class);
      emitter.fireEvent();
      assert observer.getNumTimesAnyBindingTypeEventObserved() == 1;
      assert observer.getNumTimesNonRuntimeBindingTypeObserved() == 1;
      emitter.fireEventWithNonRuntimeBindingType();
      assert observer.getNumTimesAnyBindingTypeEventObserved() == 2;
      assert observer.getNumTimesNonRuntimeBindingTypeObserved() == 2;
   }

   @Test(groups = { "events"}, expectedExceptions = { IllegalArgumentException.class } )
   @SpecAssertion(section = "10.1", id = "f")
   public void testFireEventWithNonRuntimeBindingTypeFails()
   {
      getCurrentManager().fireEvent(new Animal(), new AnnotationLiteral<NonRuntimeBindingType>(){});
   }

   @Test(groups = { "events" }, expectedExceptions = { IllegalArgumentException.class } )
   @SpecAssertion(section = "10.1", id = "g")
   public void testFireEventWithNonBindingAnnotationsFails()
   {
      getCurrentManager().fireEvent(new Animal(), new AnnotationLiteral<NonBindingType>(){});
   }
   
   @Test(groups = "events")
   @SpecAssertion(section = "10.1", id = "i")
   public void testEventAlwaysHasAnyBinding()
   {
      Bean<Event<Animal>> animalEventBean = getUniqueBean(new TypeLiteral<Event<Animal>>() {}, new WildAnnotationLiteral());
      assert animalEventBean.getQualifiers().contains(new AnyLiteral());
      
      Bean<Event<Animal>> tameAnimalEventBean = getUniqueBean(new TypeLiteral<Event<Animal>>() {}, new TameAnnotationLiteral());
      assert tameAnimalEventBean.getQualifiers().contains(new AnyLiteral());
      
      Bean<Event<Animal>> wildAnimalEventBean = getUniqueBean(new TypeLiteral<Event<Animal>>() {}, new WildAnnotationLiteral());
      assert wildAnimalEventBean.getQualifiers().contains(new AnyLiteral());
   }
}
