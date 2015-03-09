package com.hubspot.dropwizard.guice.doubleinject;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.hubspot.dropwizard.guice.sample.DoubleInjectApplication;
import com.hubspot.dropwizard.guice.sample.HelloWorldConfiguration;
import com.hubspot.dropwizard.guice.sample.db.ConfigEnvDBI;
import com.jayway.restassured.RestAssured;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.get;
import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IntegrationTest {
    @ClassRule
    public static final DropwizardAppRule<HelloWorldConfiguration> RULE =
            new DropwizardAppRule<>(DoubleInjectApplication.class, resourceFilePath("hello-world.yml"));

    private static Injector injector;

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:" + RULE.getLocalPort();
        injector = ((DoubleInjectApplication) RULE.getApplication()).guiceBundle.getInjector().get();
    }

    @Test
    public void configuration_injection_in_resource() throws Exception {
        get("/hello-world").then().body("content", equalTo("Hello, Joe!"));
    }

    @Test
    public void value_passed_through_param_converter() throws Exception {
        get("/hello-world?name=Bob").then().body("content", equalTo("Hello, Bob!"));
    }

    @Test
    public void configuration_injection_in_healthcheck() throws Exception {
        get("admin/healthcheck").then().body("template.healthy", equalTo(true));
    }

    @Test
    public void request_injection_in_resource() throws Exception {
        get("/hello-world/ctx").then().statusCode(200);
    }

    @Test
    public void module_injection_in_resource() throws Exception {
        get("/hello-world/sample").then().body(equalTo("foo"));
    }

    @Test
    public void dependent_module_gets_injected_with_config_and_env() throws Exception {
        assertEquals("More data is: something",
                     injector.getInstance(Key.get(String.class, Names.named("dependent"))));
        assertEquals("hello-world",
                injector.getInstance(Key.get(String.class, Names.named("environmentName"))));
    }

    @Test
    public void eager_singleton_created_with_config_and_env() {
        // testing https://github.com/HubSpot/dropwizard-guice/issues/19
        assertNotNull(injector.getInstance(ConfigEnvDBI.class));
    }

}