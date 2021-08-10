package gateways;

import com.google.gson.*;
import entities.Template;

import static gateways.GatewayUtility.*;

// TODO* Generics

// Data Types Accepted: primitives + String, LocalDateTime, List<E>, enums, Class<?>
// TODO javadocs: specify input for getter as precondition

public class TemplateGateway extends EntityGateway<Template> {
    public TemplateGateway(String path) {
        super(Template.class, path);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        gsonBuilder.registerTypeAdapter(Class.class, new ClassSerializer());
        gsonBuilder.registerTypeAdapter(Class.class, new ClassDeserializer());
        return gsonBuilder;
    }

    @Override
    protected String getElementId(Template template) {
        return template.getTemplateId() + "-" + template.getFileVersionNumber();
    }
}
