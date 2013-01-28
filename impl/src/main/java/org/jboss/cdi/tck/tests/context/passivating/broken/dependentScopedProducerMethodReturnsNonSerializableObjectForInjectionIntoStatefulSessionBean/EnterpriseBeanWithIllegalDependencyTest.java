/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.tests.context.passivating.broken.dependentScopedProducerMethodReturnsNonSerializableObjectForInjectionIntoStatefulSessionBean;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_VALIDATION;

import javax.enterprise.inject.IllegalProductException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class EnterpriseBeanWithIllegalDependencyTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseBeanWithIllegalDependencyTest.class).build();
    }

    @Test(groups =  INTEGRATION)
    @SpecAssertion(section = PASSIVATION_VALIDATION, id = "fab")
    public void test() {
        try {
            getInstanceByType(MaarianHaminaLocal_Broken.class).ping();
        } catch (Throwable t) {
            assert isThrowablePresent(IllegalProductException.class, t);
            return;
        }
        assert false;
    }
}