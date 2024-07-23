package com.github.rhafaelcosta.todolist.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {

    @Bean
    OpenAPI customOpenAPI() {
        var license = new License()
								.name("Apache 2.0")
								.url("http://www.apache.org/licenses/LICENSE-2.0.html");

		var contact = new Contact()
								.name("Rhafael Freitas da Costa")
								.email("rhafael.costa.dev@gmail.com");

        var info = new Info()
							.title("Activities Management System API")
							.version("v1")
							.description("API for managing tasks, tags, and users in an activity management system.")
							.contact(contact)
							.license(license);

		return new OpenAPI().info(info);
    }

}
