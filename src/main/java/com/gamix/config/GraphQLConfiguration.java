package com.gamix.config;

import com.gamix.resolvers.PostResolver;
import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLConfiguration {
    private final PostResolver postResolver;

    public GraphQLConfiguration(PostResolver postResolver) {
        this.postResolver = postResolver;
    }

    @Bean
    public GraphQLSchema graphQLSchema() {
        return SchemaParser.newParser()
                .file("schema.graphqls") // Arquivo que define o esquema GraphQL
                .resolvers(postResolver) // Adiciona os resolvers ao esquema
                .build()
                .makeExecutableSchema();
    }
}
