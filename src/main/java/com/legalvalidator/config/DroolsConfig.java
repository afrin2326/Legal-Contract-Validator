package com.legalvalidator.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
public class DroolsConfig {

    @Bean
    public KieContainer kieContainer() {
        System.out.println("📦 Initializing Drools KieContainer...");

        try {
            KieServices kieServices = KieServices.Factory.get();
            KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:rules/*.drl");

            if (resources.length == 0) {
                System.out.println("⚠️ No DRL files found at classpath:rules/*.drl");
                return null;
            }

            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                if (fileName == null) continue;

                String droolsPath = "src/main/resources/rules/" + fileName;
                kieFileSystem.write(
                    droolsPath,
                    ResourceFactory.newClassPathResource("rules/" + fileName, "UTF-8")
                );
                System.out.println("✅ Loaded DRL file: " + fileName);
            }

            KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
            kieBuilder.buildAll();

            if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
                System.out.println("❌ Drools compilation errors:");
                kieBuilder.getResults().getMessages(Message.Level.ERROR)
                        .forEach(m -> System.out.println(m.getText()));
                return null;
            }

            KieContainer container = kieServices.newKieContainer(
                    kieServices.getRepository().getDefaultReleaseId());
            System.out.println("✅ Drools KieContainer initialized successfully.");
            return container;

        } catch (Exception e) {
            System.out.println("❌ Error initializing Drools: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}