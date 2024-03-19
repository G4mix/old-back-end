package com.gamix.config;

import com.gamix.resolvers.comment.CommentMutationResolver;
import com.gamix.resolvers.comment.CommentQueryResolver;
import com.gamix.resolvers.comment.CommentResolver;
import com.gamix.resolvers.like.LikeMutationResolver;
import com.gamix.resolvers.post.PostMutationResolver;
import com.gamix.resolvers.post.PostQueryResolver;
import com.gamix.resolvers.post.PostResolver;
import com.gamix.resolvers.user.UserMutationResolver;
import com.gamix.resolvers.user.UserQueryResolver;
import graphql.GraphQL;
import graphql.kickstart.servlet.apollo.ApolloScalars;
import graphql.kickstart.tools.SchemaParser;
import graphql.kickstart.tools.SchemaParserBuilder;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig {

    @Bean
    public GraphQLScalarType uploadScalar() {
        return ApolloScalars.Upload;
    }

    @Bean
    public GraphQL graphQL(
            UserQueryResolver userQueryResolver, UserMutationResolver userMutationResolver,
            PostResolver postResolver, PostQueryResolver postQueryResolver, PostMutationResolver postMutationResolver,
            CommentResolver commentResolver, CommentQueryResolver commentQueryResolver, CommentMutationResolver commentMutationResolver,
            LikeMutationResolver likeMutationResolver
    ) {
        SchemaParserBuilder schemaParserBuilder = SchemaParser.newParser()
                .file("graphql/schema.graphqls")
                .scalars(uploadScalar())
                .resolvers(
                        userQueryResolver, userMutationResolver,
                        postResolver, postQueryResolver, postMutationResolver,
                        commentResolver, commentQueryResolver, commentMutationResolver,
                        likeMutationResolver
                );

        GraphQLSchema graphQLSchema = schemaParserBuilder.build().makeExecutableSchema();

        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurerUpload() {
        return wiringBuilder -> wiringBuilder.scalar(uploadScalar());
    }
}
