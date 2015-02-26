package com.hubspot.dropwizard.guice.sample.jersey;

import com.codahale.metrics.annotation.Timed;
import com.hubspot.dropwizard.guice.ConfigData.Config;
import com.hubspot.dropwizard.guice.sample.HelloWorldConfiguration;
import com.hubspot.dropwizard.guice.sample.config.SubConfig;
import com.hubspot.dropwizard.guice.sample.guice.ConfigData;
import com.hubspot.dropwizard.guice.sample.core.Saying;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.util.concurrent.atomic.AtomicLong;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

	final Logger logger = LoggerFactory.getLogger(HelloWorldResource.class);

    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
    private final String sample;
    private final HttpHeaders headers;

    @Inject
    private Request ctx;

    @Inject
    @Config("subConfig.moreData")
    private String moreData1;
    private String moreData2;
    @Inject
    @Config(root = HelloWorldConfiguration.class, value = "subConfig.moreData")
    private String moreData3;
    @Inject
    @Config(root = SubConfig.class, value = "moreData")
    private String moreData4;
    @Inject
    @Config("subConfig")
    private SubConfig subConfig;

    @Inject
    public HelloWorldResource(ConfigData config,
                              @Config("sample") String sample,
                              @Config("subConfig.moreData") String moreData2,
                              HttpHeaders headers) {
    	logger.info("Creating a new HelloWorldResource!");
        this.template = config.getTemplate();
        this.defaultName = config.getDefaultName();
        this.counter = new AtomicLong();
        this.sample = sample;
        this.moreData2 = moreData2;
        this.headers = headers;
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<ParamInput> nameInput, @Context ServletContext context) {
    	logger.info("User-Agent: " + headers.getRequestHeader("User-Agent"));
    	logger.info(Integer.toString(ctx.hashCode()));

        String name = (nameInput.isPresent()) ? nameInput.get().data : defaultName;
        return new Saying(counter.incrementAndGet(), String.format(template, name));
    }

    @GET
    @Timed
    @Path("ctx")
    public Response checkCTX() {
        if(ctx != null) return Response.ok().build();
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Timed
    @Path("sample")
    public String getSample() {
        return sample;
    }

    @GET
    @Timed
    @Path("moredata1")
    public String getMoreData1() {
        return moreData1;
    }

    @GET
    @Timed
    @Path("moredata2")
    public String getMoreData2() {
        return moreData2;
    }

    @GET
    @Timed
    @Path("moredata3")
    public String getMoreData3() {
        return moreData3;
    }

    @GET
    @Timed
    @Path("moredata4")
    public String getMoreData4() {
        return moreData4;
    }

    @GET
    @Timed
    @Path("moredata5")
    public String getMoreData5() {
        return subConfig.getMoreData();
    }

    @PreDestroy
    void destroy() {
    	logger.info("Destroying HelloWorldResource... :(");
    }
}